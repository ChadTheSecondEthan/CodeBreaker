package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

public class TextPackState extends GameState implements View.OnClickListener {

    // TODO add the names of quote packs, both free and paid for ones
    
    private Button[] buttons;

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        buttons = app.getButtons(new int[] { R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6 });
        String[] textPacks = MainActivity.TEXT_PACKS;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(textPacks[i]);
            ViewHelper.setWidthAndHeight(buttons[i], 70, 30);
            ViewHelper.setMarginBottom(buttons[i], 8);
            buttons[i].setOnClickListener(this);
        }

        addHomeButton();
        addVolumeButton();
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < buttons.length; i++)
            if (view.getId() == buttons[i].getId()) {
                app.getDataEditor().putInt("textPack", i).apply();
                app.setState(MainActivity.LEVELSTATE);
            }
    }
}
