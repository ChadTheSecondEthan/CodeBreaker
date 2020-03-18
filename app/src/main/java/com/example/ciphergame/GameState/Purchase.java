package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ciphergame.Currencies;
import com.example.ciphergame.Hints;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;

public class Purchase extends GameState implements View.OnClickListener {
    
    private Button[] hintButtons;
    private Button[] currencyButtons;
    
    private Hints hints;

    public Purchase(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.purchase);
        
        hints = new Hints(app);
        String[] costs = new String[] { "" + hints.getCost(0), "" + hints.getCost(1), "" + hints.getCost(2) };
        
        // add hint buttons
        TableRow hintRow = getView(R.id.hintButtons);
        
        double WIDTH = 20;
        double HEIGHT = 25;
        double MARGIN = 2;
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams((int) ViewHelper.percentWidth(WIDTH), (int) ViewHelper.percentWidth(HEIGHT));
        rowParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);
        hintButtons = new Button[3];
        for (int i = 0; i < hintButtons.length; i++) {
            hintButtons[i] = new Button(app);
            hintButtons[i].setId(View.generateViewId());
            hintButtons[i].setOnClickListener(this);
            hintButtons[i].setText(costs[i]);
            hintRow.addView(hintButtons[i], rowParams);
        }
        
        // add currency scroll view
        LinearLayout currencyLayout = getView(R.id.currencyLayout);

        WIDTH = 60;
        HEIGHT = 20;
        MARGIN = 4;
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(WIDTH), (int) ViewHelper.percentWidth(HEIGHT));
        linearParams.setMargins(0, (int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN));
        currencyButtons = new Button[Currencies.NUM_PURCHASES];

        // create array of buttons
        String text = "hi";
        for (int i = 0; i < currencyButtons.length; i++) {
            currencyButtons[i] = new Button(app);
            currencyButtons[i].setId(View.generateViewId());
            currencyButtons[i].setOnClickListener(this);
            currencyButtons[i].setText(text);
            currencyLayout.addView(currencyButtons[i], linearParams);
        }

        // set width of scroll view for currency buttons
        ViewHelper.setHeight(getView(R.id.currencyScroll), 67.5);

        // add extra buttons
        addHomeButton();
        addVolumeButton();
    }

    private void startNoMoneyAnimation() {
        final TextView textView = getView(R.id.no_money);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(ViewHelper.fadeOutAnimation(textView));
    }

    @Override
    public void onClick(View view) {
        Currencies currencies = MainActivity.getCurrencies();
        int id = view.getId();
        for (int i = 0; i < hintButtons.length; i++) 
            if (id == hintButtons[i].getId())
                if (currencies.getCoins() >= hints.getCost(i)) hints.buyHint(i);
                else startNoMoneyAnimation();
//        TODO
//        for (int i = 0; i < currencyButtons.length; i++)
//            if (id == currencyButtons[i].getId()) {
//
//            }
    }
}
