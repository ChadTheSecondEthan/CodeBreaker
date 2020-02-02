package com.example.ciphergame.GameState;

import android.text.Html;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.Views.VolumeButton;

public class Credits extends GameState {

    public Credits(MainActivity app) {
        super(app);
    }

    public void init() {
        setContentView(R.layout.credits);

        ((VolumeButton) getView(R.id.volume_button)).init(app, ViewHelper.BOTTOM_LEFT);
        ((BackButton) getView(R.id.back_button)).init(app);
        ((Title) getView(R.id.title)).init(R.string.creditsTitle);

        String creditsText = "Lead Programmer: Ethan Fisher<br><br>Music:<br>Ethan Fisher<br><br>Graphics:<br>David Fisher";
        ((TextView) getView(R.id.creditsText)).setText(Html.fromHtml(ViewHelper.coloredText(creditsText, ViewHelper.RED), 1));
    }
}
