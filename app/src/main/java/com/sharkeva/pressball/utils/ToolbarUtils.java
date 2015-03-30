package com.sharkeva.pressball.utils;

import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by tarnenok on 24.02.15.
 */
public class ToolbarUtils {
    public static final int UP_DELTA_Y = 0;
    public static final int DOWN_DELTA_Y = 20;

    private static final int DURATION = 200;

    public static void show(Toolbar toolbar){
        toolbar.animate()
                .translationY(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION)
                .start();
    }

    public static void hide(Toolbar toolbar){
        toolbar.animate()
                .translationY(-toolbar.getHeight())
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION)
                .start();
    }
}
