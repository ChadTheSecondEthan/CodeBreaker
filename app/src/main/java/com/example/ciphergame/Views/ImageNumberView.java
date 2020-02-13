package com.example.ciphergame.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.ciphergame.MainActivity;

import java.util.Objects;

@SuppressWarnings("SameParameterValue")
public class ImageNumberView extends View {

    private String variable;
    private int image;
    private Paint paint;

    public ImageNumberView(Context context) { super(context); }
    public ImageNumberView(Context context, AttributeSet attrs) { super(context, attrs); }
    public ImageNumberView(Context context, AttributeSet attrs, int something) { super(context, attrs, something); }

    public void init(int image, String numberVariable) {
        this.image = image;
        variable = numberVariable;
        setPaintColor(Color.BLACK);
    }

    private void setPaintColor(int color) {
        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.DEFAULT);
    }

    private int getNumber() {
        if (variable.equals("coin"))
            return MainActivity.getCurrencies().getCoins();
        else return 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("" + getNumber(), ViewHelper.percentWidth(50), ViewHelper.percentHeight(9.5), paint);
        Drawable drawable = getContext().getDrawable(image);
        Objects.requireNonNull(drawable).setBounds((int) ViewHelper.percentWidth(38), (int) ViewHelper.percentHeight(2), (int) ViewHelper.percentWidth(48), (int) ViewHelper.percentHeight(12));
        drawable.draw(canvas);
    }
}
