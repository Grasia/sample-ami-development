package phat.android.apps.bodypos;

import android.app.Activity;
import android.content.Context;
import sim.android.SensorManagerWrapper;
import sim.android.SimManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Original code from http://www.techrepublic.com/blog/software-engineer/a-quick-tutorial-on-coding-androids-accelerometer/
 * with a few changes
 */
public class BodyPositionMonitoring extends Activity implements SensorEventListener {

    private boolean mInitialized;
    private SensorManagerWrapper mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;

 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_position_monitoring);
        mInitialized = false;
        mSensorManager = SimManager.getSensorManager(this);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        ImageView iv = (ImageView) findViewById(R.id.image);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            mInitialized = true;
        } else {
            tvX.setText(Float.toString(x));
            tvY.setText(Float.toString(y));
            tvZ.setText(Float.toString(z));
            iv.setVisibility(View.VISIBLE);
            if (y > 8f) {
                iv.setImageResource(R.drawable.upright);
            } else if (z < 5f) {
                iv.setImageResource(R.drawable.upside_down);
            } else if (z > 5f) {
                iv.setImageResource(R.drawable.facing_up);
            } else {
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }
}
