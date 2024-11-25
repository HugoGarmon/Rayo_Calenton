package com.example.rayocalenton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Guardar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardar_ventana);

        Button btnvolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        EditText etLocas = (EditText) findViewById(R.id.editLocalidad);
        TextView textNivel = (TextView) findViewById(R.id.textNivel);
        TextView textOrientacion = (TextView) findViewById(R.id.textoOrientacion);

        //Creamos un objeto de la base de datos para usar el metodo insertar
        BaseDeDatos db = new BaseDeDatos(this,"db2",null,1);

        //Cogemos las variables del padre y las mostramos
        Intent intent = getIntent();
        String level = intent.getStringExtra("Nivel");
        String orientation = intent.getStringExtra("Orientacion");

        //Seteamos las variables nivel y orientacion
        textNivel.setText("Nivel: "+level);
        textOrientacion.setText(textOrientacion.getText().toString()+" "+orientation);




        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertamos la localizacion el nivel y la orientacion
                db.insertarLocalizacion(etLocas.getText().toString(),level,orientation);
                finish();
            }
        };
        btnGuardar.setOnClickListener(listener);

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
