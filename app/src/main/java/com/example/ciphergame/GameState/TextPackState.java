package com.example.ciphergame.GameState;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.VolumeButton;

public class TextPackState extends GameState {

    // TODO add the names of quote packs, both free and paid for ones

    private String[] textPacks = new String[] {
        "Ancient Philosophy Texts", "Inspirational", "American History", "",
            "", "" };

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        ViewHelper.setMarginsAsPercentOfScreen(getView(R.id.version), 0, 0, 2, 1);

        Title title = getView(R.id.title);
        title.init(R.string.textPackTitle);
        title.setTextSize(32);
        title.setWidth((int) ViewHelper.percentWidth(85));

        VolumeButton volumeButton = getView(R.id.volume_button);
        volumeButton.init(app, ViewHelper.BOTTOM_LEFT);

        Button[] textPackButtons = app.getButtons(new int[] { R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6 });
        for (int i = 0; i < textPackButtons.length; i++) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(textPackButtons[i], 200 / 3.0, 200 / 9.0);
            textPackButtons[i].setText(textPacks[i]);
            ViewHelper.setMarginBottomAsPercentOfScreen(textPackButtons[i], (i != textPackButtons.length - 1) ? 50 / 3.0 : 25 / 3.0);
            ViewHelper.centerHorizontally(textPackButtons[i]);
            final int num = i;
            textPackButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { app.setState(MainActivity.LEVELSTATE).getInLevelState().setTextPack(num); }
            });
        }
        ViewHelper.setMarginTopAndBottomAsPercentOfScreen(getView(R.id.button1), 25 / 3.0, 50 / 3.0);
        ViewHelper.setHeightAsPercentOfScreen(getView(R.id.textPackView), 77.5);

        final Button creditsButton = getView(R.id.creditsButton);
        ViewHelper.setWidthAndHeightAsPercentOfScreen(creditsButton, 22.5, 7);
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.CREDITS); }
        });
        ViewHelper.setGetBiggerTouchListener(creditsButton);
    }
}
