package com.example.rayocalenton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Guardar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardar_ventana);

        Button btnvolver = (Button) findViewById(R.id.btnVolver);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
        btnvolver.setOnClickListener(listener);

    }
}
