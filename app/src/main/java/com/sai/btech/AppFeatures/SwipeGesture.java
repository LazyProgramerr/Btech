package com.sai.btech.AppFeatures;

import static com.sai.btech.activities.HomeActivity.change;
import static com.sai.btech.activities.HomeActivity.fragementPosition;
import static com.sai.btech.activities.HomeActivity.smoothBottomBar;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGesture extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Check if MotionEvent objects are not null
        if (e1 == null || e2 == null) {
            return false;
        }

        // Check if other related variables are not null
        if (smoothBottomBar == null) {
            return false;
        }

        // Define your threshold for swipe detection
        int swipeThreshold = 200;
        // Calculate the difference in X coordinates
        float deltaX = e2.getX() - e1.getX();

        if (deltaX > swipeThreshold) {
            // Swipe the fragments towards the right
            if (fragementPosition > 0 && fragementPosition <= 3) {
                change(--fragementPosition);
                smoothBottomBar.setItemActiveIndex(fragementPosition);
            }
        } else if (deltaX < -swipeThreshold) {
            // Swipe the fragments towards the left
            if (fragementPosition >= 0 && fragementPosition < 3) {
                change(++fragementPosition);
                smoothBottomBar.setItemActiveIndex(fragementPosition);
            }
        }
        return true;
    }

}

