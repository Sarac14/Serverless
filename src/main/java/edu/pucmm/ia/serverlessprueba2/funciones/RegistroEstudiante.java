package edu.pucmm.ia.ds.funciones;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import edu.pucmm.ia.ds.encapsulaciones.Estudiante;

public class RegistroEstudiante {

    /**
     * No utilizo la interfaz
     * @param estudiante
     * @param context
     * @return
     */
    public RegistroEstudianteResponse registroEstudiante(Estudiante estudiante, Context context){
        LambdaLogger log = context.getLogger();
        log.log("El estudiante obtenido: "+estudiante.toString());
        //pendiente que hacer.
        return new RegistroEstudianteResponse(true, estudiante);
    }

    static class RegistroEstudianteResponse{
        boolean creado;
        Estudiante estudiante;

        public RegistroEstudianteResponse(){
            
        }

        public RegistroEstudianteResponse(boolean creado, Estudiante estudiante) {
            this.creado = creado;
            this.estudiante = estudiante;
        }

        public boolean isCreado() {
            return creado;
        }

        public void setCreado(boolean creado) {
            this.creado = creado;
        }

        public Estudiante getEstudiante() {
            return estudiante;
        }

        public void setEstudiante(Estudiante estudiante) {
            this.estudiante = estudiante;
        }
    }
}
