package edu.pucmm.ia.serverlessprueba2.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import edu.pucmm.ia.serverlessprueba2.encapsulaciones.Reserva;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase para encapsular la funcionalidad CRUD en la base de datos DynamoDB
 */
public class ReservaDynamoDbServices {


    /**
     * Función simplificando la salida relacionado
     * @param reserva
     * @param context
     * @return
     */
    
    public EstudianteResponse insertarEstudianteTabla(Reserva reserva, Context context){

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        if(reserva.getId() == 0 || reserva.getNombre().isEmpty()){
            return new EstudianteResponse(true, "Datos enviados no son válidos", null);
        }

        // Validar si hay menos de 7 estudiantes en el mismo horario
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":fecha", new AttributeValue().withS(reserva.getFecha()));
        expressionAttributeValues.put(":hora", new AttributeValue().withS(reserva.getHora()));
        expressionAttributeValues.put(":laboratorio", new AttributeValue().withS(reserva.getLaboratorio()));
        List<Reserva> estudiantesEnHorario = mapper.scan(Reserva.class, new DynamoDBScanExpression()
                .withFilterExpression("fecha = :fecha AND hora = :hora AND laboratorio = :laboratorio")
                .withExpressionAttributeValues(expressionAttributeValues));

        if(estudiantesEnHorario.size() < 7){
            try {
                mapper.save(reserva);
            }catch (Exception e){
                return new EstudianteResponse(true, e.getMessage(), null);
            }

            //Retornando
            return new EstudianteResponse(false,"", reserva);
        }
        return new EstudianteResponse(true, "Ya hay 7 solicitudes en este horario", null);
    }
    /**
     * Metodo para retornar el listado de todos los elementos de la tablas
     * @param filtro
     * @param context
     * @return
     */
    public ListarEstudiantesResponse listarEstudiantes(FiltroListaEstudiante filtro, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<Reserva> reservas = mapper.scan(Reserva.class, new DynamoDBScanExpression());

        return new ListarEstudiantesResponse(false, "", reservas);
    }

    /**
     * Función para eliminar un estudiantes
     * @param reserva
     * @param context
     * @return
     */

    public EstudianteResponse eliminarEstudiante(Reserva reserva, Context context){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        mapper.delete(reserva);

        return new EstudianteResponse(false, null, reserva);
    }

    /**
     * Representa el objeto que encapsula la información de la consulta
     */


    public static class ListarEstudiantesResponse{
        boolean error;
        String mensajeError;
        List<Reserva> reservas;

        public ListarEstudiantesResponse() {
        }

        public ListarEstudiantesResponse(boolean error, String mensajeError, List<Reserva> reservas) {
            this.error = error;
            this.mensajeError = mensajeError;
            this.reservas = reservas;
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

        public List<Reserva> getEstudiantes() {
            return reservas;
        }

        public void setEstudiantes(List<Reserva> reservas) {
            this.reservas = reservas;
        }
    }

    /**
     *  Encapsulación del objeto de respuesta.
     */
    public static class EstudianteResponse{
        boolean error;
        String mensajeError;
        Reserva reserva;

        public EstudianteResponse(){

        }

        public EstudianteResponse(boolean error, String mensajeError, Reserva reserva) {
            this.error = error;
            this.mensajeError = mensajeError;
            this.reserva = reserva;
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

        public Reserva getEstudiante() {
            return reserva;
        }

        public void setEstudiante(Reserva reserva) {
            this.reserva = reserva;
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
