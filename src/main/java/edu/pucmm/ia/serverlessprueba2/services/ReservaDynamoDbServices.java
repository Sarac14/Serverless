package edu.pucmm.ia.serverlessprueba2.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import edu.pucmm.ia.serverlessprueba2.encapsulaciones.Estudiante;
import edu.pucmm.ia.serverlessprueba2.util.ServerlessHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

/**
 * Clase para encapsular la funcionalidad CRUD en la base de datos DynamoDB
 */
public class EstudianteDynamoDbServices {


    /**
     * Función simplificando la salida relacionado
     * @param estudiante
     * @param context
     * @return
     */

//    public EstudianteResponse insertarEstudianteTabla(Estudiante estudiante, Context context){
//
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
//        DynamoDB dynamoDB = new DynamoDB(client);
//
//        if(estudiante.getId() == 0 || estudiante.getNombre().isEmpty()){
//            throw new RuntimeException("Datos enviados no son validos");
//        }
//
//
//        try {
//
//            Table table = dynamoDB.getTable(ServerlessHelper.getNombreTabla());
//            Item item = new Item().withPrimaryKey("id", estudiante.getId())
//                    .withString("nombre", estudiante.getNombre())
//                    .withString("correo", estudiante.getCorreo())
//                    .withString("laboratorio", estudiante.getLaboratorio())
//                    //.withString("idEstudiante", estudiante.getIdEstudiante())
//                    .withString("fecha", estudiante.getFecha())
//                    .withString("hora", estudiante.getHora());
//
//
//            PutItemOutcome putItemOutcome = table.putItem(item);
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//            context.getLogger().log("Error al insertar el estudiante: " + e.getMessage());
//            return new EstudianteResponse(true, e.getMessage(), null);
//        }
//
//        //Retornando
//        return new EstudianteResponse(false, null, estudiante);
//    }
    public EstudianteResponse insertarEstudianteTabla(Estudiante estudiante, Context context){

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        if(estudiante.getId() == 0 || estudiante.getNombre().isEmpty()){
            return new EstudianteResponse(true, "Datos enviados no son válidos", null);
        }

        // Validar si hay menos de 7 estudiantes en el mismo horario
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":fecha", new AttributeValue().withS(estudiante.getFecha()));
        expressionAttributeValues.put(":hora", new AttributeValue().withS(estudiante.getHora()));
        expressionAttributeValues.put(":laboratorio", new AttributeValue().withS(estudiante.getLaboratorio()));
        List<Estudiante> estudiantesEnHorario = mapper.scan(Estudiante.class, new DynamoDBScanExpression()
                .withFilterExpression("fecha = :fecha AND hora = :hora AND laboratorio = :laboratorio")
                .withExpressionAttributeValues(expressionAttributeValues));

        if(estudiantesEnHorario.size() < 7){
            try {
                mapper.save(estudiante);
            }catch (Exception e){
                return new EstudianteResponse(true, e.getMessage(), null);
            }

            //Retornando
            return new EstudianteResponse(false,"", estudiante);
        }
        return new EstudianteResponse(true, "Ya hay 7 solicitudes en este horario", null);
    }
    /**
     * Metodo para retornar el listado de todos los elementos de la tablas
     * @param filtro
     * @param context
     * @return
     */
//    public ListarEstudiantesResponse listarEstudiantes(FiltroListaEstudiante filtro, Context context) {
//        AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
//
//        List<Estudiante> estudiantes = new ArrayList<>();
//
//        ScanRequest scanRequest = new ScanRequest().withTableName(ServerlessHelper.getNombreTabla());
//        ScanResult result = null;
//
//        do {// La consulta vía ScanRequest solo retorna 1 MB de datos por iteracion,
//            //debemos iterar.
//
//            if (result != null) {
//                scanRequest.setExclusiveStartKey(result.getLastEvaluatedKey());
//            }
//            result = ddb.scan(scanRequest);
//            List<Map<String, AttributeValue>> rows = result.getItems();
//
//            // Iterando todos los elementos
//            for (Map<String, AttributeValue> mapEstudiantes : rows) {
//                System.out.println(""+mapEstudiantes);
//                //
//                AttributeValue matriculaAtributo = mapEstudiantes.get("id");
//                AttributeValue nombreAtributo = mapEstudiantes.get("nombre");
//                AttributeValue carreraAtributo = mapEstudiantes.get("correo");
//                AttributeValue labAtributo = mapEstudiantes.get("laboratorio");
//                AttributeValue idEstAtributo = mapEstudiantes.get("idEstudiante");
//                AttributeValue fechaAtributo = mapEstudiantes.get("fecha");
//                AttributeValue horaAtributo = mapEstudiantes.get("hora");
//
//                //
//
//
//                Estudiante tmp = new Estudiante();
//                tmp.setId(Integer.valueOf(matriculaAtributo.getN()));
//                if(nombreAtributo!=null){
//                   tmp.setNombre(nombreAtributo.getS());
//                }
//                if(carreraAtributo!=null){
//                    tmp.setCorreo(carreraAtributo.getS());
//                }
//                if(labAtributo!=null){
//                    tmp.setLaboratorio(labAtributo.getS());
//                }
//               /* if(idEstAtributo!=null){
//                    tmp.setIdEstudiante(idEstAtributo.getS());
//                }*/
//                if (fechaAtributo != null) {
//                    tmp.setFecha(fechaAtributo.getS());
//                }
//
//                if(horaAtributo!=null){
//                    tmp.setHora(horaAtributo.getS());
//                }
//                //
//                estudiantes.add(tmp);
//            }
//
//        } while (result.getLastEvaluatedKey() != null);
//
//        return new ListarEstudiantesResponse(false, "", estudiantes);
//    }

    public ListarEstudiantesResponse listarEstudiantes(FiltroListaEstudiante filtro, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<Estudiante> estudiantes = mapper.scan(Estudiante.class, new DynamoDBScanExpression());

        return new ListarEstudiantesResponse(false, "", estudiantes);
    }

    /**
     * Función para eliminar un estudiantes
     * @param estudiante
     * @param context
     * @return
     */
//    public EstudianteResponse eliminarEstudiante(Estudiante estudiante, Context context){
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
//        DynamoDB dynamoDB = new DynamoDB(client);
//
//        Table table = dynamoDB.getTable(ServerlessHelper.getNombreTabla());
//
//        DeleteItemOutcome outcome = table.deleteItem("id", estudiante.getId());
//        return new EstudianteResponse(false, null, estudiante);
//    }

    public EstudianteResponse eliminarEstudiante(Estudiante estudiante, Context context){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        mapper.delete(estudiante);

        return new EstudianteResponse(false, null, estudiante);
    }

    /**
     * Representa el objeto que encapsula la información de la consulta
     */


    public static class ListarEstudiantesResponse{
        boolean error;
        String mensajeError;
        List<Estudiante> estudiantes;

        public ListarEstudiantesResponse() {
        }

        public ListarEstudiantesResponse(boolean error, String mensajeError, List<Estudiante> estudiantes) {
            this.error = error;
            this.mensajeError = mensajeError;
            this.estudiantes = estudiantes;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getMensajeError() {
            return mensajeError;
        }

        public void setMensajeError(String mensajeError) {
            this.mensajeError = mensajeError;
        }

        public List<Estudiante> getEstudiantes() {
            return estudiantes;
        }

        public void setEstudiantes(List<Estudiante> estudiantes) {
            this.estudiantes = estudiantes;
        }
    }

    /**
     *  Encapsulación del objeto de respuesta.
     */
    public static class EstudianteResponse{
        boolean error;
        String mensajeError;
        Estudiante estudiante;

        public EstudianteResponse(){

        }

        public EstudianteResponse(boolean error, String mensajeError, Estudiante estudiante) {
            this.error = error;
            this.mensajeError = mensajeError;
            this.estudiante = estudiante;
        }

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getMensajeError() {
            return mensajeError;
        }

        public void setMensajeError(String mensajeError) {
            this.mensajeError = mensajeError;
        }

        public Estudiante getEstudiante() {
            return estudiante;
        }

        public void setEstudiante(Estudiante estudiante) {
            this.estudiante = estudiante;
        }
    }

    public static class FiltroListaEstudiante{
        String filtro;

        public String getFiltro() {
            return filtro;
        }

        public void setFiltro(String filtro) {
            this.filtro = filtro;
        }
    }

}
