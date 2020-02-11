package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.ViewHelper;

public class CurrencyState extends GameState {

    // TODO add video ad

    public CurrencyState(MainActivity app) {
        super(app);
    }

    public void init() {
        setContentView(R.layout.currency_state);

        ((BackButton) getView(R.id.back_button)).init(app);

        Button[] buttons = app.getButtons(new int [] {
                R.id.currency_button1, R.id.currency_button2, R.id.currency_button3,
                R.id.currency_button4, R.id.currency_button5, R.id.currency_button6 });
        for (int i = 0; i < buttons.length; i++) {
            ViewHelper.setMarginBottomAsPercentOfScreen(buttons[i], 12);
            ViewHelper.setWidthAndHeightAsPercentOfScreen(buttons[i], 60, 20);
            ViewHelper.centerHorizontally(buttons[i]);
            buttons[i].setText("Thingy " + (i + 1));
        }
        ViewHelper.setMarginTopAndBottomAsPercentOfScreen(getView(R.id.currency_button1), 6, 12);
        ViewHelper.setMarginBottomAsPercentOfScreen(getView(R.id.currency_button6), 6);
    }

    public void startNoMoneyAnimation() {
        final TextView textView = getView(R.id.no_money);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(ViewHelper.fadeOutAnimation(textView));
    }
}
