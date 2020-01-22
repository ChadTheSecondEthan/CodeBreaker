package com.example.ciphergame.Views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.example.ciphergame.GameState.GameStateManager;
import com.example.ciphergame.R;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.widget.AppCompatButton;

public class BackButton extends AppCompatButton {

    public BackButton(Context context) { super(context); }
    public BackButton(Context context, AttributeSet attrs) { super(context, attrs); }
    public BackButton(Context context, AttributeSet attrs, int something) { super(context, attrs, something); }

    public void init(@NotNull final GameStateManager gsm) {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsm.setState(gsm.getPrevState());
//                (MediaPlayer.create()) TODO add button click noises
            }
        });
        ViewHelper.setGetBiggerTouchListener(this);
        setBackgroundColor(Color.TRANSPARENT);
        ViewHelper.setMarginsAsPercentOfScreen(this, 1, 1, 0, 0);
        setBackgroundResource(R.drawable.back_button);
        ViewHelper.setWidthAsPercentOfScreen(this, 10);
        ViewHelper.makeSquareWithWidth(this);
    }
}
