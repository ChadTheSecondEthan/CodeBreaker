package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.Hints;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

public class Purchase extends GameState {

    public Purchase(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.purchase);

        // TODO hints on top left to right
        // TODO purchases at bottom in vertical scroll view

//        Button[] buttons = app.getButtons(new int [] {
//                R.id.currency_button1, R.id.currency_button2, R.id.currency_button3,
//                R.id.currency_button4, R.id.currency_button5, R.id.currency_button6 });
//        for (int i = 0; i < buttons.length; i++) {
//            ViewHelper.setMarginBottom(buttons[i], 12);
//            ViewHelper.setWidthAndHeightAsPercentOfScreen(buttons[i], 60, 20);
//            ViewHelper.centerHorizontally(buttons[i]);
//            String text = "Thingy" + i;
//            buttons[i].setText(text);
//        }
//        ViewHelper.setMarginTopAndBottom(getView(R.id.currency_button1), 6, 12);
//        ViewHelper.setMarginBottom(getView(R.id.currency_button6), 6);

        Button[] answers = app.getButtons(new int[] { R.id.answer1, R.id.answer2, R.id.answer3 });
        final Hints hints = new Hints(app);
        String[] costs = new String[] { "" + hints.getCost(0), "" + hints.getCost(1), "" + hints.getCost(2) };
        for (int i = 0; i < answers.length; i++) {
            final int num = i;
            answers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currencies.getCoins() >= hints.getCost(num)) hints.buyHint(num);
                    else startNoMoneyAnimation();
                }
            });
            ViewHelper.setGetBiggerTouchListener(answers[i], 1.25);
            answers[i].setText(costs[i]);
        }

        TextView[] hintTexts = app.getTextViews(new int[] { R.id.hint1_text, R.id.hint2_text, R.id.hint3_text });
        for (int i = 0; i < hintTexts.length; i++) hintTexts[i].setText(hints.getName(i));
    }

    public void startNoMoneyAnimation() {
        final TextView textView = getView(R.id.no_money);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(ViewHelper.fadeOutAnimation(textView));
    }
}
