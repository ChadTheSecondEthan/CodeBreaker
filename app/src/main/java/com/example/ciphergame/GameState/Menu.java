package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;

public class Menu extends GameState {

    public Menu(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.menu_state);

        getView(R.id.logo).setVisibility(View.INVISIBLE);
        getView(R.id.background).setBackgroundResource(R.drawable.menu_screen);

        Button play = getView(R.id.playButton);
        play.setAlpha(0.0f);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.TEXTPACKSTATE); }
        });
        Button buy = getView(R.id.buyButton);
        buy.setAlpha(0.0f);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
        });
    }
}
