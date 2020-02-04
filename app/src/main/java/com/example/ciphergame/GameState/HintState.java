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

        for (View view : app.getViews(new int[] { R.id.hint1, R.id.hint2, R.id.hint3 })) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(view, 65, 25);
            ViewHelper.setHorizontalBias(view, 0.5);
        }
        ViewHelper.setMarginTopAndBottomAsPercentOfScreen(getView(R.id.hint2), 5);

        ImageView[] coinImages = app.getImageViews(new int[] {R.id.hint_image1, R.id.hint_image2, R.id.hint_image3 });
        for (int i = 0; i < coinImages.length; i++) {
            ViewHelper.setWidthAsPercentOfScreen(coinImages[i], 8);
            ViewHelper.makeSquareWithWidth(coinImages[i]);
            ViewHelper.setHorizontalBias(coinImages[i], 0.43);
            ViewHelper.setMarginTopAsPercentOfScreen(coinImages[i], (i == 0) ? 42 : 41.5);
        }

        Button[] answers = app.getButtons(new int[] { R.id.answer1, R.id.answer2, R.id.answer3 });
        final Hints hints = new Hints(app);
        String[] costs = new String[] { "" + hints.getCost(0), "" + hints.getCost(1), "" + hints.getCost(2) };
        for (int i = 0; i < answers.length; i++) {
            final int num = i;
            answers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currencies.getCoins() >= hints.getCost(num)) hints.buyHint(num);
                    else app.setState(MainActivity.CURRENCYSTATE).startNoMoneyAnimation();
                    imageNumberView.invalidate();
                }
            });
            ViewHelper.setGetBiggerTouchListener(answers[i], 1.25);
            ViewHelper.setWidthAndHeightAsPercentOfScreen(answers[i], 25, 10);
            answers[i].setText(costs[i]);
            ViewHelper.centerHorizontally(answers[i]);
            ViewHelper.setMarginTopAsPercentOfScreen(answers[i], (i == 0) ? 37 : 31.5);
            ViewHelper.setPaddingLeftAsPercentOfScreen(answers[i], 12 + (3 - answers[i].getText().length()));
        }

        TextView[] hintTexts = app.getTextViews(new int[] { R.id.hint1_text, R.id.hint2_text, R.id.hint3_text });
        for (int i = 0; i < hintTexts.length; i++) {
            hintTexts[i].setText(hints.getName(i));
            ViewHelper.setWidthAndHeightAsPercentOfScreen(hintTexts[i], 30, 10);
            ViewHelper.setMarginTopAsPercentOfScreen(hintTexts[i], (i == 0) ? 27 : 31.5);
            ViewHelper.centerHorizontally(hintTexts[i]);
        }
    }
}
