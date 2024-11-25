package com.example.rayocalenton;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Lista extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ventana);

        Button btnvolver = (Button) findViewById(R.id.btnVolver);
        ListView lista = (ListView) findViewById(R.id.lista);

        BaseDeDatos db = new BaseDeDatos(this,"db2",null,1);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,db.mostrarLocalizaciones());
        lista.setAdapter(adapter);

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
