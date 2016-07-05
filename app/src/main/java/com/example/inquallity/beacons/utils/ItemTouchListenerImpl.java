package com.example.inquallity.beacons.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author Maksim Radko
 */
public class ItemTouchListenerImpl implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;

    public ItemTouchListenerImpl(@NonNull Context context, @NonNull GestureDetector.OnGestureListener listener) {
        mGestureDetector = new GestureDetector(context, listener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
