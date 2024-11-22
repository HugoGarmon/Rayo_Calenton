package com.example.rayocalenton;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String consulta = "CREATE TABLE Localizaciones (id INTEGER PRIMARY KEY AUTOINCREMENT, Localidad VARCHAR(30), Nivel VARCHAR(10), Orientacion VARCHAR(20))";
        db.execSQL(consulta);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
