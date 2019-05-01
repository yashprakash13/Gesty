/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.gestyMode;

import android.content.Context;

public class TurnDetector {

    private Context mContext;
    private GestyDetector gestureDetector;

    public TurnDetector(Context mContext) {
        this.mContext = mContext;
    }

    public void startDetector(){
        gestureDetector = new GestyDetector(mContext);
        gestureDetector.startDetector();
    }

    public void stopDetector(){
        gestureDetector.stopDetector();
    }
}
