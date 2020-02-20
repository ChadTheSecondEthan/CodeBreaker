package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LevelState extends GameState implements View.OnClickListener {

    private Button[] buttons;

    static final int NUM_PAGES = 5;
    static final int BUTTONS_PER_PAGE = 16;
    private int page = 1;

    /*
        TODO Add levels completed
     */

    public LevelState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.level_state);

        ((AdView) getView(R.id.level_state_ad)).loadAd(new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());

        buttons = app.getButtons(new int[] { R.id.level1, R.id.level2, R.id.level3, R.id.level4,
                R.id.level5, R.id.level6, R.id.level7, R.id.level8, R.id.level9, R.id.level10,
                R.id.level11, R.id.level12, R.id.level13, R.id.level14, R.id.level15, R.id.level16 });
        final double WIDTH = 18, MARGIN = 1.75;
        for (int i = 0; i < buttons.length; i++) {
            String text = "" + (i + 1);
            buttons[i].setText(text);
            buttons[i].setOnClickListener(this);
            ViewHelper.setWidthAndHeight(buttons[i], WIDTH);
            ViewHelper.setMargins(buttons[i], MARGIN);
        }

        getView(R.id.up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page != 1) {
                    page--;
                    updatePages();
                }
            }
        });
        getView(R.id.down_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page != NUM_PAGES) {
                    page++;
                    updatePages();
                }
            }
        });
        getView(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.MENU); }
        });
        getView(R.id.volumeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.volumeClick(); }
        });

        String levelsComplete = app.getData().getInt("levelsComplete", 0) + " / " + (NUM_PAGES * BUTTONS_PER_PAGE);
        ((TextView) getView(R.id.levelsComplete)).setText(levelsComplete);
        ((TextView) getView(R.id.curPack)).setText(app.getData().getString("textPack", "unknown"));

        Button button = new Button(app);
        button.setLayoutParams(new ConstraintLayout.LayoutParams((int) ViewHelper.percentWidth(100), (int) ViewHelper.percentHeight(100)));
    }

    private void updatePages() {
        for (int i = 0; i < buttons.length; i++) {
            String text = "" + ((page - 1) * BUTTONS_PER_PAGE + i + 1);
            buttons[i].setText(text);
        }
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < buttons.length; i++)
            if (view.getId() == buttons[i].getId()) {
                app.getInLevelState().setLevelNumber(i);
                app.setState(MainActivity.INLEVELSTATE);
            }
    }
}
