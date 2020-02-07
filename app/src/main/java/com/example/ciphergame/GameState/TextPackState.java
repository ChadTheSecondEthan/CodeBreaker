package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.example.ciphergame.R;

public class TextPackState extends GameState {

    // TODO add the names of quote packs, both free and paid for ones

    private String[] textPacks = new String[] {
        "Ancient Philosophy Texts", "Inspirational", "American History", "Religious Scriptures",
            "", "" };

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        ((Title) app.getView(R.id.title)).init(R.string.textPackTitle);

        Button[] textPackButtons = new Button[] {
                app.getView(R.id.button1), app.getView(R.id.button2), app.getView(R.id.button3),
                app.getView(R.id.button4), app.getView(R.id.button5), app.getView(R.id.button6) };
        for (int i = 0; i < textPackButtons.length; i++) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(textPackButtons[i], 200 / 3.0, 200 / 9.0);
            textPackButtons[i].setText(textPacks[i]);
            ViewHelper.setMarginBottomAsPercentOfScreen(textPackButtons[i], (i != textPackButtons.length - 1) ? 50 / 3.0 : 25 / 3.0);
            ViewHelper.centerHorizontally(textPackButtons[i]);
            final int num = i;
            textPackButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    app.getInLevelState().setTextPack(num);
                    app.setState(MainActivity.LEVELSTATE);
                }
            });
        }
        ViewHelper.setHeightAsPercentOfScreen(getView(R.id.textPackView), 80);
    }
}
