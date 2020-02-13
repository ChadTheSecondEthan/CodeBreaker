package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.BackButton;
import com.example.ciphergame.R;
import com.example.ciphergame.Views.Title;
import com.example.ciphergame.Views.ViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelState extends GameState {

    private Button[] buttons;
    private TextView pageText;

    private static final int NUM_PAGES = 5;
    private static final int BUTTONS_PER_PAGE = 20;
    private int page = 1;

    public LevelState(MainActivity app) {
        super(app);
    }

    public void init() {
        setContentView(R.layout.level_state);

        ((AdView) getView(R.id.level_state_ad)).loadAd(new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());
        ((BackButton) getView(R.id.back_button)).init(app);
        ((Title) getView(R.id.title)).init(R.string.levelTitle);

        buttons = app.getButtons(new int[] { R.id.level1, R.id.level2, R.id.level3, R.id.level4,
                R.id.level5, R.id.level6, R.id.level7, R.id.level8, R.id.level9, R.id.level10,
                R.id.level11, R.id.level12, R.id.level13, R.id.level14, R.id.level15, R.id.level16,
                R.id.level17, R.id.level18, R.id.level19, R.id.level20 });
        for (int i = 0; i < buttons.length; i++) {
            final int num = i;
            String text = "" + (i + 1);
            buttons[i].setText(text);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { selectLevel(num + ((page - 1) * BUTTONS_PER_PAGE)); }
            });
            ViewHelper.setGetBiggerTouchListener(buttons[i]);
            ViewHelper.setWidthAsPercentOfScreen(buttons[i], 20);
            ViewHelper.makeSquareWithWidth(buttons[i]);
        }

        String text = "1 / " + NUM_PAGES;
        pageText = getView(R.id.level_page_number);
        pageText.setText(text);

        for (Button button : app.getButtons(new int[] { R.id.level_left_button, R.id.level_right_button })) {
            ViewHelper.setWidthAndHeightAsPercentOfScreen(button, 80 / 9.0);
            ViewHelper.makeSquareWithWidth(button);
            ViewHelper.setGetBiggerTouchListener(button);
        }
        getView(R.id.level_left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { subtractPage(); }
        });
        getView(R.id.level_right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addPage(); }
        });
    }

    private void selectLevel(int level) {
        app.getInLevelState().setLevelNumber(level);
        app.setState(MainActivity.INLEVELSTATE);
    }

    private void subtractPage() {
        if (page != 1) {
            page--;
            updatePages();
        }
    }
    private void addPage() {
        if (page != NUM_PAGES) {
            page++;
            updatePages();
        }
    }
    private void updatePages() {
        for (int i = 0; i < buttons.length; i++) {
            String text = "" + (i + ((page - 1) * BUTTONS_PER_PAGE) + 1);
            buttons[i].setText(text);
        }
        String text = "" + page + " / " + NUM_PAGES;
        pageText.setText(text);
    }
}
