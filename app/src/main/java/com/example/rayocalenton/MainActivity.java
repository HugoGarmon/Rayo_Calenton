package com.example.rayocalenton;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor, accelerometerSensor, magnetometerSensor;
    private TextView lightValueText, orientationText, recommendationText, compassText;
    private Button btnGuardar, btnLista;
    private ImageView compassImage;

    private float[] gravity;
    private float[] geomagnetic;
    private float currentAzimuth = 0f;
    String level = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Vincular vistas
        lightValueText = findViewById(R.id.text_light_value);
        orientationText = findViewById(R.id.text_orientation);
        recommendationText = findViewById(R.id.text_recommendation);
        compassText = findViewById(R.id.compass_text);
        compassImage = findViewById(R.id.compass_image);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnLista = findViewById(R.id.btnLista);




        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Guardar.class);
                String orientation = compassText.getText().toString();
                intent.putExtra("Nivel",level);
                intent.putExtra("Orientacion", orientation);
                startActivity(intent);
            }
        };
        btnGuardar.setOnClickListener(listener1);

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), Lista.class);
                startActivity(intent2);
            }
        };
        btnLista.setOnClickListener(listener2);


        // Configurar sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Sensor de luz
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            lightValueText.setText("Sensor de luz no disponible.");
        }

        // Sensor de acelerómetro
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometerSensor == null || magnetometerSensor == null) {
            compassText.setText("Sensores necesarios no disponibles.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometerSensor != null) {
            sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ProgressBar bar = findViewById(R.id.bar);

        // Sensor de luz
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            if(lightValue < 1200){
                level = "1";
                lightValueText.setText(getString(R.string.Intensidad) + " 1");
            }else if(lightValue < 2400){
                lightValueText.setText(getString(R.string.Intensidad) + " 2");
                level = "2";
            }else if(lightValue < 3600){
                lightValueText.setText(getString(R.string.Intensidad) + " 3");
                level = "3";
            }else if(lightValue < 4800){
                lightValueText.setText(getString(R.string.Intensidad) + " 4");
                level = "4";
            }else if(lightValue < 6000){
                lightValueText.setText(getString(R.string.Intensidad) + " 5");
                level = "5";
            }else{
                lightValueText.setText(getString(R.string.Intensidad) + " 5+");
                level = "5+";
            }

//            lightValueText.setText(getString(R.string.Intensidad) + " " + lightValue + " lx");

            // Asignar el valor de la luz a la barra
            bar.setProgress((int) lightValue);

            // Recomendación basada en la luz
            if (lightValue < 100) {
                recommendationText.setText(R.string.RecomendacionPocaLuz);
            } else if (lightValue < 500) {
                recommendationText.setText(R.string.RecomendacionMediaLuz);
            } else {
                recommendationText.setText(R.string.RecomendacionMuchaLuz);
            }
        }

        // Sensores de orientación
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]); // Ángulo en grados
                azimuth = (azimuth + 360) % 360; // Ajuste para valores negativos

                // Actualizar la imagen de la flecha
                compassText.setText("Dirección: " + Math.round(azimuth) + "°");
                if(azimuth <= 22.5 || azimuth > 337.5){
                    compassText.setText("Estas mirando hacia el Norte");
                }else if (azimuth > 22.5 && azimuth <= 67.5 ){
                    compassText.setText("Estas mirando hacia el Noreste");
                }else if (azimuth > 67.5 && azimuth <= 112.5 ){
                    compassText.setText("Estas mirando hacia el Este");
                }else if (azimuth > 112.5 && azimuth <= 157.5 ){
                    compassText.setText("Estas mirando hacia el Sureste");
                }else if (azimuth > 157.5 && azimuth <= 202.5 ){
                    compassText.setText("Estas mirando hacia el Sur");
                }else if (azimuth > 202.5 && azimuth <= 247.5 ){
                    compassText.setText("Estas mirando hacia el Suroeste");
                }else if (azimuth > 247.5 && azimuth <= 292.5 ){
                    compassText.setText("Estas mirando hacia el Oeste");
                }else if (azimuth > 292.5 && azimuth <= 337.5 ){
                    compassText.setText("Estas mirando hacia el Noroeste");
                }
                updateArrowRotation(azimuth);
            }
        }
    }


    private void updateArrowRotation(float azimuth) {
        ImageView arrowImage = findViewById(R.id.compass_image); // Cambia "arrow_image" por el ID de tu ImageView
        arrowImage.setRotation(-azimuth); // Rotación hacia el norte
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se requiere manejo en este caso
    }
}
