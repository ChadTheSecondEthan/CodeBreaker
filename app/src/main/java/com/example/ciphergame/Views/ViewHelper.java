package com.example.ciphergame.Views;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

public class ViewHelper {

    public static final int TOP_RIGHT = 0;
    public static final int BOTTOM_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int TOP_LEFT = 3;

    private static DisplayMetrics displayMetrics;
    private static double onePercentWidth;
    private static double onePercentHeight;

    public static void setPaddingLeftAsPercentOfScreen(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, paddingPercent, 0, 0, 0); }
    public static void setPaddingTopAsPercentOfScreen(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, paddingPercent, 0, 0); }
    public static void setPaddingRightAsPercentOfScreen(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, 0, paddingPercent, 0); }
    public static void setPaddingBottomAsPercentOfScreen(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, 0, 0, paddingPercent); }
    public static void setPaddingLeftAndRightAsPercentOfScreen(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, paddingPercent, 0, paddingPercent, 0); }
    public static void setPaddingTopAndBottomAsPercentOfScreen(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, paddingPercent, 0, paddingPercent); }
//    private static void setPaddingAsPercentOfScreen(@NotNull View v, double leftPercent, double topPercent, double rightPercent, double bottomPercent) {
//        int left = (leftPercent != 0) ? (int) (onePercentWidth * leftPercent) : v.getPaddingLeft();
//        int top = (leftPercent != 0) ? (int) (onePercentWidth * topPercent) : v.getPaddingTop();
//        int right = (leftPercent != 0) ? (int) (onePercentWidth * rightPercent) : v.getPaddingRight();
//        int bottom = (leftPercent != 0) ? (int) (onePercentWidth * bottomPercent) : v.getPaddingBottom();
//        v.setPadding(left, top, right, bottom);
//    }
    private static void setPaddingAsPercentOfScreen(@NotNull View v, double leftPercent, double topPercent, double rightPercent, double bottomPercent) {
        int left = (int) (onePercentWidth * leftPercent);
        int top = (int) (onePercentWidth * topPercent);
        int right = (int) (onePercentWidth * rightPercent);
        int bottom = (int) (onePercentWidth * bottomPercent);
        v.setPadding(left, top, right, bottom);
    }

    public static void setMarginLeftAsPercentOfScreen(View v, double marginPercent) { setMarginsAsPercentOfScreen(v, marginPercent, 0, 0, 0); }
    public static void setMarginTopAsPercentOfScreen(View v, double marginPercent) { setMarginsAsPercentOfScreen(v, 0, marginPercent, 0, 0); }
    public static void setMarginRightAsPercentOfScreen(View v, double marginPercent) { setMarginsAsPercentOfScreen(v, 0, 0, marginPercent, 0); }
    public static void setMarginBottomAsPercentOfScreen(View v, double marginPercent) { setMarginsAsPercentOfScreen(v, 0, 0, 0, marginPercent); }
    public static void setMarginTopAndBottomAsPercentOfScreen(View v, double marginTop, double marginBottom) { setMarginsAsPercentOfScreen(v, 0, marginTop, 0, marginBottom); }
    public static void setMarginTopAndBottomAsPercentOfScreen(View v, double marginPercent) { setMarginsAsPercentOfScreen(v, 0, marginPercent, 0, marginPercent); }
    public static void setMarginLeftAndRightAsPercentOfScreen(View v, double marginPercent) { setMarginsAsPercentOfScreen(v, marginPercent, 0, marginPercent, 0); }
    public static void setMarginsAsPercentOfScreen(@NotNull View v, double leftPercent, double topPercent, double rightPercent, double bottomPercent) {
        int left = (int) (onePercentWidth * leftPercent);
        int top = (int) (onePercentWidth * topPercent);
        int right = (int) (onePercentWidth * rightPercent);
        int bottom = (int) (onePercentWidth * bottomPercent);
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public static void centerHorizontally(@NotNull View v) { setHorizontalBias(v, 0.5); }
    public static void centerVertically(@NotNull View v) { setVerticalBias(v, 0.5); }
    public static void setHorizontalBias(@NotNull View v, double bias) {
        v.setX((float) ((displayMetrics.widthPixels - v.getLayoutParams().width) * bias));
    }
    public static void setVerticalBias(@NotNull View v, double bias) {
        v.setY((float) ((displayMetrics.heightPixels - v.getLayoutParams().height) * bias));
    }

    public static void setWidthAsPercentOfScreen(@NotNull View v, double percentWidth) {
        v.getLayoutParams().width = (int) (percentWidth * onePercentWidth);
    }
    public static void setHeightAsPercentOfScreen(@NotNull View v, double percentHeight) {
        v.getLayoutParams().height = (int) (percentHeight * onePercentHeight);
    }
    public static void setWidthAndHeightAsPercentOfScreen(@NotNull View v, double percentWidth, double percentHeight) {
        v.getLayoutParams().width = (int) (percentWidth * onePercentWidth);
        v.getLayoutParams().height = (int) (percentHeight * onePercentHeight);
    }
    public static void setWidthAndHeightAsPercentOfScreen(@NotNull View v, double percent) {
        setWidthAndHeightAsPercentOfScreen(v, percent, percent);
    }

    public static void makeSquareWithWidth(View v) { setHeight(v, v.getLayoutParams().width); }
    public static void makeSquareWithHeight(View v) { setWidth(v, v.getLayoutParams().height); }

    private static void setWidth(@NotNull View v, int width) { v.getLayoutParams().width = width; }
    private static void setHeight(@NotNull View v, int height) { v.getLayoutParams().height = height; }

    public static void setScales(View v, double x, double y) { v.setScaleX((float) x); v.setScaleY((float) y);}
    public static void resetScales(View v) { v.setScaleX(1); v.setScaleY(1); }

    public static void moveUpAsPercentOfScreen(@NotNull View v, double percent) { v.setY(v.getY() - (float) (onePercentHeight * percent)); }

    @SuppressLint("ClickableViewAccessibility")
    private static View.OnTouchListener getBiggerTouchListener(double scale) {
        final double scales = scale;
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) setScales(view, scales, scales);
                else if (event.getAction() == MotionEvent.ACTION_UP) resetScales(view);
                return false;
            }
        };
    }

    public static void setGetBiggerTouchListener(View v) {
        setGetBiggerTouchListener(v, 1.25);
    }
    public static void setGetBiggerTouchListener(View v, double scale) {
        v.setOnTouchListener(getBiggerTouchListener(scale));
    }

    public static void setDisplayMetrics(DisplayMetrics dm) {
        displayMetrics = dm;
        onePercentWidth = displayMetrics.widthPixels / 100.0;
        onePercentHeight = displayMetrics.heightPixels / 100.0;
    }

    public static int getWidth(View v) { return v.getLayoutParams().width; }
    public static int getHeight(View v) { return v.getLayoutParams().height; }

    public static float percentHeight(double percent) { return (float) (onePercentWidth * percent); }
    public static float percentWidth(double percent) { return (float) (onePercentWidth * percent); }
}
