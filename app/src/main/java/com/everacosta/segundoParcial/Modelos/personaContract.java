package com.everacosta.segundoParcial.Modelos;

public class personaContract {


    public static final String TABLE_NAME = "personas";
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String ESTRATO = "estrato";
    public static final String SALARIO = "salario";
    public static final String NIVELE = "niveleducativo";
    public static final String CEDULA = "cedula";

    static final String CREAR_TABLA_PERSONA = "CREATE TABLE personas "+"(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOMBRE + " TEXT," + CEDULA +
            " TEXT," + SALARIO + " TEXT,"+ESTRATO + " TEXT," + NIVELE + " TEXT)";
}
