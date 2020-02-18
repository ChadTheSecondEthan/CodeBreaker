package com.example.ciphergame;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/*
    This class is used to make it easier to work with views
    Author: Ethan Fisher
 */

@SuppressWarnings({"SameParameterValue", "unused"})
public class ViewHelper {

    public static final String RED = "<font color=#FF0000>";
    private static final String FONT = "</font>";

    public static final int TOP_RIGHT = 0;
    public static final int BOTTOM_LEFT = 2;

    private static void setScales(View v, double scale) { setScales(v, scale, scale); }
    private static void setScales(View v, double x, double y) { v.setScaleX((float) x); v.setScaleY((float) y); }

    private static double onePercentWidth;
    private static double onePercentHeight;

    @SuppressLint("ClickableViewAccessibility")
    private static View.OnTouchListener getBiggerTouchListener(final double scale) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) setScales(view, scale, scale);
                else if (event.getAction() == MotionEvent.ACTION_UP) setScales(view, 1);
                return false;
            }
        };
    }

    public static AlphaAnimation fadeOutAnimation(View v) { return fadeOutAnimation(v, 1500); }
    static AlphaAnimation fadeOutAnimation(final View v, long time) {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(time);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) { v.setVisibility(View.INVISIBLE); }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        return animation;
    }
    static AlphaAnimation fadeInAnimation(long time) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(time);
        return animation;
    }

    public static String coloredText(String text, String color) { return color + text + FONT; }

    public static void setGetBiggerTouchListener(View v) { setGetBiggerTouchListener(v, 1.25); }
    public static void setGetBiggerTouchListener(View v, double scale) { v.setOnTouchListener(getBiggerTouchListener(scale)); }

    public static void setDisplayMetrics(DisplayMetrics dm) {
        onePercentHeight = dm.heightPixels / 100.0;
        onePercentWidth = dm.widthPixels / 100.0;
    }

    public static double percentWidth(double percent) { return onePercentWidth * percent; }
    public static double percentHeight(double percent) { return onePercentHeight * percent; }

}
