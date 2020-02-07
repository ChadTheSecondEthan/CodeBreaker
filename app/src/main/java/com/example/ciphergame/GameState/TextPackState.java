package com.example.ciphergame.GameState;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.Views.VolumeButton;
import com.example.ciphergame.R;

public class TextPackState extends GameState {

    // TODO add the names of quote packs, both free and paid for ones

    private String[] textPacks = new String[] {"Pack 1", "Pack 2", "Pack 3", "Pack 4", "Pack 5", "Pack 6" };

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);
        getView(R.id.logo).setVisibility(View.INVISIBLE);
        getView(R.id.version).setVisibility(View.VISIBLE);

        ViewHelper.setMarginsAsPercentOfScreen(getView(R.id.version), 0, 0, 2, 1);

        Title title = getView(R.id.title);
        title.init(R.string.textPackTitle).setTextSize(20);
        ViewHelper.setWidthAsPercentOfScreen(title, 100);

        SpannableString string = new SpannableString(Html.fromHtml("The Codebuster<br>Select Text Pack", 0));
        string.setSpan(new RelativeSizeSpan(1.6f), 0, 14, 0);
        title.setText(string);

        ((VolumeButton) getView(R.id.volume_button)).init(app, ViewHelper.BOTTOM_LEFT);

        double marginTop = 6.25;
        double marginBottom = marginTop * 266 / 400.0;
        Button[] textPackButtons = app.getButtons(new int[] { R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6 });
        for (int i = 0; i < textPackButtons.length; i++) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(textPackButtons[i], 200 / 3.0, 200 / 9.0);
            textPackButtons[i].setText(textPacks[i]);
            ViewHelper.setMarginBottomAsPercentOfScreen(textPackButtons[i], (i != textPackButtons.length - 1) ? marginBottom : marginTop);
            ViewHelper.centerHorizontally(textPackButtons[i]);
            final int num = i;
            textPackButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { app.setState(MainActivity.LEVELSTATE).getInLevelState().setTextPack(num); }
            });
        }
        ViewHelper.setMarginTopAndBottomAsPercentOfScreen(getView(R.id.button1), marginTop, marginBottom);
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
