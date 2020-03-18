package com.example.ciphergame.GameState;

import android.graphics.Color;
import android.text.Html;
import android.util.Log;
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

public class InLevelState extends GameState {

    private static final String WHITE = "<font color=#FFFFFF>";
    private static final String RED = "<font color=#FF0000>";
    private static final String FONT = "</font>";

    private TextView text;
    private Button[] topLetters, bottomLetters;
    private int selectedLetter = -1;

    private int level;
    private Cipher cipher;
    private String hintCipherText; // contains the hints only
    private String cipherText; // contains the original ciphered text
    private String curCipherText; // contains the current text
    private String regularText;

    public InLevelState(MainActivity app) { super(app); }

    public void init() {
        setContentView(R.layout.in_level_state);

        // load ad on adview
        ((AdView) getView(R.id.in_level_ad)).loadAd(new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());

        // reset and hint buttons
        Button reset = getView(R.id.reset_answer);
        Button hint = getView(R.id.hint_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { resetAnswer(); }
        });
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { app.setState(MainActivity.PURCHASE); }
        });
        reset.setBackgroundColor(Color.TRANSPARENT);
        hint.setBackgroundColor(Color.TRANSPARENT);

        Button checkAnswer = getView(R.id.checkAnswer);
        checkAnswer.setBackgroundColor(Color.TRANSPARENT);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { checkAnswer(); }
        });

        cipher = MainActivity.getCipher();

        cipherText = withoutHtml(app.getData().getString("cipherText", cipher.getText(app.getData().getInt("textPack", -1), level)));
        app.getDataEditor().putString("cipherText", cipherText).apply();

        hintCipherText = app.getData().getString("hintCipherText", cipherText);
        curCipherText = app.getData().getString("curCipherText", cipherText);
        regularText = cipher.getRegularText(app.getData().getInt("textPack", -1), level).toUpperCase();

        final double WIDTH = 92;
        ViewHelper.setWidth(getView(R.id.scrollViewTop), WIDTH);
        ViewHelper.setWidth(getView(R.id.scrollViewBottom), WIDTH);

        LinearLayout letterLayout = getView(R.id.topLine);
        TableRow rowTop = getView(R.id.bottomLineButtons);
        TableRow rowBottom = getView(R.id.bottomLineTexts);

        final double SIZE = 10;
        final double MARGIN = 1.5;
        final double PADDING = 0.05;
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        linearLayoutParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);
        
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        tableRowParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);

        topLetters = new Button[26];
        for (int i = 0; i < 26; i++) {
            topLetters[i] = new Button(app);
            topLetters[i].setId(View.generateViewId());
            topLetters[i].setOnClickListener(topClick);
            String text = "" + (char) (i + Cipher.UPPER_CASE_START);
            topLetters[i].setText(text);
            topLetters[i].setBackgroundResource(R.drawable.circle);
            topLetters[i].setLayoutParams(linearLayoutParams);
            topLetters[i].setPadding(0, 0, 0, (int) ViewHelper.percentWidth(PADDING));
            letterLayout.addView(topLetters[i]);
        }

        bottomLetters = new Button[26];
        for (int i = 0; i < 26; i++) {
            bottomLetters[i] = new Button(app);
            bottomLetters[i].setId(View.generateViewId());
            bottomLetters[i].setOnClickListener(bottomClick);
            bottomLetters[i].setBackgroundResource(R.drawable.circle);
            bottomLetters[i].setLayoutParams(tableRowParams);
            bottomLetters[i].setPadding(0, 0, 0, (int) ViewHelper.percentWidth(PADDING));
            rowTop.addView(bottomLetters[i]);
        }
        
        for (int i = 0; i < 26; i++) {
            TextView textView = new TextView(app);
            textView.setLayoutParams(tableRowParams);
            textView.setTextColor(Color.WHITE);
            String text = "" + (char) (i + Cipher.LOWER_CASE_START);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(text);
            rowBottom.addView(textView);
        }

        resetBottomLetters();

        resetText();
        text = getView(R.id.in_level_text);
        updateText();
        text.setTextSize(cipherText.length() >= 250 ? (float) (20 - ((cipherText.length() - 250) / 50.0)) : 20);

        addHomeButton();
        addVolumeButton();
    }

    private void resetAnswer() {

        // reset background of current selected letter
        if (topLetterSelected()) {
            topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
            selectedLetter = -1;
        }

        // reset the cipher text to leave only the hints
        curCipherText = hintCipherText;
        app.getDataEditor().putString("curCipherText", curCipherText).apply();

        // reset curBottomLetters
        for (int i = 0; i < 26; i++)
            app.getDataEditor().putString("curBottomLetter" + i, "");
        app.getDataEditor().apply();

        // reset bottom letters and update the text
        resetBottomLetters();
        updateText();
    }

    private void resetBottomLetters() {

        // resets bottom letters so that they are blank unless filled in with a hint
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i = 0; i < 26; i++)
            // if the regular text doesn't contain a letter, it gets a dash, otherwise blank/hint
            if (!regularText.contains("" + alphabet[i]))
                bottomLetters[i].setText("-");
            else
                bottomLetters[i].setText(app.getData().getString("cipherLetter" + i, app.getData().getString("curBottomLetter" + i, "")));
    }

    private void resetText() {
        int color = app.getApplicationContext().getColor(R.color.opaqueBlack);
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        addButtonListeners();

        // reset letters
        for (int i = 0; i < 26; i++) {
            topLetters[i].setBackgroundResource(R.drawable.circle);
            topLetters[i].setText(cipherText.contains("" + (char) (i + Cipher.UPPER_CASE_START)) ? "" + (char) (i + Cipher.UPPER_CASE_START) : "-");
        }

        // reset bottomLetters
        for (int i = 0; i < 26; i++) {
            bottomLetters[i].setBackgroundResource(R.drawable.circle);
            bottomLetters[i].setTextColor(color);
//            if (!regularText.contains("" + alphabet[i]) && !hintCipherText.contains("" + alphabet[i])) bottomLetters[i].setText("-");
            if (!regularText.contains("" + alphabet[i]))
                bottomLetters[i].setText("-");
            else
                bottomLetters[i].setText(app.getData().getString("cipherLetter" + i, app.getData().getString("curBottomLetter" + i, "")));
        }
        resetBottomLetters();
    }

    private void checkAnswer() {
        selectedLetter = -1;
        for (Button button : topLetters) button.setBackgroundResource(R.drawable.circle);
        if (withoutHtml(curCipherText).equals(regularText.toLowerCase()) || withoutHtml(text.getText().toString()).equals("You won")) {
            text.setText(Html.fromHtml(WHITE + "You won" + FONT, 1));
            app.levelReset();
            level++;
            if (level == LevelState.NUM_BUTTONS) app.setState(MainActivity.LEVELSTATE);
            else {
                removeButtonListeners();
                for (int i = 0; i < 26; i++) {
                    topLetters[i].setBackgroundResource(R.drawable.green_circle);
                    bottomLetters[i].setBackgroundResource(R.drawable.green_circle);
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
            topLetters[i].setBackgroundResource(R.drawable.red_circle);
            bottomLetters[i].setBackgroundResource(R.drawable.red_circle);
        }
        removeButtonListeners();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 26; i++) {
                    topLetters[i].setBackgroundResource(R.drawable.circle);
                    bottomLetters[i].setBackgroundResource(R.drawable.circle);
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
            curCipherText = curCipherText.replace(WHITE + (char) (index + Cipher.LOWER_CASE_START) + FONT, bottomLetters[index].getText());
            app.getDataEditor().putString("curCipherText", curCipherText).apply();
            updateText();
        }
    }

    private View.OnClickListener topClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            for (int i = 0; i < 26; i++)
                if (id == topLetters[i].getId() && !topLetters[i].getText().equals("-")) {

                    if (selectedLetter == i)
                        topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
                    else {
                        if (topLetterSelected()) {
                            topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
                            changeTextColor(-1);
                        }
                        topLetters[i].setBackgroundResource(R.drawable.green_circle);
                    }
                    selectedLetter = (selectedLetter == i) ? -1 : i;
                    changeTextColor(selectedLetter);
                }
        }
    };

    private View.OnClickListener bottomClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            // check for bottom letters
            for (int i = 0; i < 26; i++)
                if (id == bottomLetters[i].getId() && !bottomLetters[i].getText().equals("-"))

                    if (topLetterSelected() && !hintsContainedIn(i) && isNotHint(selectedLetter)) {
                        app.getDataEditor().putString("curBottomLetter" + i, (char) i + "").apply();
                        if (isDifferentThanNormal(i))
                            removeLetter(i);
                        changeText(selectedLetter, i);
                        topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
                        String text = "" + (char) (selectedLetter + Cipher.UPPER_CASE_START);
                        bottomLetters[i].setText(text);
                        for (int j = 0; j < bottomLetters.length; j++)
                            if (bottomLetters[j].getText().equals("" + (char) (selectedLetter + Cipher.UPPER_CASE_START)) && j != i) {
                                bottomLetters[j].setText("");
                                break;
                            }
                        selectedLetter = -1;
                    } else if (firstLetterOf(bottomLetters[i]) != '!' && app.getData().getString("cipherLetter" + i, "").equals("")) {
                        removeLetter(i);
                        bottomLetters[i].setText("");
                    }
        }
    };

//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//
//        for (int i = 0; i < 26; i++)
//            if (id == topLetters[i].getId() && !topLetters[i].getText().equals("-")) {
//
//                if (selectedLetter == i)
//                    topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
//                else {
//                    if (topLetterSelected()) {
//                        topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
//                        changeTextColor(-1);
//                    }
//                    topLetters[i].setBackgroundResource(R.drawable.green_circle);
//                }
//                selectedLetter = (selectedLetter == i) ? -1 : i;
//                changeTextColor(selectedLetter);
//            }
//
//        // check for bottom letters
//        for (int i = 0; i < 26; i++)
//            if (id == bottomLetters[i].getId() && !bottomLetters[i].getText().equals("-"))
//
//                if (topLetterSelected() && !hintsContainedIn(i) && isNotHint(selectedLetter)) {
//                    app.getDataEditor().putString("curBottomLetter" + i, (char) i + "").apply();
//                    if (isDifferentThanNormal(i))
//                        removeLetter(i);
//                    changeText(selectedLetter, i);
//                    topLetters[selectedLetter].setBackgroundResource(R.drawable.circle);
//                    String text = "" + (char) (selectedLetter + Cipher.UPPER_CASE_START);
//                    bottomLetters[i].setText(text);
//                    for (int j = 0; j < bottomLetters.length; j++)
//                        if (bottomLetters[j].getText().equals("" + (char) (selectedLetter + Cipher.UPPER_CASE_START)) && j != i) {
//                            bottomLetters[j].setText("");
//                            break;
//                        }
//                    selectedLetter = -1;
//                } else if (firstLetterOf(bottomLetters[i]) != '!' && app.getData().getString("cipherLetter" + i, "").equals("")) {
//                    removeLetter(i);
//                    bottomLetters[i].setText("");
//                }
//    }

    public void peek() {

        // create variables used to check letters and bottomLetters
        char[] cipherAlphabet = cipher.getCipherAlphabet();
        char[] curCipherAlphabet = getCurAlphabet();
        int randChoice = (int) Math.round(Math.random() * 25);
        boolean[] canCorrect = new boolean[26];

        Log.d("Bottom Letters", String.copyValueOf(cipherAlphabet));
        Log.d("Current Bottom Letters", String.copyValueOf(curCipherAlphabet));

        // make sure a hint is available
        if (!canUseHint()) {
            MainActivity.getCurrencies().addCoins(Hints.PEEK_COST);
            return;
        }

        // checks which letters can be used as hints
        boolean req1, req2, req3;
        for (int i = 0; i < 26; i++) {
            req1 = cipherAlphabet[i] != curCipherAlphabet[i];
            req2 = firstLetterOf(bottomLetters[i]) != '-';
            req3 = topLettersContain(i);

            canCorrect[i] = req1 && req2 && req3;
        }

        // pick a random letter to switch
        do {
            randChoice++;
            if (randChoice == 25)
                randChoice = 0;
        } while (!canCorrect[randChoice]);

        Log.d("First Letter of Top Letter", firstLetterOf(topLetters[randChoice]) + "");

        // change the text of the changed cipherLetter
        String text = "" + cipherAlphabet[randChoice];
        bottomLetters[randChoice].setText(text);

        // change the text in the form of a hint
        changeHint(cipherAlphabet[randChoice] - Cipher.UPPER_CASE_START, randChoice);
        app.getDataEditor().putString("cipherLetter" + randChoice, "" + cipherAlphabet[randChoice]).apply();

        Log.d("OldLetter", cipherAlphabet[randChoice] + "");
        Log.d("NewLetter", randChoice + "");

        // bring game back to the inlevelstate and reset the bottom letters
        app.setState(MainActivity.INLEVELSTATE);
        resetBottomLetters();

        // variables used in scrolling to the letters changed during peek
        final double SCROLL_END = 232.75;
        final int choice = randChoice;
        final int letter = cipherAlphabet[randChoice] - Cipher.UPPER_CASE_START;

        // both scroll views scroll to the letters that were changed

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
                topLetters[letter].setBackgroundResource(R.drawable.green_circle);
                bottomLetters[choice].setBackgroundResource(R.drawable.green_circle);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        topLetters[letter].setBackgroundResource(R.drawable.circle);
                        bottomLetters[choice].setBackgroundResource(R.drawable.circle);
                    }
                }, 375);
            }
        }, 250);
    }

    public void choose() {

        // check if the hint can be used
        if (!canUseHint()) {
            MainActivity.getCurrencies().addCoins(Hints.CHOOSE_COST);
            return;
        }

        // change to inlevelstate and reset bottom letters
        app.setState(MainActivity.INLEVELSTATE);
        resetBottomLetters();

        // make a text view prompting user to select a letter to be solved
        TextView chooseLetter = getView(R.id.chooseLetterText);
        chooseLetter.setText(R.string.pickLetter);
        chooseLetter.setVisibility(View.VISIBLE);

        // on click listener for when they're click on to be solved
        for (Button letter : topLetters)
            letter.setOnClickListener(hintClick);
    }
    
    private View.OnClickListener hintClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            for (int i = 0; i < 26; i++)
                if (id == topLetters[i].getId()) {
                    if (topLetters[i].getText().equals("-") || !isNotHint(i)) return;
                    char[] alphabet = cipher.getCipherAlphabet();
                    for (int j = 0; j < 26; j++)
                        if (alphabet[j] == (char) (i + Cipher.UPPER_CASE_START)) {
                            String text = alphabet[j] + "";
                            bottomLetters[j].setText(text);
                            changeHint(i, j);
                            app.getDataEditor().putString("cipherLetter" + j, (char) (i + Cipher.LOWER_CASE_START) + "").apply();
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
        }
    };

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
        resetBottomLetters();
    }

    // removes the onclicklisteners from buttons
    private void removeButtonListeners() {
        for (int i = 0; i < 26; i++) {
            bottomLetters[i].setOnClickListener(null);
            topLetters[i].setOnClickListener(null);
        }
    }
    // adds onclicklisteners back
    private void addButtonListeners() {
        for (int i = 0; i < 26; i++) {
            bottomLetters[i].setOnClickListener(bottomClick);
            topLetters[i].setOnClickListener(topClick);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canUseHint() {
        // checks bottom letters
        for (Button letter : bottomLetters)
            if (firstLetterOf(letter) == '!')
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

    private boolean topLetterSelected() { return selectedLetter != -1; }
    private boolean isDifferentThanNormal(int index) { return !bottomLetters[index].getText().equals(""); }
    private char firstLetterOf(Button button) {
        return button.getText().length() > 0 ? button.getText().charAt(0) : '!'; }

    private String withoutHtml(String s) { return s.replace(FONT, "").replace(RED, "").replace(WHITE, ""); }
    private boolean hintsContainedIn(int index) {
        return !app.getData().getString("cipherLetter" + index, "").equals("");
    }
    private boolean isNotHint(int letter) {
        for (int i = 0; i < 26; i++)
            if (app.getData().getString("cipherLetter" + i, "").equals("" + (char) (letter + Cipher.LOWER_CASE_START)) ||
                    app.getData().getString("cipherLetter" + i, "").equals("" + (char) (letter + Cipher.UPPER_CASE_START)))
                return false;
        return true;
    }
    private void updateText() { text.setText(Html.fromHtml(curCipherText, 0)); }

    private char[] getCurAlphabet() {
        char[] alphabet = new char[26];
        for (int i = 0; i < bottomLetters.length; i++)
            alphabet[i] = Character.toUpperCase(firstLetterOf(bottomLetters[i]));
        return alphabet;
    }
    private boolean topLettersContain(int letter) {
        for (int i = 0; i < 26; i++)
            if (topLetters[i].getText().equals("" + (char) (letter + Cipher.UPPER_CASE_START)))
                return true;
        return false;
    }

}