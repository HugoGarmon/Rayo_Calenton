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

import com.example.rayocalenton.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor, accelerometerSensor;
    private TextView compassText;
    private ImageView compassImage;

    private float[] gravity;
    private float[] geomagnetic;
    private float currentAzimuth = 0f;

    private TextView lightValueText, orientationText, recommendationText, brujulaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Vincular vistas
        lightValueText = findViewById(R.id.text_light_value);
        orientationText = findViewById(R.id.text_orientation);
        recommendationText = findViewById(R.id.text_recommendation);
        brujulaText = findViewById(R.id.compass_text);

        // Configurar sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Sensor de luz
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            lightValueText.setText("Sensor de luz no disponible.");
        }

        // Sensor de acelerómetro
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            orientationText.setText("Sensor de acelerómetro no disponible.");
        }

        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (accelerometerSensor == null || magnetometer == null) {
            compassText.setText("Sensores necesarios no disponibles.");
        }

        ImageView compassImage = findViewById(R.id.compass_image);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ProgressBar bar = (ProgressBar) findViewById(R.id.bar);
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            lightValueText.setText(getString(R.string.Intensidad) + " " + lightValue + " lx");
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

                // Asegurar que esté en rango 0-360
                if (azimuth < 0) {
                    azimuth += 360;
                }

                // Mostrar el ángulo en texto
                compassText.setText("Dirección: " + Math.round(azimuth) + "°");

                // Rotar la imagen suavemente
                rotateCompassImage(azimuth);
            }
        }

    }
    private void rotateCompassImage(float azimuth) {
        // Calcula la diferencia para rotación suave
        float rotationAngle = azimuth - currentAzimuth;

        // Rota la imagen
        compassImage.animate()
                .rotationBy(rotationAngle)
                .setDuration(500) // Duración de la animación en milisegundos
                .start();

        // Actualizar el ángulo actual
        currentAzimuth = azimuth;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se requiere manejo en este caso
    }
}
