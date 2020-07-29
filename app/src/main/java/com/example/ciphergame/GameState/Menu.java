package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;

public class Menu extends GameState {

    public Menu(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.menu_state);

        getView(R.id.logo).setVisibility(View.INVISIBLE);

        getView(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setState(MainActivity.TEXTPACKSTATE);
                buttonClick();
            }
        });
        getView(R.id.buyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setState(MainActivity.PURCHASE);
                buttonClick();
            }
        });

        getView(R.id.instructions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView about = getView(R.id.instructionsText);
                about.setAlpha(about.getAlpha() == 1 ? 0 : 1);
                buttonClick();
            }
        });
    }
}
