package com.example.ciphergame.GameState;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.Hints;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

// TODO if they dont have enough coins, just flash the "coins" number red, dont show text

public class Purchase extends GameState {
    
    private Button[] hintButtons;
    private Button[] currencyButtons;
    private TextView money;
    
    private Hints hints;

    // since they can go to this screen without selecting a level
    private boolean levelSelected;

    public Purchase(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.purchase);

        levelSelected = getLevel() != -1 && getTextPack() != -1;
        
        hints = new Hints(app);
        int[] costs = hints.getCosts();
        String[] names = hints.getNames();
        
        // add hint buttons
        TableRow hintRow = getView(R.id.hintButtons);

        // create layout for hint buttons
        double SIZE = 25;
        double MARGIN = 2;
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        rowParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);

        // text color for hint buttons and currency buttons
        final int textColor = app.getResources().getColor(R.color.nearWhite, null);

        hintButtons = new Button[3];
        for (int i = 0; i < hintButtons.length; i++) {
            hintButtons[i] = new Button(app);
            hintButtons[i].setId(View.generateViewId());
            hintButtons[i].setOnClickListener(hintClick);
            hintButtons[i].setBackgroundResource(R.drawable.basic_rectangle);

            // set text with name and cost
            Spanned text = Html.fromHtml(names[i] + "<br><br>" + costs[i], 0);
            hintButtons[i].setTextColor(textColor);
            hintButtons[i].setText(text);
            hintRow.addView(hintButtons[i], rowParams);
        }

        // also tell user how much money they have or if no level is selected
        money = getView(R.id.money);
        String text;
        if (!levelSelected) {
            text = "No Level Selected";
            money.setTextColor(Color.RED);
        } else {
            text = "Money: " + MainActivity.getCurrencies().getCoins();
            money.setTextColor(Color.WHITE);
        }

        money.setText(text);
        
        // add currency scroll view
        LinearLayout currencyLayout = getView(R.id.currencyLayout);

        // layout for currency buttons
        double WIDTH = 60;
        double HEIGHT = 20;
        MARGIN = 4;
        double EDGE = 10;
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(WIDTH), (int) ViewHelper.percentWidth(HEIGHT));
        linearParams.setMargins(0, (int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN));

        // create array of buttons
        currencyButtons = new Button[Currencies.NUM_PURCHASES];
        for (int i = 0; i < currencyButtons.length; i++) {
            currencyButtons[i] = new Button(app);
            currencyButtons[i].setId(View.generateViewId());
            currencyButtons[i].setOnClickListener(purchaseClick);
            currencyButtons[i].setBackgroundResource(R.drawable.basic_rectangle);
            currencyButtons[i].setTextColor(textColor);
            if (i == 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(WIDTH), (int) ViewHelper.percentWidth(HEIGHT));
                lp.setMargins(0, (int) ViewHelper.percentWidth(EDGE), 0, (int) ViewHelper.percentWidth(MARGIN));
                currencyButtons[i].setLayoutParams(lp);
            } else if (i == currencyButtons.length - 1) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(WIDTH), (int) ViewHelper.percentWidth(HEIGHT));
                lp.setMargins(0, (int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(EDGE));
                currencyButtons[i].setLayoutParams(lp);
            } else
                currencyButtons[i].setLayoutParams(linearParams);
//            currencyButtons[i].setText();
            currencyLayout.addView(currencyButtons[i]);
        }

        // add extra buttons
        addHomeButton(MainActivity.MENU);
        addVolumeButton();
    }

    private void startAnimation(String s) {
        // TODO test
        CharSequence curText = money.getText();
        money.setText(s);
        ObjectAnimator moneyAnimator = ObjectAnimator.ofInt(money, "textColor", Color.WHITE, Color.RED);
        moneyAnimator.setInterpolator(new LinearInterpolator());
        moneyAnimator.setDuration(200);
        moneyAnimator.setRepeatCount(5);
        moneyAnimator.setRepeatMode(ValueAnimator.REVERSE);
        moneyAnimator.setEvaluator(new ArgbEvaluator());
        AnimatorSet t = new AnimatorSet();
        t.play(moneyAnimator);
        t.start();
        money.setText(curText);
    }

    private View.OnClickListener hintClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClick();
            Currencies currencies = MainActivity.getCurrencies();
            int id = view.getId();
            for (int i = 0; i < hintButtons.length; i++)

                if (id == hintButtons[i].getId() && levelSelected) {
                    if (currencies.getCoins() >= hints.getCosts()[i]) {
                        // if you can buy the hint, buy it
                        if (hints.buyHint(i))
                            startAnimation("Unable to buy hint.");
                    } else
                        // otherwise tell the user
                        startAnimation("Not enough money.");
                }
        }
    };

    private View.OnClickListener purchaseClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClick();
            int id = view.getId();
            for (int i = 0; i < currencyButtons.length; i++)
                if (id == currencyButtons[i].getId()) {}
        }
    };
}
