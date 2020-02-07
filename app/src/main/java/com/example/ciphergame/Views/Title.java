package com.example.ciphergame.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class Title extends AppCompatTextView {

    public Title(Context context) { super(context); }
    public Title(Context context, AttributeSet attrs) { super(context, attrs); }
    public Title(Context context, AttributeSet attrs, int something) { super(context, attrs, something); }

    private Title init() {
        setTextColor(Color.BLACK);
        setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setTextSize(36);
        ViewHelper.setWidthAndHeightAsPercentOfScreen(this, 80, 20);
        return this;
    }

    public Title init(int title) {
        init();
        setText(title);
        return this;
    }

    public Title init(String title) {
        init();
        setText(title);
        return this;
    }
}
