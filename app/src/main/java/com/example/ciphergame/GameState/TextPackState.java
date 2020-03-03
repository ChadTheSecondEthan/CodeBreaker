package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

public class TextPackState extends GameState implements View.OnClickListener {

    // TODO add the names of quote packs, both free and paid for ones
    
    private Button[] buttons;

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        LinearLayout layout = getView(R.id.textpackLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(70), (int) ViewHelper.percentWidth(30));
        params.bottomMargin = (int) ViewHelper.percentWidth(8);

        buttons = new Button[6];
        String[] textPacks = MainActivity.TEXT_PACKS;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(app);
            buttons[i].setText(textPacks[i]);
            buttons[i].setOnClickListener(this);
            layout.addView(buttons[i], params);
        }

        addHomeButton();
        addVolumeButton();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        for (int i = 0; i < buttons.length; i++)
            if (id == buttons[i].getId()) {
                app.getDataEditor().putInt("textPack", i).apply();
                app.setState(MainActivity.LEVELSTATE);
            }
    }
}
