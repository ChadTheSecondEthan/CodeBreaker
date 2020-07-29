package com.example.ciphergame.GameState;

import android.graphics.drawable.Drawable;
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

        // strings for each of the textpacks
        String[] textPacks = MainActivity.TEXT_PACKS;

        // colors for text and background of buttons
        Drawable background = app.getResources().getDrawable(R.drawable.basic_rectangle, null);
        int textColor = app.getResources().getColor(R.color.nearWhite, null);

        buttons = new Button[textPacks.length];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(app);
            buttons[i].setId(View.generateViewId());
            buttons[i].setBackground(background);
            buttons[i].setTextColor(textColor);
            buttons[i].setText(textPacks[i]);
            buttons[i].setOnClickListener(this);

            // the top button needs a top margin, too
            if (i == 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(70), (int) ViewHelper.percentWidth(30));
                lp.topMargin = (int) ViewHelper.percentWidth(10);
                lp.bottomMargin = (int) ViewHelper.percentWidth(8);
                buttons[i].setLayoutParams(lp);
            } else if (i == buttons.length - 1) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(70), (int) ViewHelper.percentWidth(30));
                lp.bottomMargin = (int) ViewHelper.percentWidth(10);
                buttons[i].setLayoutParams(lp);
            } else
                buttons[i].setLayoutParams(params);
            layout.addView(buttons[i]);
        }

        // TODO remove once there are more text packs
        buttons[2].setClickable(false);

        addHomeButton(MainActivity.MENU);
        addVolumeButton();
    }

    @Override
    public void onClick(View view) {
        buttonClick();
        int id = view.getId();
        for (int i = 0; i < buttons.length; i++)
            if (id == buttons[i].getId()) {
                app.getDataEditor().putInt(GameState.TEXT_PACK, i).apply();
                app.setState(MainActivity.LEVELSTATE);
            }
    }
}
