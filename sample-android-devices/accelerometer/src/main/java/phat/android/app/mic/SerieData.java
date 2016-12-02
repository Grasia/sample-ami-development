/*
 * Copyright (C) 2014 Pablo Campillo-Sanchez <pabcampi@ucm.es>
 * 
 * Modified version by JJGomez Sanz
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

package phat.android.app.mic;

import android.graphics.Canvas;
import android.util.Log;
import com.androidplot.Plot;
import com.androidplot.PlotListener;
import com.androidplot.xy.XYSeries;
import java.util.ArrayList;
import java.util.List;

public class SerieData implements XYSeries, PlotListener {


    private String title;
    List<Number> xValues = new ArrayList<Number>();
    List<Number> yValues = new ArrayList<Number>();
    List<Number> xValuesBuf = new ArrayList<Number>();
    List<Number> yValuesBuf = new ArrayList<Number>();
    Integer timeWindow;
    float rate = -1f;

    public SerieData(String title) {
        this.title = title;
        
    }

    public void setTimeWindow(Integer seconds) {
        timeWindow = new Integer(seconds);
    }

    public Integer getTimeWindow() {
        return timeWindow;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int size() {
        return xValues.size();
    }

    @Override
    public Number getX(int index) {
        return xValues.get(index);
    }

    @Override
    public Number getY(int index) {
        return yValues.get(index);
    }
    final Object mutex = new Object();

    public void add(Number x, Number y) {
        synchronized (mutex) {
            if (!drawing) {
                updateValues();
                add(x, y, xValues, yValues);
                remove(xValues, yValues);
            } else {
                add(x, y, xValuesBuf, yValuesBuf);
            }
        }
        //printLast(5);
    }

    private void updateValues() {
        if (xValuesBuf.size() > 0) {
            xValues.addAll(xValuesBuf);
            yValues.addAll(yValuesBuf);
            xValuesBuf.clear();
            yValuesBuf.clear();
        }
    }

    private void add(Number x, Number y, List<Number> xV, List<Number> yV) {
        if (xV.size() > 1) {
            float last = xV.get(xV.size() - 1).floatValue();
            float distance = x.floatValue() - last;
            if (distance < 0.1f) {
                return;
            }
        }
        xV.add(x);
        yV.add(y);
    }

    private void remove(List<Number> xV, List<Number> yV) {
        if (timeWindow != null) {
            float last = xV.get(xV.size() - 1).floatValue();
            float distance = last - xV.get(0).floatValue();
            while (distance > timeWindow) {
                xV.remove(0);
                yV.remove(0);
                distance = last - xV.get(0).floatValue();
            }
        }
    }

    private void printLast(int n) {
        int size = xValues.size();
        if (size < n) {
            return;
        }
        String result = "";
        for (int i = size - n; i < size; i++) {
            result += "(" + xValues.get(i) + "," + yValues.get(i) + "), ";
        }
        Log.d(title, result);
    }
    boolean drawing = false;

    @Override
    public void onBeforeDraw(Plot plot, Canvas canvas) {
        synchronized (mutex) {
            drawing = true;
        }
    }

    @Override
    public void onAfterDraw(Plot plot, Canvas canvas) {
        synchronized (mutex) {
            drawing = false;
        }
    }
}
