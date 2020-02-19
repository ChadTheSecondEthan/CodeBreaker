package com.example.ciphergame.GameState;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelState extends GameState {

    private Button[] buttons;

    static final int NUM_PAGES = 5;
    static final int BUTTONS_PER_PAGE = 16;
    private int page = 1;

    /*
        Add levels completed
     */

    public LevelState(MainActivity app) {
        super(app);
    }

    public void init() {
        setContentView(R.layout.level_state);

        ((AdView) getView(R.id.level_state_ad)).loadAd(new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());

        buttons = app.getButtons(new int[] { R.id.level1, R.id.level2, R.id.level3, R.id.level4,
                R.id.level5, R.id.level6, R.id.level7, R.id.level8, R.id.level9, R.id.level10,
                R.id.level11, R.id.level12, R.id.level13, R.id.level14, R.id.level15, R.id.level16 });
        for (int i = 0; i < buttons.length; i++) {
            final int num = i;
            String text = "" + (i + 1);
            buttons[i].setText(text);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { selectLevel(num + ((page - 1) * BUTTONS_PER_PAGE)); }
            });
            ViewHelper.setWidthAndHeight(buttons[i], 20);
            ViewHelper.setMargins(buttons[i], 1.75);
        }

        getView(R.id.up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { subtractPage(); }
        });
        getView(R.id.down_button).setOnClickListener(new View.OnClickListener() {
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
    }
}
