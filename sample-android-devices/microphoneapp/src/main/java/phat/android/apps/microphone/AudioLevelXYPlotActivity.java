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
package phat.android.apps.microphone;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import sim.android.AudioRecordWrapper;
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

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.util.concurrent.Executor;

import phat.android.app.mic.AudioLevelSeries;
import phat.android.app.mic.AudioTools;
import phat.android.apps.microphone.R;
import sim.android.media.AudioRecord;

public class AudioLevelXYPlotActivity extends Activity {

    private static String TAG = "AudioLevelXYPlotActivity";
    private static final int UPDATE_RMS = 0x001;
    private boolean processing = false;
    private EventHandler mEventHandler;
    private DirectExecutor mDirectExecutor;
    private XYPlot dynamicPlot;
    private AudioLevelSeries audioSeries;

    class DirectExecutor implements Executor {
        @Override
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }

   

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // android boilerplate stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiolevelxylot);

        initPlot();
        
        Looper looper;
        if ((looper = Looper.myLooper()) != null) {
            mEventHandler = new EventHandler(looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            mEventHandler = new EventHandler(looper);
        } else {
            mEventHandler = null;
        }

        mDirectExecutor = new DirectExecutor();
    }

    private void initPlot() {
        audioSeries = new AudioLevelSeries("Mic Intensity Level");
        audioSeries.setTimeWindow(10);

        // get handles to our View defined in layout.xml:
        dynamicPlot = (XYPlot) findViewById(R.id.dynamicXYPlot);
        //dynamicPlot.setRenderMode(RenderMode.USE_BACKGROUND_THREAD);

        dynamicPlot.addListener(audioSeries);

        // only display whole numbers in domain labels
        dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));

        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 0, 0), null, null, null);
        series1Format.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        series1Format.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(audioSeries,
                series1Format);

        // thin out domain tick labels so they dont overlap each other:
        dynamicPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setDomainStepValue(1);

        dynamicPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setRangeStepValue(5);

        dynamicPlot.setRangeValueFormat(new DecimalFormat("###.#"));

        // uncomment this line to freeze the range boundaries:
        dynamicPlot.setRangeBoundaries(-0.1, 50, BoundaryMode.FIXED);
        dynamicPlot.setDomainBoundaries(0, 10, BoundaryMode.AUTO);

        // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[]{PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        dynamicPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
        dynamicPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
    }

    @Override
    public void onResume() {
        super.onResume();

        processing = true;
        mDirectExecutor.execute(new RMSAudioRunnableTask());
    }

    @Override
    public void onPause() {
        super.onPause();
        processing = false;
    }

    class RMSAudioRunnableTask implements Runnable {
        @Override
        public void run() {
        	 Log.d(TAG, "process()");
             // Audio Format = PCM_SIGNED 44100.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
             int frequency = 44100;
             int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
             int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

             try {
                 // Create a new AudioRecord object to record the audio.
                 int bufferSize = AudioRecordWrapper.getMinBufferSize(frequency,
                         channelConfiguration, audioEncoding);
                 Log.d(TAG, "audioRecord...");
                 AudioRecordWrapper audioRecord = SimManager.getAudio(
                         MediaRecorder.AudioSource.MIC, frequency,
                         channelConfiguration, audioEncoding, bufferSize);
                 Log.d(TAG, "...audioRecord");
                 byte[] buffer = new byte[bufferSize];
                 Log.d(TAG, "startRecording...");
                 audioRecord.startRecording();

                 AudioTrack audioTrack = new AudioTrack(
                         AudioManager.STREAM_MUSIC,
                         frequency,
                         channelConfiguration,
                         audioEncoding,
                         bufferSize,
                         AudioTrack.MODE_STREAM);
                 audioTrack.play();
                 audioTrack.setPlaybackRate(frequency);

                 double initialTime = (double) ((double) System.currentTimeMillis() / 1000.0);
                 Log.d(TAG, "initialTime = " + initialTime);
                 while (processing) {
                     int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                     if (bufferReadResult > 0) {
                         double rms = AudioTools.volumeRMS(buffer, 0, bufferReadResult);
                         double ct = (double) ((double) System.currentTimeMillis() / 1000.0);

                         audioSeries.add(ct - initialTime, rms);
                         Message m = mEventHandler.obtainMessage(UPDATE_RMS, 0, 0, rms);
                         mEventHandler.sendMessage(m);
                         audioTrack.write(buffer, 0, bufferReadResult);
                     }
                 }

                 audioRecord.stop();
                 audioTrack.stop();

             } catch (Throwable t) {
                 Log.e("AudioRecord", "Recording Failed");
                 if (t != null && t.getMessage() != null) {
                     Log.e("AudioRecord", t.getMessage());
                 }
             }
        }
    }
    
  

    private class EventHandler extends Handler {

        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_RMS:
                    //Log.d(TAG, "updateRMS = " + msg.obj);
                    dynamicPlot.redraw();
                    return;
                default:
                    Log.e(TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }
}
