package com.example.ciphergame.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.ciphergame.GameState.GameStateManager;
import com.example.ciphergame.R;

import androidx.appcompat.widget.AppCompatButton;

public class VolumeButton extends AppCompatButton {

    public VolumeButton(Context context) { super(context); }
    public VolumeButton(Context context, AttributeSet attrs) { super(context, attrs); }
    public VolumeButton(Context context, AttributeSet attrs, int something) { super(context, attrs, something); }

    public void init(final GameStateManager gsm, int area) {
        ViewHelper.setGetBiggerTouchListener(this);
        ViewHelper.setWidthAsPercentOfScreen(this, 80 / 9.0);
        ViewHelper.makeSquareWithWidth(this);
        setBackgroundResource((gsm.isVolumeOn()) ? R.drawable.volume_on_image : R.drawable.volume_off_image);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gsm.getMediaPlayer() != null) {
                    if (gsm.isVolumeOn())
                        gsm.getMediaPlayer().pause();
                    else
                        gsm.getMediaPlayer().start();
                }
                setBackgroundResource((gsm.isVolumeOn()) ? R.drawable.volume_off_image : R.drawable.volume_on_image);
                gsm.setVolumeOn(!gsm.isVolumeOn());
            }
        });
        if (area == ViewHelper.BOTTOM_LEFT)
            ViewHelper.setMarginsAsPercentOfScreen(this, 1, 0 , 0, 1);
        else if (area == ViewHelper.TOP_LEFT)
            ViewHelper.setMarginsAsPercentOfScreen(this, 0, 1, 1, 0);
    }
}
