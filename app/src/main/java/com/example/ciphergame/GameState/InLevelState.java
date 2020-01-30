package com.example.ciphergame.GameState;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.*;
import com.example.ciphergame.Cipher;
import com.example.ciphergame.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class InLevelState extends GameState implements View.OnClickListener {

    /*
        TODO add currencies, like lives and coins, which can be used for hints or answers
     */

    private static final String WHITE = "<font color=#FFFFFF>";
    private static final String RED = "<font color=#FF0000>";
    private static final String FONT = "</font>";

    private TextView text;
    private Button[] letters; // the bottom letters
    private Button[] cipherLetters; // the top letters
    private int selectedLetter = -1;

    private int level;
    private int textPack;
    private Cipher cipher;
    private String hintCipherText;
    private String cipherText;
    private String curCipherText;

    private boolean instructionsOpen;

    public InLevelState(MainActivity app) {
        super(app);
    }

    public void init() {
        setContentView(R.layout.in_level_state);

        AdView adView = getView(R.id.in_level_ad);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build();
        adView.loadAd(adRequest);

        BackButton backButton = getView(R.id.back_button);
        backButton.init(app);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instructionsOpen) openInstructions();
                else app.setState(app.getPrevState());
            }
        });
        ((VolumeButton) getView(R.id.volume_button)).init(app, ViewHelper.TOP_RIGHT);

        Button reset = getView(R.id.reset_answer);
        Button hint = getView(R.id.hint_button);
        Button lives = getView(R.id.lives_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAnswer();
            }
        });
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.HINTSTATE); }
        });
        lives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.CURRENCYSTATE); }
        });
        for (Button button : new Button[] { reset, hint, lives }) {
            button.setBackgroundColor(Color.TRANSPARENT);
            ViewHelper.setWidthAndHeightAsPercentOfScreen(button, 30, 5);
        }
        Button checkAnswer = getView(R.id.checkAnswer);
        checkAnswer.setBackgroundColor(Color.TRANSPARENT);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
        ViewHelper.setWidthAndHeightAsPercentOfScreen(checkAnswer, 90, 5);

        cipher = new Cipher(app.getApplicationContext());
        cipherText = withoutHtml(app.getData().getString("cipherText", cipher.getText(textPack, level)));
        app.getDataEditor().putString("cipherText", cipherText).apply();
        hintCipherText = app.getData().getString("hintCipherText", cipherText);
        curCipherText = app.getData().getString("curCipherText", cipherText);

        ViewHelper.setWidthAsPercentOfScreen(getView(R.id.scrollViewTop), 94);
        ViewHelper.setWidthAsPercentOfScreen(getView(R.id.scrollViewBottom), 94);

        // TODO make this more efficient
        letters = new Button[] { getView(R.id.a), getView(R.id.b),
                getView(R.id.c), getView(R.id.d), getView(R.id.e), getView(R.id.f),
                getView(R.id.g), getView(R.id.h), getView(R.id.i), getView(R.id.j),
                getView(R.id.k), getView(R.id.l), getView(R.id.m), getView(R.id.n),
                getView(R.id.o), getView(R.id.p), getView(R.id.q), getView(R.id.r),
                getView(R.id.s), getView(R.id.t), getView(R.id.u), getView(R.id.v),
                getView(R.id.w), getView(R.id.x), getView(R.id.y), getView(R.id.z) };
        cipherLetters = new Button[] { getView(R.id.a_text), getView(R.id.b_text),
                getView(R.id.c_text), getView(R.id.d_text), getView(R.id.e_text), getView(R.id.f_text),
                getView(R.id.g_text), getView(R.id.h_text), getView(R.id.i_text), getView(R.id.j_text),
                getView(R.id.k_text), getView(R.id.l_text), getView(R.id.m_text), getView(R.id.n_text),
                getView(R.id.o_text), getView(R.id.p_text), getView(R.id.q_text), getView(R.id.r_text),
                getView(R.id.s_text), getView(R.id.t_text), getView(R.id.u_text), getView(R.id.v_text),
                getView(R.id.w_text), getView(R.id.x_text), getView(R.id.y_text), getView(R.id.z_text) };
        for (Button button : letters) {
            ViewHelper.setPaddingBottomAsPercentOfScreen(button, 0.25);
            ViewHelper.setWidthAsPercentOfScreen(button, 10);
            ViewHelper.makeSquareWithWidth(button);
        }
        for (Button button : cipherLetters) {
            ViewHelper.setPaddingBottomAsPercentOfScreen(button, 0.25);
            ViewHelper.setWidthAsPercentOfScreen(button, 10);
            ViewHelper.makeSquareWithWidth(button);
        }
        resetText();

        text = getView(R.id.in_level_text);
        updateText();
        text.setTextSize((cipherText.length() >= 250) ? (float) (20 - ((cipherText.length() - 250) / 50.0)) : 20);

        Button instructions = getView(R.id.instructions);
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstructions();
            }
        });
        ViewHelper.setGetBiggerTouchListener(instructions);
        ViewHelper.setMarginTopAsPercentOfScreen(instructions, 1);

        ViewHelper.setPaddingTopAsPercentOfScreen(getView(R.id.instructions_text), 20);

        instructionsOpen = false;
    }

    private void openInstructions() {
        instructionsOpen = !instructionsOpen;
        getView(R.id.instructions_text).setVisibility((instructionsOpen) ? View.VISIBLE : View.INVISIBLE);
        getView(R.id.volume_button).setVisibility((instructionsOpen) ? View.INVISIBLE : View.VISIBLE);
        getView(R.id.instructions).setVisibility((instructionsOpen) ? View.INVISIBLE : View.VISIBLE);
    }

    // TODO fix this
    private void resetAnswer() {
        if (selectedLetter != -1) {
            letters[selectedLetter].setBackgroundResource(R.drawable.circle);
            changeTextColor(-1);
            selectedLetter = -1;
        }
        curCipherText = hintCipherText;
        app.getDataEditor().putString("curCipherText", curCipherText).apply();
        resetCipherLetters();
        updateText();
    }

    private void resetCipherLetters() {
        String regularText = cipher.getRegularText(textPack, level).toUpperCase();
        char[] alphabet = cipher.getCipherAlphabet();
        for (int i = 0; i < 26; i++)
            cipherLetters[i].setText(regularText.contains("" + (alphabet[i])) ? app.getData().getString("cipherLetter" + i, "") : "-");
    }

    // TODO make this faster
    private void resetText() {
        String regularText = cipher.getRegularText(textPack, level).toUpperCase();
        int color = app.getApplicationContext().getColor(R.color.opaqueBlack);
        char[] alphabet = cipher.getCipherAlphabet();
        // reset letters
        for (int i = 0; i < 26; i++) {
            letters[i].setOnClickListener(this);
            letters[i].setBackgroundResource(R.drawable.circle);
            letters[i].setText(cipherText.contains("" + (char) (i + Cipher.UPPER_CASE_START)) ? "" + (char) (i + Cipher.UPPER_CASE_START) : "-");
        }
        // reset cipherLetters
        for (int i = 0; i < 26; i++) {
            cipherLetters[i].setOnClickListener(this);
            cipherLetters[i].setBackgroundResource(R.drawable.circle);
            cipherLetters[i].setTextColor(color);
            if (regularText.contains("" + alphabet[i]))
                for (int j = 0; j < cipherText.length(); j++)
                    if (regularText.charAt(j) == (char) (i + Cipher.LOWER_CASE_START)) {
                        cipherLetters[i].setText("" + cipherText.charAt(j));
                        break;
                    }
        }
    }

    private void checkAnswer() {
        if (withoutHtml(curCipherText).equals(cipher.getRegularText(textPack, level).toLowerCase()) || withoutHtml(text.getText().toString()).equals("You won")) {
            text.setText(Html.fromHtml(WHITE + "You won" + FONT, 1));
            currencies.levelComplete();
            app.removeTexts();
            for (int i = 0; i < 26; i++) {
                cipherLetters[i].setOnClickListener(null);
                letters[i].setOnClickListener(null);
            }
            level++;
            if (level == 100) app.setState(MainActivity.LEVELSTATE);
        } else {
            TextView wrong = getView(R.id.chooseLetterText);
            wrong.setText("Wrong answer");
            wrong.startAnimation(ViewHelper.fadeAnimation(wrong));
        }
    }

    private void changeTextColor(int letter) {
        // changes the selected letters to red
        if (letter != -1) {
            char realLetter = (char) (letter + Cipher.UPPER_CASE_START);
            char curLetter = realLetter;
            for (int i = 0; i < cipherText.length(); i++)
                if (cipherText.charAt(i) == realLetter) {
                    curLetter = withoutHtml(curCipherText).charAt(i);
                    break;
                }
            text.setText(Html.fromHtml(curCipherText.replace(WHITE + curLetter + FONT, "" + realLetter)
                    .replace(WHITE, "@").replace(FONT, "*")
                    .replace("" + realLetter, RED + realLetter + FONT)
                    .replace("@", WHITE).replace("*", FONT), 1));
        } else updateText();
    }

    private void changeText(int oldLetter, int newLetter) {
        curCipherText = curCipherText.replace(WHITE, "*").replace(FONT, "@")
                .replace("" + (char) (oldLetter + Cipher.UPPER_CASE_START),
                WHITE + (char) (newLetter + Cipher.LOWER_CASE_START) + FONT)
                .replace("@", FONT).replace("*",  WHITE);
        updateText();
        app.getDataEditor().putString("curCipherText", curCipherText).apply();
        checkAnswer();
    }

    private void removeLetter(int index) {
        // deselects a given cipherLetter and assumes the cipherLetter has already been selected
        // TODO make this work
        if (isNotHint(index)) {
            curCipherText = curCipherText.replace(WHITE + (char) (index + Cipher.LOWER_CASE_START) + FONT, cipherLetters[index].getText());
            app.getDataEditor().putString("curCipherText", curCipherText).apply();
            updateText();
        }
    }

    private char[] getCurAlphabet() {
        char[] alphabet = new char[26];
        for (int i = 0; i < cipherLetters.length; i++)
            alphabet[i] = Character.toUpperCase(firstLetterOf(cipherLetters[i]));
        return alphabet;
    }
    private boolean lettersContain(int letter) {
        for (int i = 0; i < 26; i++)
            if (letters[i].getText().equals("" + (char) (letter + Cipher.UPPER_CASE_START)))
                return true;
        return false;
    }

    private void changeHint(int oldLetter, int newLetter) {
        changeText(oldLetter, newLetter);
        hintCipherText = hintCipherText.replace(WHITE + (char) (oldLetter + Cipher.UPPER_CASE_START) + FONT, "" + (char) (oldLetter + Cipher.UPPER_CASE_START))
                .replace("" + (char) (oldLetter + Cipher.UPPER_CASE_START), WHITE + (char) (newLetter + Cipher.LOWER_CASE_START) + FONT);
        app.getDataEditor().putString("hintCipherText", hintCipherText).apply();
    }

    public void peek() {
        char[] cipherAlphabet = cipher.getCipherAlphabet();
        char[] curCipherAlphabet = getCurAlphabet();
        boolean[] canCorrect;
        int randChoice;
        canCorrect = new boolean[26];
        for (int i = 0; i < 26; i++)
            canCorrect[i] = cipherAlphabet[i] != curCipherAlphabet[i] && firstLetterOf(cipherLetters[i]) != '-' && lettersContain(i);
        // TODO fix this

        // pick a random letter to switch
        do randChoice = (int) (Math.random() * 25);
        while (!canCorrect[randChoice]);

        cipherLetters[randChoice].setText("" + cipherAlphabet[randChoice]);
        changeHint(cipherAlphabet[randChoice] - Cipher.UPPER_CASE_START, randChoice);
        app.getDataEditor().putString("cipherLetter" + randChoice, "" + cipherAlphabet[randChoice]).apply();
        app.setState(MainActivity.INLEVELSTATE);
        resetCipherLetters();
        final TextView lettersSwitched = getView(R.id.chooseLetterText);
        lettersSwitched.setText(cipherAlphabet[randChoice] + " replaced with " + (char) (randChoice + Cipher.LOWER_CASE_START));
        lettersSwitched.setVisibility(View.VISIBLE);
        lettersSwitched.startAnimation(ViewHelper.fadeAnimation(lettersSwitched));
    }

    public void choose() {
        // TODO add this hint
        app.setState(MainActivity.INLEVELSTATE);
        resetCipherLetters();
        TextView chooseLetter = getView(R.id.chooseLetterText);
        chooseLetter.setText(R.string.pickLetter);
        chooseLetter.setVisibility(View.VISIBLE);
        // TODO if one on the top has already been used for another hint, it can't be picked, and the bottom letters can't be picked either
        for (int i = 0; i < 26; i++) {
            final int num = i;
            letters[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (letters[num].getText().equals("-") || !isNotHint(num)) return;
                    char[] alphabet = cipher.getCipherAlphabet();
                    for (int i = 0; i < 26; i++)
                        if (alphabet[i] == (char) (num + Cipher.UPPER_CASE_START)) {
                            cipherLetters[i].setText(alphabet[i] + "");
                            changeHint(num, i);
                            app.getDataEditor().putString("cipherLetter" + i, (char) (num + Cipher.LOWER_CASE_START) + "").apply();
                        }
                    resetLetterClicks();
                    final TextView chooseLetter = getView(R.id.chooseLetterText);
                    chooseLetter.setText(R.string.pickLetter);
                    chooseLetter.setVisibility(View.VISIBLE);
                    chooseLetter.startAnimation(ViewHelper.fadeAnimation(chooseLetter));
                }
            });
        }
    }

    public void reveal() {
        app.setState(MainActivity.INLEVELSTATE);
        char[] alphabet = cipher.getCipherAlphabet();
        for (int i = 0; i < 26; i++) cipherLetters[i].setText("" + alphabet[i]);
        curCipherText = cipher.getRegularText(textPack, level).toLowerCase();
        text.setText(Html.fromHtml(WHITE + curCipherText + FONT, 1));
        app.getDataEditor().putString("curCipherText", curCipherText);
        for (int i = 0; i < 26; i++) {
            letters[i].setOnClickListener(null);
            cipherLetters[i].setOnClickListener(null);
        }
    }

    private void resetLetterClicks() {
        for (Button button : letters) button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < 26; i++)
            if (view.getId() == letters[i].getId() && !letters[i].getText().equals("-")) {
                if (selectedLetter == i)
                    letters[selectedLetter].setBackgroundResource(R.drawable.circle);
                else {
                    if (letterSelected()) {
                        letters[selectedLetter].setBackgroundResource(R.drawable.circle);
                        changeTextColor(-1);
                    }
                    letters[i].setBackgroundResource(R.drawable.green_circle);
                }
                selectedLetter = (selectedLetter == i) ? -1 : i;
                changeTextColor(selectedLetter);
            }
        for (int i = 0; i < 26; i++)
            if (view.getId() == cipherLetters[i].getId() && !cipherLetters[i].getText().equals("-")) {
                if (letterSelected() && !hintsContainedIn(i) && isNotHint(selectedLetter)) {
                    if (isDifferentThanNormal(i))
                        removeLetter(i);
                    changeText(selectedLetter, i);
                    letters[selectedLetter].setBackgroundResource(R.drawable.circle);
                    cipherLetters[i].setText("" + (char) (selectedLetter + Cipher.UPPER_CASE_START));
                    for (int j = 0; j < cipherLetters.length; j++)
                        if (cipherLetters[j].getText().equals("" + (char) (selectedLetter + Cipher.UPPER_CASE_START)) && j != i) {
                            cipherLetters[j].setText("");
                            break;
                        }
                    selectedLetter = -1;
                    for (int j = 0; j < 26; j++) {
                        System.out.println(app.getData().getString("cipherLetter" + j, "_") + ", ");
                    }
                } else if (firstLetterOf(cipherLetters[i]) != '!' && app.getData().getString("cipherLetter" + i, "").equals("")) {
                    removeLetter(i);
                    cipherLetters[i].setText("");
                }
            }
    }

    private boolean letterSelected() { return selectedLetter != -1; }
    private boolean isDifferentThanNormal(int index) { return !cipherLetters[index].getText().equals(""); }
    private char firstLetterOf(Button button) { return (button.getText().length() > 0) ? button.getText().charAt(0) : '!'; }

    private String withoutHtml(String s) { return s.replace(FONT, "").replace(RED, "").replace(WHITE, ""); }
    private boolean hintsContainedIn(int index) {
        return !app.getData().getString("cipherLetter" + index, "").equals("");
    }
    private boolean isNotHint(int letter) {
        for (int i = 0; i < 26; i++)
            if (app.getData().getString("cipherLetter" + i, "").equals("" + (char) (letter + Cipher.LOWER_CASE_START)) || app.getData().getString("cipherLetter" + i, "").equals("" + (char) (letter + Cipher.UPPER_CASE_START)))
                return false;
        return true;
    }
    private void updateText() { text.setText(Html.fromHtml(curCipherText, 1)); }

    void setLevelNumber(int number) { level = number; }
    void setTextPack(int textPack) { this.textPack = textPack; }

}
