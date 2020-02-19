package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

public class TextPackState extends GameState {

    // TODO add the names of quote packs, both free and paid for ones

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        Button[] textPackButtons = app.getButtons(new int[] { R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6 });
        ViewHelper.setMarginTop(textPackButtons[0], 8);
        String[] textPacks = MainActivity.TEXT_PACKS;
        for (int i = 0; i < textPackButtons.length; i++) {
            textPackButtons[i].setText(textPacks[i]);
            ViewHelper.setWidthAndHeight(textPackButtons[i], 70, 30);
            ViewHelper.setMarginBottom(textPackButtons[i], 8);
            final int num = i;
            textPackButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { app.setState(MainActivity.LEVELSTATE).getInLevelState().setTextPack(num); }
            });
        }
    }
}
