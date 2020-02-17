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

    private final String[] textPacks = new String[] {"Pack 1", "Pack 2", "Pack 3", "Pack 4", "Pack 5", "Pack 6" };

    public TextPackState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.text_pack_state);

        Title title = getView(R.id.title);
        title.init(R.string.textPackTitle);
        title.setTextSize(32);
        ViewHelper.setWidthAsPercentOfScreen(title, 100);
        String text = "Select Text Pack";
        title.setText(text);

        ((BackButton) getView(R.id.back_button)).init(app);

        double marginTop = 6.25;
        double marginBottom = marginTop * 266 / 400.0;
        Button[] textPackButtons = app.getButtons(new int[] { R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6 });
        for (int i = 0; i < textPackButtons.length; i++) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(textPackButtons[i], 200 / 3.0, 200 / 9.0);
            textPackButtons[i].setText(textPacks[i]);
            ViewHelper.setMarginBottom(textPackButtons[i], (i != textPackButtons.length - 1) ? marginBottom : marginTop);
            ViewHelper.centerHorizontally(textPackButtons[i]);
            final int num = i;
            textPackButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { app.setState(MainActivity.LEVELSTATE).getInLevelState().setTextPack(num); }
            });
        }
        ViewHelper.setMarginTopAndBottom(getView(R.id.button1), marginTop, marginBottom);
        ViewHelper.setHeightAsPercentOfScreen(getView(R.id.textPackView), 77.5);
    }
}
