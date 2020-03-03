package com.example.ciphergame.GameState;

import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Handler;

import com.example.ciphergame.Hints;
import com.example.ciphergame.MainActivity;
import com.example.ciphergame.ViewHelper;
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
    private Cipher cipher;
    private String hintCipherText; // contains the hints only
    private String cipherText; // contains the original ciphered text
    private String curCipherText; // contains the current text

    public InLevelState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.in_level_state);

        ((AdView) getView(R.id.in_level_ad)).loadAd(new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());

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
        for (Button button : new Button[] { reset, hint, lives }) button.setBackgroundColor(Color.TRANSPARENT);
        Button checkAnswer = getView(R.id.checkAnswer);
        checkAnswer.setBackgroundColor(Color.TRANSPARENT);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });

        cipher = MainActivity.getCipher();
        cipherText = withoutHtml(app.getData().getString("cipherText", cipher.getText(app.getData().getInt("textPack", -1), level)));
        app.getDataEditor().putString("cipherText", cipherText).apply();
        hintCipherText = app.getData().getString("hintCipherText", cipherText);
        curCipherText = app.getData().getString("curCipherText", cipherText);
        System.out.println(", " + cipher.getText(app.getData().getInt("textPack", -1), level));

        final double WIDTH = 92;
        ViewHelper.setWidth(getView(R.id.scrollViewTop), WIDTH);
        ViewHelper.setWidth(getView(R.id.scrollViewBottom), WIDTH);

        LinearLayout letterLayout = getView(R.id.topLine);
        TableRow rowTop = getView(R.id.bottomLineButtons);
        TableRow rowBottom = getView(R.id.bottomLineTexts);

        final double SIZE = 10;
        final double MARGIN = 1.5;
        final double PADDING = 0.05;
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams((int) (ViewHelper.percentWidth(SIZE)), (int) ViewHelper.percentWidth(SIZE));
        linearLayoutParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);
        
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams((int) (ViewHelper.percentWidth(SIZE)), (int) ViewHelper.percentWidth(SIZE));
        tableRowParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);

        letters = new Button[26];
        for (int i = 0; i < 26; i++) {
            letters[i] = new Button(app);
            letters[i].setId(View.generateViewId());
            letters[i].setOnClickListener(this);
            String text = "" + (char) (i + Cipher.UPPER_CASE_START);
            letters[i].setText(text);
            letters[i].setBackgroundResource(R.drawable.circle);
            letters[i].setLayoutParams(linearLayoutParams);
            letters[i].setPadding(0, 0, 0, (int) ViewHelper.percentWidth(PADDING));
            letterLayout.addView(letters[i]);
        }

        cipherLetters = new Button[26];
        for (int i = 0; i < 26; i++) {
            cipherLetters[i] = new Button(app);
            cipherLetters[i].setId(View.generateViewId());
            cipherLetters[i].setOnClickListener(this);
            cipherLetters[i].setBackgroundResource(R.drawable.circle);
            cipherLetters[i].setLayoutParams(tableRowParams);
            cipherLetters[i].setPadding(0, 0, 0, (int) ViewHelper.percentWidth(PADDING));
            rowTop.addView(cipherLetters[i]);
        }
        
        TextView[] texts = new TextView[26];
        for (int i = 0; i < 26; i++) {
            texts[i] = new TextView(app);
            texts[i].setLayoutParams(tableRowParams);
            String text = "" + (char) (i + Cipher.LOWER_CASE_START);
            texts[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            texts[i].setText(text);
            rowBottom.addView(texts[i]);
        }

        resetCipherLetters();

        resetText();
        text = getView(R.id.in_level_text);
        updateText();
        text.setTextSize(cipherText.length() >= 250 ? (float) (20 - ((cipherText.length() - 250) / 50.0)) : 20);

        addHomeButton();
        addVolumeButton();
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
        String regularText = cipher.getRegularText(app.getData().getInt("textPack", -1), level).toUpperCase();
        char[] alphabet = cipher.getCipherAlphabet();
        for (int i = 0; i < 26; i++)
            cipherLetters[i].setText(regularText.contains("" + (alphabet[i])) ? app.getData().getString("cipherLetter" + i, "") : "-");
    }

    private void resetText() {
        String regularText = cipher.getRegularText(app.getData().getInt("textPack", -1), level).toUpperCase();
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
        if (withoutHtml(curCipherText).equals(cipher.getRegularText(app.getData().getInt("textPack", -1), level).toLowerCase()) || withoutHtml(text.getText().toString()).equals("You won")) {
            text.setText(Html.fromHtml(WHITE + "You won" + FONT, 1));
            app.levelReset();
            level++;
            if (level == LevelState.NUM_BUTTONS) app.setState(MainActivity.LEVELSTATE);
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
        int id = view.getId();
        for (int i = 0; i < 26; i++)
            if (id == letters[i].getId() && !letters[i].getText().equals("-")) {
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
            if (id == cipherLetters[i].getId() && !cipherLetters[i].getText().equals("-"))
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