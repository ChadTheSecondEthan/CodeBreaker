package com.example.ciphergame.GameState;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.R;
import com.example.ciphergame.ViewHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelState extends GameState implements View.OnClickListener {

    private Button[] buttons;

    private static final int ROWS = 25;
    static final int NUM_BUTTONS = ROWS * 4;

    /*
        TODO Add levels completed

        TODO add padding top and bottom to scroll view with buttons
     */

    public LevelState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.level_state);

        ((AdView) getView(R.id.level_state_ad)).loadAd(new AdRequest.Builder().addTestDevice("").build());

        ViewHelper.setHeight(getView(R.id.levelScroll), 75);
        TableLayout table = getView(R.id.levelTable);
        table.setPadding(0, (int) ViewHelper.percentHeight(23), 0, (int) ViewHelper.percentHeight(7));

        TableRow[] rows = new TableRow[ROWS];
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new TableRow(app);
            rows[i].setLayoutParams(tableParams);
        }

        final double SIZE = 18;
        final double MARGIN = 2;
        TableRow.LayoutParams params = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        params.setMargins((int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN));
        buttons = new Button[ROWS * 4];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(app);
            buttons[i].setOnClickListener(this);
            if (i / 4 == 0) {
                TableRow.LayoutParams lp = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
                lp.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN));
                buttons[i].setLayoutParams(lp);
            } else if (i / 4 == ROWS - 1) {
                TableRow.LayoutParams lp = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
                lp.setMargins((int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), 0);
                buttons[i].setLayoutParams(lp);
            }
            else buttons[i].setLayoutParams(params);
            String text = "" + (i + 1);
            buttons[i].setText(text);
            rows[i / 4].addView(buttons[i]);
        }

        for (TableRow row : rows) table.addView(row);

        // TODO find better color
        String levelsComplete = app.getData().getInt("levelsComplete", 0) + " / " + (NUM_BUTTONS);
        ((TextView) getView(R.id.levelsComplete)).setText(levelsComplete);
        ((TextView) getView(R.id.curPack)).setText(MainActivity.TEXT_PACKS[app.getData().getInt("textPack", -1)]);

        addHomeButton();
        addVolumeButton();
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < buttons.length; i++)
            if (view.getId() == buttons[i].getId()) {
                app.getDataEditor().putInt("level", i).apply();
                app.setState(MainActivity.INLEVELSTATE);
                return;
            }
    }
}
