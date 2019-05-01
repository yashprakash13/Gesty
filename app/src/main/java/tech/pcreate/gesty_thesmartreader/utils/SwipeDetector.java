/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.utils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SwipeDetector implements View.OnTouchListener{

    private int min_distance = 100;
    private float downX, downY, upX, upY;
    private View v;

    private onSwipeEvent swipeEventListener;



    public SwipeDetector(View v){
        this.v=v;
        v.setOnTouchListener(this);
    }

    public void setOnSwipeListener(onSwipeEvent listener)
    {
        try{
            swipeEventListener=listener;
        }
        catch(ClassCastException e)
        {
            Log.e("ClassCastException","please pass SwipeDetector.onSwipeEvent Interface instance",e);
        }
    }


    public void onRightToLeftSwipe(){
        if(swipeEventListener!=null)
            swipeEventListener.SwipeEventDetected(v,SwipeTypeEnum.RIGHT_TO_LEFT);
        else
            Log.e("SwipeDetector error","please pass SwipeDetector.onSwipeEvent Interface instance");
    }

    public void onLeftToRightSwipe(){
        if(swipeEventListener!=null)
            swipeEventListener.SwipeEventDetected(v,SwipeTypeEnum.LEFT_TO_RIGHT);
        else
            Log.e("SwipeDetector error","please pass SwipeDetector.onSwipeEvent Interface instance");
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                //HORIZONTAL SCROLL
                if(Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if(Math.abs(deltaX) > min_distance){
                        // left or right
                        if(deltaX < 0)
                        {
                            this.onLeftToRightSwipe();
                            return true;
                        }
                        if(deltaX > 0) {
                            this.onRightToLeftSwipe();
                            return true;
                        }
                    }
                    else {
                        //not long enough swipe...
                        return true;
                    }
                }
                return true;
            }
        }
        return true;
    }
    public interface onSwipeEvent
    {
        public void SwipeEventDetected(View v, SwipeTypeEnum SwipeType);
    }

    public SwipeDetector setMinDistanceInPixels(int min_distance)
    {
        this.min_distance=min_distance;
        return this;
    }

    public enum SwipeTypeEnum
    {
        RIGHT_TO_LEFT,LEFT_TO_RIGHT
    }

}