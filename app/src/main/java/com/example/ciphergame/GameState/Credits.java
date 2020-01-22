package com.example.ciphergame.GameState;

import com.example.ciphergame.R;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.Views.VolumeButton;

public class Credits extends GameState {

    Credits(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        setContentView(R.layout.credits);

        ((VolumeButton) getView(R.id.volume_button)).init(gsm, ViewHelper.BOTTOM_LEFT);
        ((BackButton) getView(R.id.back_button)).init(gsm);
        ((Title) getView(R.id.title)).init(R.string.creditsTitle);
    }
}
