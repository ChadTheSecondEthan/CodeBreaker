package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.R;

public class TextPackState extends GameState {

    // TODO add the names of quote packs, both free and paid for ones

    private String[] textPacks = new String[] {
        "Ancient Philosophy Texts", "Inspirational", "American History", "Religious Scriptures",
            "", "" };

    TextPackState(GameStateManager gsm) { super(gsm); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        ((BackButton) getView(R.id.back_button)).init(gsm);
        ((Title) gsm.getView(R.id.title)).init(R.string.textPackTitle);

        Button[] textPackButtons = new Button[] {
                gsm.getView(R.id.button1), gsm.getView(R.id.button2), gsm.getView(R.id.button3),
                gsm.getView(R.id.button4), gsm.getView(R.id.button5), gsm.getView(R.id.button6) };
        for (int i = 0; i < textPackButtons.length; i++) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(textPackButtons[i], 200 / 3.0, 200 / 9.0);
            textPackButtons[i].setText(textPacks[i]);
            ViewHelper.setMarginBottomAsPercentOfScreen(textPackButtons[i], (i != textPackButtons.length - 1) ? 50 / 3.0 : 25 / 3.0);
            ViewHelper.centerHorizontally(textPackButtons[i]);
            final int num = i;
            textPackButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gsm.getInLevelState().setTextPack(num);
                    gsm.setState(GameStateManager.LEVELSTATE);
                }
            });
        }
        ViewHelper.setHeightAsPercentOfScreen(getView(R.id.textPackView), 80);
    }
}
