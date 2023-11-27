package edu.pucmm.ia.serverlessprueba2.funciones;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import edu.pucmm.ia.serverlessprueba2.encapsulaciones.Reserva;
import edu.pucmm.ia.serverlessprueba2.services.ReservaDynamoDbServices;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

/**
 * Funcion para trabajar el CRUD de la entidad Estudiantes.
 * Es importante crear los permisos necesarios para conectar DynamoDB con el servicio de los Serverless.
 * Se cambia el rol del serverless, pueden utilizar desde la plantilla "Permisos de microservicios sencillos" o
 * "Simple microservice permissions"
 *
 * Para el uso del ejemplo de debe crear una variable de ambiente llamada "TABLA_ESTUDIANTE" donde se indica la tabla
 * que será utilizada en DynamoDb.
 *
 * En caso que estemos utilizando el laboratorio de AWS Academy es necesario incluir el permiso AWSLambdaBasicExecutionRole y de escritura
 * AmazonDynamoDBFullAccess .
 */
public class FuncionCRUDReserva implements RequestStreamHandler {

    //Instanciando objeto el manejo de la base de datos.
    private ReservaDynamoDbServices dbServices = new ReservaDynamoDbServices();
    private Gson gson = new Gson();

    /**
     * Estaremos analizando el metodo de acceso a lo interno de la función y realizando la conversación.
     * @param input
     * @param output
     * @param context
     * @throws IOException
     */
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        //Objetos para el control de la salida.
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String cuerpoRecibido = null;
        JSONObject responseJson = new JSONObject();
        String salida = "";
        Reserva reserva = null;
        //
        try {
            //Parseando el objeto.
            JSONObject evento = (JSONObject) parser.parse(reader);

            //Ver la salida por la consola sobre la trama enviada por el APIGateway
            context.getLogger().log(""+evento.toJSONString());

            //Recuperando el metodo de acceso de la llamada del API.
            if(evento.get("requestContext")==null){
                throw new IllegalArgumentException("No respeta el API de entrada");
            }
            //String metodoHttp = ((JSONObject)((JSONObject)evento.get("httpMethod")).get("http")).get("method").toString();
            String metodoHttp = evento.get("httpMethod").toString();

            //Realizando la operacion
            switch (metodoHttp){
                case "GET":
                    ReservaDynamoDbServices.ListarEstudiantesResponse listarEstudiantesResponse = dbServices.listarEstudiantes(null, context);
                    salida = gson.toJson(listarEstudiantesResponse);
                    break;
                case "POST":
                case "PUT":
                    reserva = getEstudianteBodyJson(evento);
                    dbServices.insertarEstudianteTabla(reserva, context);
                    salida = gson.toJson(reserva);
                    break;
                case "DELETE":
                    reserva = getEstudianteBodyJson(evento);
                    dbServices.eliminarEstudiante(reserva, context);
                    salida = gson.toJson(reserva);
                    break;
            }

            //La información enviada por el metodo Post o Put estará disponible en la propiedad body:
            if(evento.get("body")!=null){
                cuerpoRecibido = evento.get("body").toString();
            }

            //Respuesta en el formato esperado:
            JSONObject responseBody = new JSONObject();
            responseBody.put("data", JsonParser.parseString(salida));
            responseBody.put("entrada", cuerpoRecibido);

            JSONObject headerJson = new JSONObject();
            headerJson.put("mi-header", "Mi propio header");
            headerJson.put("Content-Type", "application/json");

            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        }catch (Exception ex){
            responseJson.put("statusCode", 400);
            responseJson.put("exception", ex);
        }

        //Salida
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }

    /**
     * Encapsula el JSON enviado al objeto Estudiante.
     * @param json
     * @return
     */
    private Reserva getEstudianteBodyJson(JSONObject json) throws IllegalArgumentException{
        if(json.get("body")==null){
            throw new IllegalArgumentException("No envio el cuerpo en la trama.");
        }
        Reserva reserva =gson.fromJson(json.get("body").toString(), Reserva.class);
        return reserva;
    }
}
