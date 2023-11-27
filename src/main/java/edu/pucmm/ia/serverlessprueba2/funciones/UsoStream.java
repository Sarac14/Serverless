package edu.pucmm.ia.ds.funciones;

import com.amazonaws.services.lambda.runtime.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Permite utilizar el control de flujo (Stream), para no trabajar con JSON a la salida.
 */
public class UsoStream {

    /**
     * La entrada en input debe ser una trama valida JSON. El output no tiene esa limitación.
     * @param inputStream
     * @param outputStream
     * @param context
     * @throws IOException
     */
    public void controlFlujo(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        int letter;
        while((letter = inputStream.read()) != -1){
            System.out.print((char)letter);
            outputStream.write(Character.toUpperCase(letter));
        }
    }
}
