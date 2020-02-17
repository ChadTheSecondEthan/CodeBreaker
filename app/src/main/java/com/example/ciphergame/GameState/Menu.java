package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.Views.VolumeButton;

public class Menu extends GameState {

    public Menu(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.menu_state);

        getView(R.id.logo).setVisibility(View.INVISIBLE);
        getView(R.id.version).setVisibility(View.VISIBLE);
        ViewHelper.setMargins(getView(R.id.version), 0, 0, 2, 1);

        ((VolumeButton) getView(R.id.volume_button)).init(app, ViewHelper.BOTTOM_LEFT);

        Button play = getView(R.id.playButton);
        ViewHelper.setWidthAsPercentOfScreen(play, 20);
        ViewHelper.makeSquareWithWidth(play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.TEXTPACKSTATE); }
        });
        Button buy = getView(R.id.buyButton);
        ViewHelper.setWidthAsPercentOfScreen(buy, 20);
        ViewHelper.makeSquareWithWidth(buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
        });

        Title title = getView(R.id.title);
        title.init("Codebreaker");
        title.setTextSize(20);
        ViewHelper.setWidthAsPercentOfScreen(title, 100);
    }
}
