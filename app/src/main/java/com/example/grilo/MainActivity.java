package com.example.grilo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mLuz;
    private MediaPlayer mp;
    private boolean grilo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLuz = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        grilo = false;
        mSensorManager.registerListener(new LuzSensor(), mLuz,
                SensorManager.SENSOR_DELAY_UI);
    }

    class LuzSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            if (vl < 10) {
                grilo();
            } else {
                parar();
            }
        }
    }
    public void parar() {
        grilo = false;
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(getApplicationContext(), R.raw.grilo);
        }
    }

    private void grilo() {
        if (!grilo) {
            grilo = true;
            mp = MediaPlayer.create(getApplicationContext(), R.raw.grilo);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    grilo = false;
                    mp.release();
                }
            });
        mp.start();
    }
}
}