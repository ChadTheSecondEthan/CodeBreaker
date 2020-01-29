package com.example.ciphergame.GameState;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.ImageNumberView;
import com.example.ciphergame.Views.ViewHelper;

public class CurrencyState extends GameState {

    // TODO add video ad

    public CurrencyState(MainActivity app) {
        super(app);
    }

    public void init() {
        setContentView(R.layout.currency_state);

        ((BackButton) getView(R.id.back_button)).init(app);

        ViewHelper.setMarginTopAsPercentOfScreen(getView(R.id.currency_button1), 12);
        for (Button button : new Button[] {
                getView(R.id.currency_button1), getView(R.id.currency_button2),
                getView(R.id.currency_button3), getView(R.id.currency_button4),
                getView(R.id.currency_button5), getView(R.id.currency_button6) }) {
            ViewHelper.setMarginBottomAsPercentOfScreen(button, 12);
            ViewHelper.setWidthAndHeightAsPercentOfScreen(button, 60, 25);
        }
        ViewHelper.setHeightAsPercentOfScreen(getView(R.id.currencyScrollView), 100);
    }

    public void startNoMoneyAnimation() {
        TextView textView = getView(R.id.no_money);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(ViewHelper.fadeAnimation(textView));
    }
}
