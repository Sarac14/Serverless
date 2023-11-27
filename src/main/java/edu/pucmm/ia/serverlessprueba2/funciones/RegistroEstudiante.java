package edu.pucmm.ia.serverlessprueba2.funciones;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import edu.pucmm.ia.serverlessprueba2.encapsulaciones.Reserva;

public class RegistroEstudiante {

    /**
     * No utilizo la interfaz
     * @param reserva
     * @param context
     * @return
     */
    public RegistroEstudianteResponse registroEstudiante(Reserva reserva, Context context){
        LambdaLogger log = context.getLogger();
        log.log("El estudiante obtenido: "+ reserva.toString());
        //pendiente que hacer.
        return new RegistroEstudianteResponse(true, reserva);
    }

    static class RegistroEstudianteResponse{
        boolean creado;
        Reserva reserva;

        public RegistroEstudianteResponse(){
            
        }

        public RegistroEstudianteResponse(boolean creado, Reserva reserva) {
            this.creado = creado;
            this.reserva = reserva;
        }

        public boolean isCreado() {
            return creado;
        }

        public void setCreado(boolean creado) {
            this.creado = creado;
        }

        public Reserva getEstudiante() {
            return reserva;
        }

        public void setEstudiante(Reserva reserva) {
            this.reserva = reserva;
        }
    }
}
