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

        getView(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.TEXTPACKSTATE); }
        });
        getView(R.id.buyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
        });
    }
}
