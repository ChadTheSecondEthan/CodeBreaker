package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelState extends GameState implements View.OnClickListener {

    private Button[] buttons;
    private TextView pageText;

    private static final int NUM_PAGES = 5;
    private static final int BUTTONS_PER_PAGE = 20;
    private int page = 1;

    LevelState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        setContentView(R.layout.level_state);

        AdView adView = getView(R.id.level_state_ad);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build();
        adView.loadAd(adRequest);

        buttons = new Button[] {
                getView(R.id.level1), getView(R.id.level2), getView(R.id.level3), getView(R.id.level4),
                getView(R.id.level5), getView(R.id.level6), getView(R.id.level7), getView(R.id.level8),
                getView(R.id.level9), getView(R.id.level10), getView(R.id.level11), getView(R.id.level12),
                getView(R.id.level13), getView(R.id.level14), getView(R.id.level15), getView(R.id.level16),
                getView(R.id.level17), getView(R.id.level18), getView(R.id.level19), getView(R.id.level20) };
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText("" + (i + 1));
            buttons[i].setOnClickListener(this);
            ViewHelper.setGetBiggerTouchListener(buttons[i]);
            ViewHelper.setWidthAsPercentOfScreen(buttons[i], 20);
            ViewHelper.makeSquareWithWidth(buttons[i]);
        }

        pageText = getView(R.id.level_page_number);
        pageText.setText("1 / " + NUM_PAGES);

        for (Button button : new Button[] { getView(R.id.level_left_button), getView(R.id.level_right_button) }) {
            button.setOnClickListener(this);
            ViewHelper.setWidthAndHeightAsPercentOfScreen(button, 80 / 9.0);
            ViewHelper.makeSquareWithWidth(button);
            ViewHelper.setGetBiggerTouchListener(button);
        }

        ((BackButton) getView(R.id.back_button)).init(gsm);
        ((Title) getView(R.id.title)).init(R.string.levelTitle);
    }

    private void selectLevel(int level) {
        gsm.getInLevelState().setLevelNumber(level);
        gsm.setState(GameStateManager.INLEVELSTATE);
    }

    private void subtractPage() {
        if (page != 1) {
            page--;
            for (int i = 0; i < buttons.length; i++)
                buttons[i].setText("" + (i + ((page - 1) * BUTTONS_PER_PAGE) + 1));
            pageText.setText("" + page + " / " + NUM_PAGES);
        }
    }
    private void addPage() {
        if (page != NUM_PAGES) {
            page++;
            for (int i = 0; i < buttons.length; i++)
                buttons[i].setText("" + (i + ((page - 1) * BUTTONS_PER_PAGE) + 1));
            pageText.setText("" + page + " / " + NUM_PAGES);
        }
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < buttons.length; i++)
            if (view.getId() == buttons[i].getId()) {
                selectLevel(i + ((page - 1) * BUTTONS_PER_PAGE));
                return;
            }
        if (view.getId() == R.id.level_left_button)
            subtractPage();
        else if (view.getId() == R.id.level_right_button)
            addPage();
    }
}
