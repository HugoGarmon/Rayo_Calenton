package com.example.rayocalenton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String consulta = "CREATE TABLE Localizaciones (id INTEGER PRIMARY KEY AUTOINCREMENT, Localizacion VARCHAR(30), Nivel VARCHAR(10), Orientacion VARCHAR(20))";
        db.execSQL(consulta);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertarLocalizacion(String localizacion, String puntuacion, String orientacion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues datos = new ContentValues();
        datos.put("Localizacion",localizacion);
        datos.put("Nivel",puntuacion);
        datos.put("Orientacion",orientacion);
        db.insert("Localizaciones",null,datos);

    }
    public ArrayList<String> mostrarLocalizaciones(){
        SQLiteDatabase db = this.getReadableDatabase();
        //Hacemos la consulta
        String consulta = "SELECT * FROM Localizaciones";

        //Inicializamos el array
        ArrayList<String> locas = new ArrayList<>();

        //Ejecutamos la consulta y hacemos el cursor
        Cursor cursor = db.rawQuery(consulta,null);

        //Recorremos la tabla y metemos los datos en una cadena para meterla al array
        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(0);
                String location = cursor.getString(1);
                String level = cursor.getString(2);
                String orientation = cursor.getString(3);
                String cadena = "La planta "+id+" Localizacion: "+ location+ " Nivel: "+level+" Orientación: "+orientation;

                locas.add(cadena);
            }while (cursor.moveToNext());
        }

        return locas;
    }

}
