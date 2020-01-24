package com.example.ciphergame.GameState;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciphergame.Hints;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.ImageNumberView;
import com.example.ciphergame.Views.ViewHelper;

public class HintState extends GameState {

    public HintState(MainActivity app) { super(app); }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        setContentView(R.layout.hint_state);

        ((BackButton) getView(R.id.back_button)).init(app);
        final ImageNumberView imageNumberView = getView(R.id.coinImageNumber);
        imageNumberView.init(R.drawable.coin, "coin");

        for (View view : new View[] { getView(R.id.hint1), getView(R.id.hint2), getView(R.id.hint3) }) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(view, 65, 25);
            ViewHelper.setHorizontalBias(view, 0.5);
        }
        ViewHelper.setMarginTopAndBottomAsPercentOfScreen(getView(R.id.hint2), 5);

        final ImageView[] coinImages = new ImageView[] {
                getView(R.id.hint_image1), getView(R.id.hint_image2), getView(R.id.hint_image3) };
        for (int i = 0; i < coinImages.length; i++) {
            ViewHelper.setWidthAsPercentOfScreen(coinImages[i], 8);
            ViewHelper.makeSquareWithWidth(coinImages[i]);
            ViewHelper.setHorizontalBias(coinImages[i], 0.43);
            ViewHelper.setMarginTopAsPercentOfScreen(coinImages[i], (i == 0) ? 42 : 41.5);
        }

        Button[] answers = new Button[] { getView(R.id.answer1), getView(R.id.answer2), getView(R.id.answer3) };
        final Hints hints = new Hints(app);
        String[] costs = new String[] { "" + hints.getCost(0), "" + hints.getCost(1), "" + hints.getCost(2) };
        for (int i = 0; i < answers.length; i++) {
            final int num = i;
            answers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currencies.getCoins() >= hints.getCost(num)) hints.buyHint(num);
                    else {
                        app.setState(MainActivity.CURRENCYSTATE);
                        app.startNoMoneyAnimation();
                    }
                    imageNumberView.invalidate();
                }
            });
            answers[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ViewHelper.setScales(view, 1.25, 1.25);
                        ViewHelper.setScales(coinImages[num], 1.25, 1.25);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        ViewHelper.resetScales(view);
                        ViewHelper.resetScales(coinImages[num]);
                    }
                    return false;
                }
            });
            ViewHelper.setWidthAndHeightAsPercentOfScreen(answers[i], 25, 10);
            answers[i].setText(costs[i]);
            ViewHelper.centerHorizontally(answers[i]);
            ViewHelper.setMarginTopAsPercentOfScreen(answers[i], (i == 0) ? 37 : 31.5);
            ViewHelper.setPaddingLeftAsPercentOfScreen(answers[i], 12 + (3 - answers[i].getText().length()));
        }

        TextView[] hintTexts = new TextView[] {
                getView(R.id.hint1_text), getView(R.id.hint2_text), getView(R.id.hint3_text) };
        for (int i = 0; i < hintTexts.length; i++) {
            hintTexts[i].setText(hints.getName(i));
            ViewHelper.setWidthAndHeightAsPercentOfScreen(hintTexts[i], 30, 10);
            ViewHelper.setMarginTopAsPercentOfScreen(hintTexts[i], (i == 0) ? 27 : 31.5);
            ViewHelper.centerHorizontally(hintTexts[i]);
        }
    }
}
