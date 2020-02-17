package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciphergame.Hints;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.Views.ImageNumberView;
import com.example.ciphergame.ViewHelper;

public class Purchase extends GameState {

    public Purchase(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.purchase);

        ((BackButton) getView(R.id.back_button)).init(app);

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

        final ImageNumberView imageNumberView = getView(R.id.coinImageNumber);
        imageNumberView.init(R.drawable.coin, "coin");

        ImageView[] coinImages = app.getImageViews(new int[] { R.id.hint_image1, R.id.hint_image2, R.id.hint_image3 });
        for (ImageView coin : coinImages) {
            ViewHelper.setWidthAsPercentOfScreen(coin, 8);
            ViewHelper.makeSquareWithWidth(coin);
            ViewHelper.setMarginLeftAndRight(coin, 7.5);
            ViewHelper.setMarginTop(coin, 10);
        }
        ViewHelper.setMarginLeft(coinImages[0], 20);

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
                    imageNumberView.invalidate();
                }
            });
            ViewHelper.setGetBiggerTouchListener(answers[i], 1.25);
            ViewHelper.setWidthAndHeightAsPercentOfScreen(answers[i], 25, 10);
            answers[i].setText(costs[i]);
            ViewHelper.centerHorizontally(answers[i]);
            ViewHelper.matchAttributes(answers[i], coinImages[i]);
//            ViewHelper.setMarginTop(answers[i], (i == 0) ? 37 : 31.5);
            ViewHelper.setPaddingLeft(answers[i], 12 + (3 - answers[i].getText().length()));
        }

        TextView[] hintTexts = app.getTextViews(new int[] { R.id.hint1_text, R.id.hint2_text, R.id.hint3_text });
        for (int i = 0; i < hintTexts.length; i++) {
            hintTexts[i].setText(hints.getName(i));
            ViewHelper.setWidthAndHeightAsPercentOfScreen(hintTexts[i], 20, 10);
            ViewHelper.setMarginLeftAndRight(hintTexts[i], 2.5);
            ViewHelper.centerHorizontally(hintTexts[i]);
        }
    }

    public void startNoMoneyAnimation() {
        final TextView textView = getView(R.id.no_money);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(ViewHelper.fadeOutAnimation(textView));
    }
}
