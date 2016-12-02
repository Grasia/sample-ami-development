/*
 * Copyright (C) 2014 Pablo Campillo-Sanchez <pabcampi@ucm.es>
 *
 * This software has been developed as part of the 
 * SociAAL project directed by Jorge J. Gomez Sanz
 * (http://grasia.fdi.ucm.es/sociaal)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package phat.android.apps.accelerometer;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import sim.android.AudioRecordWrapper;
import sim.android.SensorManagerWrapper;
import sim.android.SimManager;
import sim.android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.util.concurrent.Executor;

import phat.android.app.mic.SerieData;
import phat.android.app.mic.AudioTools;

import sim.android.media.AudioRecord;

public class AccelerometerXYPlotActivity extends Activity implements SensorEventListener{

	private static String TAG = "AccelerometerXYPlotActivity";
	private static final int UPDATE_RMS = 0x001;
	private boolean processing = false;
	private SensorManagerWrapper mSensorManager;
	private Sensor mAccelerometer;
	private XYPlot dynamicPlot;
	private SerieData xSeries;
	private SerieData ySeries;
	private SerieData zSeries;
	private double initialTime;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		// android boilerplate stuff
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiolevelxylot);
		initPlot();
		mSensorManager = SimManager.getSensorManager(this);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
		initialTime = (double) ((double) System.currentTimeMillis() / 1000.0);
	}
	
	

	private void initPlot() {
		xSeries = new SerieData("X");
		xSeries.setTimeWindow(10);

		ySeries = new SerieData("Y");
		ySeries.setTimeWindow(10);

		zSeries = new SerieData("Z");
		zSeries.setTimeWindow(10);

		// get handles to our View defined in layout.xml:
		dynamicPlot = (XYPlot) findViewById(R.id.dynamicXYPlot);
		//dynamicPlot.setRenderMode(RenderMode.USE_BACKGROUND_THREAD);

		dynamicPlot.addListener(xSeries);
		dynamicPlot.addListener(ySeries);
		dynamicPlot.addListener(zSeries);


		// only display whole numbers in domain labels
		dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));

		LineAndPointFormatter series1Format = new LineAndPointFormatter(
				Color.rgb(0, 0, 0), null, null, null);

		series1Format.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
		series1Format.getLinePaint().setStrokeWidth(10);
		dynamicPlot.addSeries(xSeries,
				series1Format);
		series1Format = new LineAndPointFormatter(
				Color.BLUE, null, null, null);

		series1Format.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
		series1Format.getLinePaint().setStrokeWidth(10);
		dynamicPlot.addSeries(ySeries,
				series1Format);

		series1Format = new LineAndPointFormatter(
				Color.RED, null, null, null);

		series1Format.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
		series1Format.getLinePaint().setStrokeWidth(10);
		dynamicPlot.addSeries(zSeries,
				series1Format);

		// thin out domain tick labels so they dont overlap each other:
		dynamicPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
		dynamicPlot.setDomainStepValue(1);

		dynamicPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
		dynamicPlot.setRangeStepValue(5);

		dynamicPlot.setRangeValueFormat(new DecimalFormat("###.#"));

		// uncomment this line to freeze the range boundaries:
		dynamicPlot.setRangeBoundaries(-15, 15, BoundaryMode.FIXED);
		dynamicPlot.setDomainBoundaries(-15, 15, BoundaryMode.AUTO);

		// create a dash effect for domain and range grid lines:
		DashPathEffect dashFx = new DashPathEffect(
				new float[]{PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
		dynamicPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
		dynamicPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
	}

	 @Override
	    protected void onResume() {
	        super.onResume();
	        processing = true;
	        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	    	processing = false;
	        mSensorManager.unregisterListener(this);
	    }



	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d(TAG, "initialTim1e = " + initialTime);
	   if (processing) {
		   float x = event.values[0];
		   float y = event.values[1];
		   float z = event.values[2];

		   Log.d(TAG, "initialTime = " + initialTime);

		   double ct = (double) ((double) System.currentTimeMillis() / 1000.0);

		   xSeries.add(ct - initialTime, x);
		   ySeries.add(ct - initialTime, y);
		   zSeries.add(ct - initialTime, z);

		   dynamicPlot.redraw();
	   }

	}




}
