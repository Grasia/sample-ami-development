package phat.android.apps.camera;

/**
 * *****************************************************************************
 * Look! is a Framework of Augmented Reality for Android.
 *
 * Copyright (C) 2011 Sergio Bellon Alcarazo Jorge Creixell Rojo Agel Serrano
 * Laguna
 *
 * Final Year Project developed to Sistemas Informáticos 2010/2011 - Facultad
 * de Informática - Universidad Complutense de Madrid - Spain
 *
 * Project led by: Jorge J. Gomez Sanz
 *
 *
 * ****************************************************************************
 *
 * This file is part of Look! (http://lookar.sf.net/)
 *
 * Look! is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/
 *****************************************************************************
 */
import android.graphics.Paint;
import android.hardware.Camera.Parameters;
import android.opengl.GLSurfaceView;

import java.io.IOException;

import sim.android.CameraWrapper;
import sim.android.SimManager;
import sim.android.app.Activity;
import sim.android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

public class Preview extends GLSurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private CameraWrapper mCamera;
    private Activity activity;

    public Preview(Activity context) {
        super(context);
        Log.i(getClass().getName(), "new Preview: context = " + context);        
        this.activity = context;
        setLayerType(View.LAYER_TYPE_HARDWARE, new Paint());

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        Log.i(getClass().getName(), "new Preview: getHolder() = " + mHolder);
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
        Log.d("Preview", "is hardware accelerated = "+isHardwareAccelerated());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(getClass().getName(), "surfaceCreated: holder = "+holder);
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        
        mCamera = SimManager.getCamera().open();
        try {            
            mCamera.setPreviewDisplay(holder);
        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
            // TODO: add more exception handling logic here
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        Log.i("activity", "camera surfaceDestroyed");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.

        Parameters parameters = mCamera.getParameters();

        parameters.setPreviewSize(w, h);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 270;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 90;
                break;
        }

        //CameraParametersHelper.setCameraParameters(mCamera, w, h);
        mCamera.setDisplayOrientation((degrees + 90) % 360);
        mCamera.startPreview();
        Log.i("activity", "camera surfaceChanged");
    }
}
