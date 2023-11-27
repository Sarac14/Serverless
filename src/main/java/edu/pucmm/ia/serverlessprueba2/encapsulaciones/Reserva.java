package edu.pucmm.ia.serverlessprueba2.encapsulaciones;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.time.LocalTime;
import java.util.Date;

/**
 * Clase que utiliza en enlace para DynamoDB.
 */

@DynamoDBTable(tableName="reservas")

public class Estudiante {
    private int id;
    private String nombre;
    private String correo;

    private String laboratorio;
    private String fecha;
   // @DynamoDBAttribute(attributeName = "hora")
    private String hora;

    public Estudiante(){
        
    }

    public Estudiante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Estudiante(int id, String nombre, String correo, String laboratorio) {
        this.id = id;
        this.nombre = nombre;
       this.correo = correo;
        this.laboratorio = laboratorio;
    }

    public Estudiante(int id, String nombre, String correo, String laboratorio, String hora, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.laboratorio = laboratorio;
        this.hora = hora;
        this.fecha = fecha;
    }

    public Estudiante(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }
    @DynamoDBHashKey(attributeName="id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @DynamoDBAttribute(attributeName = "correo")
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    @DynamoDBAttribute(attributeName = "laboratorio")
    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }
    @DynamoDBAttribute(attributeName = "fecha")
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    @DynamoDBAttribute(attributeName = "hora")
    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "matricula=" + id +
                ", nombre='" + nombre + '\'' +
                ", carrera='" + correo + '\'' +
                '}';
    }
}
