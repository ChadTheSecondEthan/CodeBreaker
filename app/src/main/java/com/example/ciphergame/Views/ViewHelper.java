package com.example.ciphergame.Views;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"SuspiciousNameCombination", "SameParameterValue", "unused"})
public class ViewHelper {

    public static final String RED = "<font color=#FF0000>";
    private static final String FONT = "</font>";

    public static final int TOP_RIGHT = 0;
    public static final int BOTTOM_LEFT = 2;

    private static DisplayMetrics displayMetrics;
    private static double onePercentWidth;
    private static double onePercentHeight;

    public static void setPaddingLeft(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, paddingPercent, 0, 0, 0); }
    public static void setPaddingTop(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, paddingPercent, 0, 0); }
    public static void setPaddingRight(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, 0, paddingPercent, 0); }
    public static void setPaddingBottom(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, 0, 0, paddingPercent); }
    public static void setPaddingLeftAndRight(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, paddingPercent, 0, paddingPercent, 0); }
    public static void setPaddingTopAndBottom(View v, double paddingPercent) { setPaddingAsPercentOfScreen(v, 0, paddingPercent, 0, paddingPercent); }

    private static void setPaddingAsPercentOfScreen(@NotNull View v, double leftPercent, double topPercent, double rightPercent, double bottomPercent) {
        int left = (int) (onePercentWidth * leftPercent);
        int top = (int) (onePercentWidth * topPercent);
        int right = (int) (onePercentWidth * rightPercent);
        int bottom = (int) (onePercentWidth * bottomPercent);
        v.setPadding(left, top, right, bottom);
    }

    public static void setMarginLeft(View v, double marginPercent) { setMargins(v, marginPercent, 0, 0, 0); }
    public static void setMarginTop(View v, double marginPercent) { setMargins(v, 0, marginPercent, 0, 0); }
    public static void setMarginRight(View v, double marginPercent) { setMargins(v, 0, 0, marginPercent, 0); }
    public static void setMarginBottom(View v, double marginPercent) { setMargins(v, 0, 0, 0, marginPercent); }
    public static void setMarginTopAndBottom(View v, double marginTop, double marginBottom) { setMargins(v, 0, marginTop, 0, marginBottom); }
    public static void setMarginTopAndBottom(View v, double marginPercent) { setMargins(v, 0, marginPercent, 0, marginPercent); }
    public static void setMarginLeftAndRight(View v, double marginPercent) { setMargins(v, marginPercent, 0, marginPercent, 0); }
    public static void setMargins(@NotNull View v, double leftPercent, double topPercent, double rightPercent, double bottomPercent) {
        int left = leftPercent == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).leftMargin : (int) (onePercentWidth * leftPercent);
        int top = topPercent == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin : (int) (onePercentHeight * topPercent);
        int right = rightPercent == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).rightMargin : (int) (onePercentWidth * rightPercent);
        int bottom = bottomPercent == 0 ? ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin : (int) (onePercentHeight * bottomPercent);
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public static void matchAttributes(@NotNull View start, @NotNull View toMatch) {
        start.setX(toMatch.getX());
        start.setY(toMatch.getY());
        matchMargins(start, toMatch);
        start.setPadding(toMatch.getPaddingLeft(), toMatch.getPaddingTop(), toMatch.getPaddingRight(), toMatch.getPaddingLeft());
    }

    public static void matchMargins(@NotNull View start, @NotNull View toMatch) {
        int left =  ((ViewGroup.MarginLayoutParams) toMatch.getLayoutParams()).leftMargin;
        int top = ((ViewGroup.MarginLayoutParams) toMatch.getLayoutParams()).topMargin;
        int right = ((ViewGroup.MarginLayoutParams) toMatch.getLayoutParams()).rightMargin;
        int bottom = ((ViewGroup.MarginLayoutParams) toMatch.getLayoutParams()).bottomMargin;
        ((ViewGroup.MarginLayoutParams) start.getLayoutParams()).setMargins(left, top, right, bottom);
    }

    public static void matchWidthAndHeight(View start, View toMatch) {
        // TODO
    }

    public static void centerHorizontally(@NotNull View v) { setHorizontalBias(v, 0.5); }
    public static void center(@NotNull View v) { setHorizontalBias(v, 0.5); setVerticalBias(v, 0.5);}
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

    public static void setScales(View v, double scale) { setScales(v, scale, scale); }
    private static void setScales(View v, double x, double y) { v.setScaleX((float) x); v.setScaleY((float) y);}
    private static void resetScales(View v) { v.setScaleX(1); v.setScaleY(1); }

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

    public static AlphaAnimation fadeOutAnimation(View v) { return fadeOutAnimation(v, 1500); }
    public static AlphaAnimation fadeOutAnimation(final View v, long time) {
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
    public static AlphaAnimation fadeInAnimation(long time) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(time);
        return animation;
    }

    public static String coloredText(String text, String color) {
        return color + text + FONT;
    }

    public static void setGetBiggerTouchListener(View v) { setGetBiggerTouchListener(v, 1.25); }
    public static void setGetBiggerTouchListener(View v, double scale) { v.setOnTouchListener(getBiggerTouchListener(scale)); }

    public static void setDisplayMetrics(DisplayMetrics dm) {
        displayMetrics = dm;
        onePercentWidth = displayMetrics.widthPixels / 100.0;
        onePercentHeight = displayMetrics.heightPixels / 100.0;
    }

    public static float percentHeight(double percent) { return (float) (onePercentWidth * percent); }
    public static float percentWidth(double percent) { return (float) (onePercentWidth * percent); }
}
