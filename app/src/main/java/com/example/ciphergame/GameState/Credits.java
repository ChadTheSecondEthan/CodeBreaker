package com.example.ciphergame.GameState;

import android.text.Html;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.BackButton;

public class Credits extends GameState {

    public Credits(MainActivity app) {
        super(app);
    }


    public void init() {
        setContentView(R.layout.credits);

        // TODO Put everything at the bottom and game logo at top also change name
        String creditsText = "Music: TODO find music";
        ((TextView) getView(R.id.creditsText)).setText(Html.fromHtml(creditsText, 1));
        ((BackButton) getView(R.id.back_button)).init(app);
    }
}
