package com.example.ciphergame.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;

import androidx.appcompat.widget.AppCompatButton;

public class VolumeButton extends AppCompatButton {

    public VolumeButton(Context context) { super(context); }
    public VolumeButton(Context context, AttributeSet attrs) { super(context, attrs); }
    public VolumeButton(Context context, AttributeSet attrs, int something) { super(context, attrs, something); }

    public void init(final MainActivity app, int area) {
        ViewHelper.setGetBiggerTouchListener(this);
        ViewHelper.setWidthAsPercentOfScreen(this, 80 / 9.0);
        ViewHelper.makeSquareWithWidth(this);
        setBackgroundResource((app.isVolumeOn()) ? R.drawable.volume_on_image : R.drawable.volume_off_image);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getMediaPlayer() != null) {
                    if (app.isVolumeOn())
                        app.getMediaPlayer().pause();
                    else
                        app.getMediaPlayer().start();
                }
                setBackgroundResource((app.isVolumeOn()) ? R.drawable.volume_off_image : R.drawable.volume_on_image);
                app.setVolumeOn(!app.isVolumeOn());
            }
        });
        if (area == ViewHelper.BOTTOM_LEFT)
            ViewHelper.setMarginsAsPercentOfScreen(this, 1, 0 , 0, 1);
        else if (area == ViewHelper.TOP_LEFT)
            ViewHelper.setMarginsAsPercentOfScreen(this, 0, 1, 5, 0);
    }
}
