package com.example.ciphergame.GameState;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
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

    private static final int ROWS = 3;
    static final int NUM_BUTTONS = ROWS * 4;

    /*
        TODO Add levels completed

        TODO tell user if they start a different level that the progress of the other level is erased
     */

    public LevelState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.level_state);

        // add adview
        ((AdView) getView(R.id.level_state_ad)).loadAd(
                new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());

        // set table's layout params
        ScrollView.LayoutParams scrollParams = new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollParams.leftMargin = (int) ViewHelper.percentWidth(6) - 2;
        TableLayout table = getView(R.id.levelTable);
        table.setLayoutParams(scrollParams);

        TableRow[] rows = new TableRow[ROWS];
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new TableRow(app);
            rows[i].setLayoutParams(tableParams);
        }

        // layout parameters for buttons depending on their locations
        // Margin * 8 + Size * 4 = 88 (Formula to calculate size and margin)
        final double SIZE = 15;
        final double MARGIN = 3.5;
        final double EDGE = 10;
        TableRow.LayoutParams top = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        top.setMargins((int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(EDGE), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN));

        TableRow.LayoutParams middle = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        middle.setMargins((int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN));

        TableRow.LayoutParams bottom = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        bottom.setMargins((int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(MARGIN), (int) ViewHelper.percentWidth(EDGE));

        int white = app.getResources().getColor(R.color.nearWhite, null);
        buttons = new Button[ROWS * 4];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(app);
            buttons[i].setOnClickListener(this);
            buttons[i].setId(View.generateViewId());

            // if the level's already been won, color it green
            if (app.getData().getBoolean(getString(GameState.LEVEL_WON, getTextPack(), i), false))
                buttons[i].setBackgroundResource(R.drawable.basic_rectangle_green);

            // it its in progress, color it red
            else if (app.getData().getBoolean(getString(GameState.IN_PROGRESS, getTextPack(), i), false))
                buttons[i].setBackgroundResource(R.drawable.basic_rectangle_red);

            // otherwise it's just black and white
            else
                buttons[i].setBackgroundResource(R.drawable.basic_rectangle);

            buttons[i].setTextColor(white);

            // set layout params depending on location
            if (i / 4 == 0)
                buttons[i].setLayoutParams(top);
            else if (i / 4 == ROWS - 1)
                buttons[i].setLayoutParams(bottom);
            else
                buttons[i].setLayoutParams(middle);

            String text = "" + (i + 1);
            buttons[i].setText(text);
            rows[i / 4].addView(buttons[i]);
        }

        for (TableRow row : rows) table.addView(row);

        // TODO find better color
        String levelsComplete = getLevelsComplete(getTextPack()) + " / " + (NUM_BUTTONS);
        ((TextView) getView(R.id.levelsComplete)).setText(levelsComplete);
        ((TextView) getView(R.id.curPack)).setText(MainActivity.TEXT_PACKS[getTextPack()]);

        addHomeButton(MainActivity.TEXTPACKSTATE);
        addVolumeButton();
    }

    @Override
    public void onClick(View view) {
        buttonClick();
        for (int i = 0; i < buttons.length; i++)
            if (view.getId() == buttons[i].getId()) {
                app.getDataEditor().putInt(GameState.LEVEL, i).apply();
                app.setState(MainActivity.INLEVELSTATE);
                return;
            }
    }
}
