/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.gestyMode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import tech.pcreate.gesty_thesmartreader.epubReader.ReaderActivity;
import tech.pcreate.gesty_thesmartreader.utils.AppConstants;

public class GestyDetector implements SensorEventListener {

    private Context mContext;

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 2;

    private long mThreshold = 1700;
    private long mScrollThreshold = 3000;
    private long startMilli;
    private long endMilli;


    public GestyDetector(Context mContext) {
        this.mContext = mContext;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);/*Copyright (c) @ 2019 Yash Prakash
         */
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    public void stopDetector(){
        mSensorManager.unregisterListener(this);
    }

    public void startDetector(){
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                //Toast.makeText(mContext, "near", Toast.LENGTH_SHORT).show();
                startMilli = System.currentTimeMillis();

            } else {
                //far
                //Toast.makeText(mContext, "far", Toast.LENGTH_SHORT).show();
                endMilli = System.currentTimeMillis();
                changePage(endMilli - startMilli);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void changePage(long l) {

        //Log.e("Time: ", String.valueOf(l));
        startMilli = 0;

        if (l < mThreshold) {
            //Toast.makeText(mContext, "Next ", Toast.LENGTH_SHORT).show();
            setPageTurnEvent( AppConstants.NEXT);
        }
        else if (l <= mScrollThreshold){
            //Toast.makeText(mContext, "Previous", Toast.LENGTH_SHORT).show(); /*Copyright (c) @ 2019 Yash Prakash
            //                 */
            setPageTurnEvent(AppConstants.PREVIOUS);
        }else{
            setScrollEvent();
        }

    }

    private void setScrollEvent() {
        ReaderActivity.startScrolling(!ReaderActivity.isScrolling);
    }

    private void setPageTurnEvent(int i) {
        ReaderActivity.turnPage(i);
    }

}
