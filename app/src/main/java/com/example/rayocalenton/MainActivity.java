package com.example.rayocalenton;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor, accelerometerSensor, magnetometerSensor;
    private TextView lightValueText, orientationText, recommendationText, compassText;
    private ImageView compassImage;

    private float[] gravity;
    private float[] geomagnetic;
    private float currentAzimuth = 0f;

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
            lightValueText.setText(getString(R.string.Intensidad) + " " + lightValue + " lx");

            // Normalizar el progreso de la barra
            int progress = (int) Math.min((lightValue / 1000) * 100, 100); // Escalar 0-100
            bar.setProgress(progress);

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
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];

            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                // Dirección en radianes, convertir a grados
                float azimuth = (float) Math.toDegrees(orientation[0]);
                if (azimuth < 0) azimuth += 360;

                compassText.setText("Dirección: " + Math.round(azimuth) + "°");
                rotateCompassImage(-azimuth);
            }
        }
    }

    private void rotateCompassImage(float azimuth) {
        float rotationAngle = azimuth - currentAzimuth;
        compassImage.animate().rotationBy(rotationAngle).setDuration(0).start();
        currentAzimuth = azimuth;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se requiere manejo en este caso
    }
}
