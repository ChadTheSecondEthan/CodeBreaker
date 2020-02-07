package com.example.ciphergame.GameState;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import com.example.ciphergame.MainActivity;
import com.example.ciphergame.Views.*;
import com.example.ciphergame.Cipher;
import com.example.ciphergame.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class InLevelState extends GameState implements View.OnClickListener {

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
    private String hintCipherText; // contains the hints only
    private String cipherText; // contains the original ciphered text
    private String curCipherText; // contains the current text

    private boolean instructionsOpen;

    public InLevelState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.in_level_state);

        ((AdView) getView(R.id.in_level_ad)).loadAd(new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());
        ((BackButton) getView(R.id.back_button)).init(app).setOnClickListener(new View.OnClickListener() {
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
            ViewHelper.setWidthAndHeightAsPercentOfScreen(button, 30, 7);
        }
        Button checkAnswer = getView(R.id.checkAnswer);
        checkAnswer.setBackgroundColor(Color.TRANSPARENT);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
        ViewHelper.setWidthAndHeightAsPercentOfScreen(checkAnswer, 90, 7);

        cipher = new Cipher(app.getApplicationContext());
        cipherText = withoutHtml(app.getData().getString("cipherText", cipher.getText(textPack, level)));
        app.getDataEditor().putString("cipherText", cipherText).apply();
        hintCipherText = app.getData().getString("hintCipherText", cipherText);
        curCipherText = app.getData().getString("curCipherText", cipherText);

        ViewHelper.setWidthAsPercentOfScreen(getView(R.id.scrollViewTop), 94);
        ViewHelper.setWidthAsPercentOfScreen(getView(R.id.scrollViewBottom), 94);

        letters = app.getButtons(new int[] { R.id.a, R.id.b, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g,
                R.id.h, R.id.i, R.id.j, R.id.k, R.id.l, R.id.m, R.id.n, R.id.o, R.id.p, R.id.q, R.id.r,
                R.id.s, R.id.t, R.id.u, R.id.v, R.id.w, R.id.x, R.id.y, R.id.z });
        cipherLetters = app.getButtons(new int[] { R.id.a_text, R.id.b_text, R.id.c_text, 
                R.id.d_text, R.id.e_text, R.id.f_text, R.id.g_text, R.id.h_text, R.id.i_text, 
                R.id.j_text, R.id.k_text, R.id.l_text, R.id.m_text, R.id.n_text, R.id.o_text, 
                R.id.p_text, R.id.q_text, R.id.r_text, R.id.s_text, R.id.t_text, R.id.u_text, 
                R.id.v_text, R.id.w_text, R.id.x_text, R.id.y_text, R.id.z_text });
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
        text.setTextSize(cipherText.length() >= 250 ? (float) (20 - ((cipherText.length() - 250) / 50.0)) : 20);

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
        selectedLetter = -1;
        for (Button button : letters) button.setBackgroundResource(R.drawable.circle);
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
        } else wrongAnswerAnimation();
    }

    private void wrongAnswerAnimation() {
        for (int i = 0; i < 26; i++) {
            letters[i].setBackgroundResource(R.drawable.red_circle);
            cipherLetters[i].setBackgroundResource(R.drawable.red_circle);
            letters[i].setOnClickListener(null);
            cipherLetters[i].setOnClickListener(null);
        }
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 26; i++) {
                    letters[i].setBackgroundResource(R.drawable.circle);
                    cipherLetters[i].setBackgroundResource(R.drawable.circle);
                }
            }
        }, 500);
        for (int i = 0; i < 26; i++) {
            letters[i].setOnClickListener(this);
            cipherLetters[i].setOnClickListener(this);
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
        char letter = 0;
        for (int i = 0; i < cipherText.length(); i++)
            if (cipherText.charAt(i) == (char) (oldLetter + Cipher.UPPER_CASE_START)) {
                letter = withoutHtml(curCipherText).charAt(i);
                break;
            }
        curCipherText = curCipherText.replace(WHITE, "*").replace(FONT, "@")
                .replace("*" + letter + "@", "" + letter).replace("" + letter,
                        WHITE + (char) (newLetter + Cipher.LOWER_CASE_START) + FONT)
                .replace("@", FONT).replace("*",  WHITE);
        updateText();
        app.getDataEditor().putString("curCipherText", curCipherText).apply();
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

    private void changeHint(int oldLetter, int newLetter) {
        changeText(oldLetter, newLetter);
        hintCipherText = hintCipherText.replace(WHITE + (char) (oldLetter + Cipher.UPPER_CASE_START) + FONT, "" + (char) (oldLetter + Cipher.UPPER_CASE_START))
                .replace("" + (char) (oldLetter + Cipher.UPPER_CASE_START), WHITE + (char) (newLetter + Cipher.LOWER_CASE_START) + FONT);
        app.getDataEditor().putString("hintCipherText", hintCipherText).apply();
    }

    private void resetLetterClicks() {
        for (Button button : letters) button.setOnClickListener(this);
        for (Button button : cipherLetters) button.setOnClickListener(this);
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
            if (view.getId() == cipherLetters[i].getId() && !cipherLetters[i].getText().equals("-"))
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
                } else if (firstLetterOf(cipherLetters[i]) != '!' && app.getData().getString("cipherLetter" + i, "").equals("")) {
                    removeLetter(i);
                    cipherLetters[i].setText("");
                }
    }

    public void peek() {
        char[] cipherAlphabet = cipher.getCipherAlphabet();
        char[] curCipherAlphabet = getCurAlphabet();
        int randChoice;
        boolean[] canCorrect = new boolean[26];

        for (int i = 0; i < 26; i++)
            canCorrect[i] = cipherAlphabet[i] == '!';
        boolean nonCorrectable = true;
        for (int i = 0; i < 26; i++)
            if (canCorrect[i]) {
                nonCorrectable = false;
                break;
            }
        if (nonCorrectable)
            for (int i = 0; i < 26; i++)
                canCorrect[i] = cipherAlphabet[i] != curCipherAlphabet[i] && firstLetterOf(cipherLetters[i]) != '-' && lettersContain(i);

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
        lettersSwitched.startAnimation(ViewHelper.fadeOutAnimation(lettersSwitched));
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
                    chooseLetter.startAnimation(ViewHelper.fadeOutAnimation(chooseLetter));
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

}
