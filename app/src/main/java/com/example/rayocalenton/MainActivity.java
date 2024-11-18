package com.example.rayocalenton;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rayocalenton.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor, accelerometerSensor;

    private TextView lightValueText, orientationText, recommendationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Vincular vistas
        lightValueText = findViewById(R.id.text_light_value);
        orientationText = findViewById(R.id.text_orientation);
        recommendationText = findViewById(R.id.text_recommendation);

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
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            lightValueText.setText("Intensidad de luz: " + lightValue + " lx");

            // Recomendación basada en la luz
            if (lightValue < 100) {
                recommendationText.setText("Recomendación: Poca luz, busca un lugar más soleado.");
            } else if (lightValue < 500) {
                recommendationText.setText("Recomendación: Luz moderada, ideal para plantas de sombra.");
            } else {
                recommendationText.setText("Recomendación: Luz óptima para plantas de sol.");
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Verificar orientación (debe estar hacia arriba)
            if (Math.abs(z) > Math.abs(x) && Math.abs(z) > Math.abs(y)) {
                orientationText.setText("Orientación correcta: Sí");
            } else {
                orientationText.setText("Orientación correcta: No");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se requiere manejo en este caso
    }
}
