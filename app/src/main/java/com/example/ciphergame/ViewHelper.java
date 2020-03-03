package com.example.ciphergame;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import androidx.constraintlayout.widget.ConstraintLayout;

/*
    This class is used to make it easier to work with views
    Author: Ethan Fisher
 */

@SuppressWarnings({"SameParameterValue", "unused", "SuspiciousNameCombination"})
public class ViewHelper {

    public static final String RED = "<font color=#FF0000>";
    private static final String FONT = "</font>";

    public static final int TOP_RIGHT = 0;
    public static final int BOTTOM_LEFT = 2;

    private static void setScales(View v, double scale) { setScales(v, scale, scale); }
    private static void setScales(@NotNull View v, double x, double y) { v.setScaleX((float) x); v.setScaleY((float) y); }

    // one percent width is used for measuring with views to make it easier to make squares
    private static double onePercentWidth;
    private static double onePercentHeight;

    public static void setMarginLeft(View v, double percent) { setMargins(v, percent, 0, 0, 0); }
    public static void setMarginTop(View v, double percent) { setMargins(v, 0, percent, 0, 0); }
    public static void setMarginRight(View v, double percent) { setMargins(v, 0, 0, percent, 0); }
    public static void setMarginBottom(View v, double percent) { setMargins(v, 0, 0, 0, percent); }
    public static void setMarginLeftAndRight(View v, double left, double right) {
        setMargins(v, left, 0, right, 0);
    }
    public static void setMarginTopAndBottom(View v, double top, double bottom) {
        setMargins(v, 0, top, 0, bottom);
    }
    public static void setMarginLeftAndRight(View v, double margin) {
        setMargins(v, margin, 0, margin, 0);
    }
    public static void setMarginTopAndBottom(View v, double margin) {
        setMargins(v, 0, margin, 0, margin);
    }
    private static void setMargins(@NotNull View v, double left, double top, double right, double bottom) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).setMargins(
                left == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).leftMargin : (int) (left * onePercentWidth),
                top == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin: (int) (top * onePercentWidth),
                right == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).rightMargin : (int) (right * onePercentWidth),
                bottom == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin : (int) (bottom * onePercentWidth));
    }
    public static void setMargins(@NotNull View v, double margin) { setMargins(v, margin, margin, margin, margin); }
    
    public static void setWidth(@NotNull View v, double percent) { v.getLayoutParams().width = (int) (percent * onePercentWidth); }
    public static void setHeight(@NotNull View v, double percent) { v.getLayoutParams().height = (int) (percent * onePercentHeight); }
    public static void setWidthAndHeight(@NotNull View v, double percent) { setWidth(v, percent); setHeight(v, percent); }
    public static void setWidthAndHeight(@NotNull View v, double width, double height) { setWidth(v, width); setHeight(v, height); }
    public static void setWidthAndSquare(@NotNull View v, double percent) {
        v.getLayoutParams().width = (int) (percent * onePercentWidth);
        v.getLayoutParams().height = v.getLayoutParams().width;
    }
    public static void setHeightAndSquare(@NotNull View v, double percent) {
        v.getLayoutParams().height = (int) (percent * onePercentHeight);
        v.getLayoutParams().width = v.getLayoutParams().height;
    }

    public static void setHorizontalBias(View v, double bias) { ((ConstraintLayout.LayoutParams) v.getLayoutParams()).horizontalBias = (float) bias; }
    public static void setVerticalBias(View v, double bias) { ((ConstraintLayout.LayoutParams) v.getLayoutParams()).verticalBias = (float) bias; }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
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

    @NotNull
    @Contract(pure = true)
    public static String coloredText(String text, String color) { return color + text + FONT; }

    public static void setGetBiggerTouchListener(View v) { setGetBiggerTouchListener(v, 1.25); }
    public static void setGetBiggerTouchListener(@NotNull View v, double scale) { v.setOnTouchListener(getBiggerTouchListener(scale)); }

    static void setDisplayMetrics(@NotNull DisplayMetrics dm) {
        onePercentHeight = dm.heightPixels / 100.0;
        onePercentWidth = dm.widthPixels / 100.0;
    }

    @Contract(pure = true)
    public static float percentWidth(double percent) { return (float) (onePercentWidth * percent); }
    @Contract(pure = true)
    public static float percentHeight(double percent) { return (float) (onePercentHeight * percent); }

}
