package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;
import com.example.ciphergame.VolumeButton;

public class Menu extends GameState {

    public Menu(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.menu_state);

        getView(R.id.logo).setVisibility(View.INVISIBLE);
        getView(R.id.background).setBackgroundResource(R.drawable.menu_screen);

        ViewHelper.setMargins(getView(R.id.version), 0, 0, 2, 1);

        ((VolumeButton) getView(R.id.volume_button)).init(app, ViewHelper.BOTTOM_LEFT);

        Button play = getView(R.id.playButton);
        play.setAlpha(0.0f);
        ViewHelper.setWidthAndHeightAsPercentOfScreen(play, 24, 8);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.TEXTPACKSTATE); }
        });
        Button buy = getView(R.id.buyButton);
        buy.setAlpha(0.0f);
        ViewHelper.setWidthAndHeightAsPercentOfScreen(buy, 24, 8);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
        });
    }
}
