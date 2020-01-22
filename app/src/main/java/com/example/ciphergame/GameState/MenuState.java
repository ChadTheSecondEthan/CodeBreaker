package com.example.ciphergame.GameState;

import android.view.View;

import com.example.ciphergame.R;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.Views.VolumeButton;

public class MenuState extends GameState {

    MenuState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        setContentView(R.layout.menu_state);

        ViewHelper.setMarginsAsPercentOfScreen(getView(R.id.version), 0, 0, 2, 1);
        getView(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { gsm.setState(GameStateManager.TEXTPACKSTATE); }});
        getView(R.id.creditsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { gsm.setState(GameStateManager.CREDITS); }
        });
        ((Title) gsm.getView(R.id.title)).init(R.string.menuTitle);
        ((VolumeButton) getView(R.id.volume_button)).init(gsm, ViewHelper.BOTTOM_LEFT);
    }
}
