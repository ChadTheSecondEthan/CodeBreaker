package com.example.ciphergame.GameState;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.os.Handler;

import com.example.ciphergame.Hints;
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
        ((BackButton) getView(R.id.back_button)).init(app);
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
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
        });
        lives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
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

        cipher = MainActivity.getCipher();
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
        for (int i = 0; i < 26; i++) {
            ViewHelper.setPaddingBottom(letters[i], 0.25);
            ViewHelper.setWidthAsPercentOfScreen(letters[i], 10);
            ViewHelper.makeSquareWithWidth(letters[i]);
            ViewHelper.setMarginRight(letters[i], 2.5);
            ViewHelper.setPaddingBottom(cipherLetters[i], 0.25);
            ViewHelper.setWidthAsPercentOfScreen(cipherLetters[i], 10);
            ViewHelper.makeSquareWithWidth(cipherLetters[i]);
            ViewHelper.setMarginRight(cipherLetters[i], 2.5);
        }
        TextView[] smallTexts = app.getTextViews(new int[] { R.id.a_small_text, R.id.b_small_text,
                R.id.c_small_text, R.id.d_small_text, R.id.e_small_text, R.id.f_small_text, R.id.g_small_text,
                R.id.h_small_text, R.id.i_small_text, R.id.j_small_text, R.id.k_small_text, R.id.l_small_text,
                R.id.m_small_text, R.id.n_small_text, R.id.o_small_text, R.id.p_small_text, R.id.q_small_text,
                R.id.r_small_text, R.id.s_small_text, R.id.t_small_text, R.id.u_small_text, R.id.v_small_text,
                R.id.w_small_text, R.id.x_small_text, R.id.y_small_text, R.id.z_small_text });
        for (int i = 0; i < 26; i++) {
            ViewHelper.setWidthAsPercentOfScreen(smallTexts[i], 10);
            ViewHelper.makeSquareWithWidth(smallTexts[i]);
            ViewHelper.setMarginRight(smallTexts[i], 2.5);
            String text = "" + (char) (i + Cipher.LOWER_CASE_START);
            smallTexts[i].setText(text);
        }
        ViewHelper.setMarginLeftAndRight(letters[0], 2.5);
        ViewHelper.setMarginLeftAndRight(cipherLetters[0], 2.5);
        ViewHelper.setMarginLeftAndRight(getView(R.id.a_small_text), 2.5);
        resetCipherLetters();

        resetText();
        text = getView(R.id.in_level_text);
        updateText();
        text.setTextSize(cipherText.length() >= 250 ? (float) (20 - ((cipherText.length() - 250) / 50.0)) : 20);
    }

    private void resetAnswer() {
        if (letterSelected()) {
            letters[selectedLetter].setBackgroundResource(R.drawable.circle);
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

    private void resetText() {
        String regularText = cipher.getRegularText(textPack, level).toUpperCase();
        int color = app.getApplicationContext().getColor(R.color.opaqueBlack);
        char[] alphabet = cipher.getCipherAlphabet();
        addButtonListeners();
        // reset letters
        for (int i = 0; i < 26; i++) {
            letters[i].setBackgroundResource(R.drawable.circle);
            letters[i].setText(cipherText.contains("" + (char) (i + Cipher.UPPER_CASE_START)) ? "" + (char) (i + Cipher.UPPER_CASE_START) : "-");
        }
        // reset cipherLetters
        for (int i = 0; i < 26; i++) {
            cipherLetters[i].setBackgroundResource(R.drawable.circle);
            cipherLetters[i].setTextColor(color);
            if (!(regularText.contains("" + alphabet[i]) && hintCipherText.contains("" + alphabet[i]))) cipherLetters[i].setText("-");
        }
    }

    private void checkAnswer() {
        selectedLetter = -1;
        for (Button button : letters) button.setBackgroundResource(R.drawable.circle);
        if (withoutHtml(curCipherText).equals(cipher.getRegularText(textPack, level).toLowerCase()) || withoutHtml(text.getText().toString()).equals("You won")) {
            text.setText(Html.fromHtml(WHITE + "You won" + FONT, 1));
            app.levelReset();
            level++;
            if (level == 100) app.setState(MainActivity.LEVELSTATE);
            else {
                removeButtonListeners();
                for (int i = 0; i < 26; i++) {
                    letters[i].setBackgroundResource(R.drawable.green_circle);
                    cipherLetters[i].setBackgroundResource(R.drawable.green_circle);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { app.setState(MainActivity.INLEVELSTATE); }
                }, 1000);
            }
        } else wrongAnswerAnimation();
    }

    private void wrongAnswerAnimation() {
        for (int i = 0; i < 26; i++) {
            letters[i].setBackgroundResource(R.drawable.red_circle);
            cipherLetters[i].setBackgroundResource(R.drawable.red_circle);
        }
        removeButtonListeners();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 26; i++) {
                    letters[i].setBackgroundResource(R.drawable.circle);
                    cipherLetters[i].setBackgroundResource(R.drawable.circle);
                }
            }
        }, 500);
        addButtonListeners();
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

    private void changeHint(int oldLetter, int newLetter) {
        changeText(oldLetter, newLetter);
        hintCipherText = hintCipherText.replace(WHITE, "*").replace(FONT, "@")
                .replace("*" + (char) (oldLetter + Cipher.UPPER_CASE_START) + "@", "" + (char) (oldLetter + Cipher.UPPER_CASE_START))
                .replace("" + (char) (oldLetter + Cipher.UPPER_CASE_START), "*" + (char) (newLetter + Cipher.LOWER_CASE_START) + "@")
                .replace("*", WHITE).replace("@", FONT);
        app.getDataEditor().putString("hintCipherText", hintCipherText).apply();
    }

    private void removeLetter(int index) {
        if (isNotHint(index)) {
            curCipherText = curCipherText.replace(WHITE + (char) (index + Cipher.LOWER_CASE_START) + FONT, cipherLetters[index].getText());
            app.getDataEditor().putString("curCipherText", curCipherText).apply();
            updateText();
        }
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < 26; i++)
            if (view.getId() == letters[i].getId() && !letters[i].getText().equals("-")) {
                if (selectedLetter == i) letters[selectedLetter].setBackgroundResource(R.drawable.circle);
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
                    String text = "" + (char) (selectedLetter + Cipher.UPPER_CASE_START);
                    cipherLetters[i].setText(text);
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

        if (!canUseHint()) {
            MainActivity.getCurrencies().addCoins(Hints.PEEK_COST);
            return;
        }

        for (int i = 0; i < 26; i++)
            canCorrect[i] = cipherAlphabet[i] != curCipherAlphabet[i] && firstLetterOf(cipherLetters[i]) != '-' && lettersContain(i);

        // pick a random letter to switch
        do randChoice = (int) (Math.random() * 25);
        while (!canCorrect[randChoice]);

        String text = "" + cipherAlphabet[randChoice];
        cipherLetters[randChoice].setText(text);
        changeHint(cipherAlphabet[randChoice] - Cipher.UPPER_CASE_START, randChoice);
        app.getDataEditor().putString("cipherLetter" + randChoice, "" + cipherAlphabet[randChoice]).apply();
        app.setState(MainActivity.INLEVELSTATE);
        resetCipherLetters();

        final double SCROLL_END = 232.75;
        final int choice = randChoice;
        final int letter = cipherAlphabet[randChoice] - Cipher.UPPER_CASE_START;

        // top goes to cipheralphabet[randchoice]
        final HorizontalScrollView top = getView(R.id.scrollViewTop), bottom = getView(R.id.scrollViewBottom);
        top.post(new Runnable() {
            @Override
            public void run() {
                if (letter > 21) top.smoothScrollTo((int) ViewHelper.percentWidth(SCROLL_END), 0);
                else if (letter < 4) top.smoothScrollTo(0, 0);
                else top.smoothScrollTo((int) ViewHelper.percentWidth(letter * 12.5 - 39), 0);
            }
        });
        // bottom goes to randchoice
        bottom.post(new Runnable() {
            @Override
            public void run() {
                if (choice > 21) bottom.smoothScrollTo((int) ViewHelper.percentWidth(SCROLL_END), 0);
                else if (choice < 4) bottom.smoothScrollTo(0, 0);
                else bottom.smoothScrollTo((int) ViewHelper.percentWidth(choice * 12.5 - 39), 0);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                letters[letter].setBackgroundResource(R.drawable.green_circle);
                cipherLetters[choice].setBackgroundResource(R.drawable.green_circle);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        letters[letter].setBackgroundResource(R.drawable.circle);
                        cipherLetters[choice].setBackgroundResource(R.drawable.circle);
                    }
                }, 375);
            }
        }, 250);
    }

    public void choose() {
        app.setState(MainActivity.INLEVELSTATE);
        resetCipherLetters();

        if (!canUseHint()) {
            MainActivity.getCurrencies().addCoins(Hints.CHOOSE_COST);
            return;
        }

        TextView chooseLetter = getView(R.id.chooseLetterText);
        chooseLetter.setText(R.string.pickLetter);
        chooseLetter.setVisibility(View.VISIBLE);
        for (int i = 0; i < 26; i++) {
            final int num = i;
            letters[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (letters[num].getText().equals("-") || !isNotHint(num)) return;
                    char[] alphabet = cipher.getCipherAlphabet();
                    for (int i = 0; i < 26; i++)
                        if (alphabet[i] == (char) (num + Cipher.UPPER_CASE_START)) {
                            String text = alphabet[i] + "";
                            cipherLetters[i].setText(text);
                            changeHint(num, i);
                            app.getDataEditor().putString("cipherLetter" + i, (char) (num + Cipher.LOWER_CASE_START) + "").apply();
                        }
                    addButtonListeners();
                    getView(R.id.chooseLetterText).startAnimation(ViewHelper.fadeOutAnimation(getView(R.id.chooseLetterText)));
                    final HorizontalScrollView scrollView = getView(R.id.scrollViewBottom);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo((int) ViewHelper.percentWidth(12.5 * 26), 0);
                        }
                    });
                }
            });
        }
    }

    public void reveal() {
        if (!canUseHint()) {
            MainActivity.getCurrencies().addCoins(Hints.REVEAL_COST);
            return;
        }

        app.setState(MainActivity.INLEVELSTATE);
        char[] alphabet = cipher.getCipherAlphabet();
        for (int i = 0; i < 26; i++) {
            changeHint(alphabet[i] - Cipher.UPPER_CASE_START, i);
            app.getDataEditor().putString("cipherLetter" + i, "" + alphabet[i]).apply();
        }
        resetCipherLetters();
    }

    private void removeButtonListeners() {
        for (int i = 0; i < 26; i++) {
            cipherLetters[i].setOnClickListener(null);
            letters[i].setOnClickListener(null);
        }
    }
    private void addButtonListeners() {
        for (int i = 0; i < 26; i++) {
            cipherLetters[i].setOnClickListener(this);
            letters[i].setOnClickListener(this);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canUseHint() {
        for (Button letter : cipherLetters)
            if (letter.getText().toString().equals(""))
                return true;

        app.setState(MainActivity.INLEVELSTATE);
        String noUse = "<br><br>Cannot use hint. No avialable letters to switch.";
        text.setText(Html.fromHtml(noUse, 0));
        removeButtonListeners();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateText();
                addButtonListeners();
            }
        }, 1000);
        return false;
    }

    private boolean letterSelected() { return selectedLetter != -1; }
    private boolean isDifferentThanNormal(int index) { return !cipherLetters[index].getText().equals(""); }
    private char firstLetterOf(Button button) { return button.getText().length() > 0 ? button.getText().charAt(0) : '!'; }

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
    private void updateText() { text.setText(Html.fromHtml(curCipherText, 0)); }

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