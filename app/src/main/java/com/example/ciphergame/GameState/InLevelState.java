package com.example.ciphergame.GameState;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Handler;

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

    private Cipher cipher;
    private String hintCipherText; // contains the hints only
    private String cipherText; // contains the original ciphered text
    private String curCipherText; // contains the current text
    private String regularText; // contains the quote

    public InLevelState(MainActivity app) { super(app); }

    public void init() {

        // TODO update pictures and logo and gif
        setContentView(R.layout.in_level_state);

        final boolean won = app.getData().getBoolean(getString(GameState.LEVEL_WON, getTextPack(), getLevel()), false);

        // load ad on adview
        ((AdView) getView(R.id.in_level_ad)).loadAd(
                new AdRequest.Builder().addTestDevice("F7C1A666D29DEF8F4F05EED1EAC2E8E0").build());

        // reset and hint buttons
        Button reset = getView(R.id.reset_answer);
        Button hint = getView(R.id.hint_button);
        Button checkAnswer = getView(R.id.checkAnswer);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
                if (!won) resetAnswer();
            }
        });
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
                if (!won) app.setState(MainActivity.PURCHASE);
            }
        });
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
                if (!won) checkAnswer();
            }
        });
        reset.setBackgroundColor(Color.TRANSPARENT);
        hint.setBackgroundColor(Color.TRANSPARENT);
        checkAnswer.setBackgroundColor(Color.TRANSPARENT);

        cipher = MainActivity.getCipher();

        // get ciphertext either from the app if it's there, or from cipher object, then remember it
        cipherText = withoutHtml(app.getData().getString(getString(CIPHER_TEXT, getTextPack(), getLevel()), cipher.getText(getTextPack(), getLevel())));
        app.getDataEditor().putString(getString(CIPHER_TEXT, getTextPack(), getLevel()), cipherText).apply();

        hintCipherText = app.getData().getString(getString(HINT_CIPHER_TEXT, getTextPack(), getLevel()), cipherText);
        curCipherText = app.getData().getString(getString(CUR_CIPHER_TEXT, getTextPack(), getLevel()), cipherText);
        regularText = cipher.getRegularText(getTextPack(), getLevel()).toUpperCase();

        final double WIDTH = 92;
        ViewHelper.setWidth(getView(R.id.scrollViewTop), WIDTH);
        ViewHelper.setWidth(getView(R.id.scrollViewBottom), WIDTH);

        LinearLayout letterLayout = getView(R.id.topLine);
        TableRow rowTop = getView(R.id.bottomLineButtons);
        TableRow rowBottom = getView(R.id.bottomLineTexts);

        final double SIZE = 10;
        final double MARGIN = 1.5;
        final double PADDING = 0.05;
        LinearLayout.LayoutParams linearLayoutParams =
                new LinearLayout.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        linearLayoutParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);

        TableRow.LayoutParams tableRowParams =
                new TableRow.LayoutParams((int) ViewHelper.percentWidth(SIZE), (int) ViewHelper.percentWidth(SIZE));
        tableRowParams.setMargins((int) ViewHelper.percentWidth(MARGIN), 0, (int) ViewHelper.percentWidth(MARGIN), 0);

        // text color for the buttons
        int textColor = app.getResources().getColor(R.color.nearWhite, null);

        topLetters = new Button[26];
        for (int i = 0; i < 26; i++) {
            topLetters[i] = new Button(app);
            topLetters[i].setId(View.generateViewId());
            topLetters[i].setOnClickListener(topClick);
            String text = "" + (char) (i + Cipher.UPPER_CASE_START);
            topLetters[i].setText(text);
//            topLetters[i].setBackgroundResource(R.drawable.basic_rectangle);
            topLetters[i].setTextColor(textColor);
            topLetters[i].setLayoutParams(linearLayoutParams);
            topLetters[i].setPadding(0, 0, 0, (int) ViewHelper.percentWidth(PADDING));
            letterLayout.addView(topLetters[i]);
        }

        bottomLetters = new Button[26];
        for (int i = 0; i < 26; i++) {
            bottomLetters[i] = new Button(app);
            bottomLetters[i].setId(View.generateViewId());
            bottomLetters[i].setOnClickListener(bottomClick);
            bottomLetters[i].setTextColor(textColor);
            bottomLetters[i].setLayoutParams(tableRowParams);
            bottomLetters[i].setPadding(0, 0, 0, (int) ViewHelper.percentWidth(PADDING));
            rowTop.addView(bottomLetters[i]);
        }

        for (int i = 0; i < 26; i++) {
            TextView textView = new TextView(app);
            textView.setLayoutParams(tableRowParams);
            textView.setTextColor(textColor);
            String text = "" + (char) (i + Cipher.LOWER_CASE_START);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(text);
            rowBottom.addView(textView);
        }

        // TODO fix text size
        text = getView(R.id.in_level_text);
        text.setTextSize(cipherText.length() > 250 ? (float) (20 - (cipherText.length() - 250 / 5.0)) : 20);
        resetText();

//        if (!app.getData().getBoolean("level" + level + "getTextPack()" + getTextPack() + "complete", false)) {
        if (!app.getData().getBoolean(getString(LEVEL_WON, getTextPack(), getLevel()), false)) {
            updateText();
        } else {
            text.setText(Html.fromHtml(WHITE + regularText.toLowerCase() + FONT, 0));
            for (int i = 0; i < 26; i++) {
                topLetters[i].setText("-");
                bottomLetters[i].setText("-");
            }
            removeButtonListeners();
        }

        addHomeButton(MainActivity.LEVELSTATE);
        addVolumeButton();
    }

    private void resetAnswer() {
        
        int textPack = getTextPack(), level = getLevel();

        // reset background of current selected letter
        if (topLetterSelected()) {
            topLetters[selectedLetter].setBackgroundResource(R.drawable.basic_rectangle);
            selectedLetter = -1;
        }

        // checks if there is still progress done on the level
        checkProgress();

        // reset the cipher text to leave only the hints
        curCipherText = hintCipherText;
        app.getDataEditor().putString(getString(CUR_CIPHER_TEXT, textPack, level), curCipherText).apply();

        // if resetting loses all progress on level, its not in progress anymore
        if (hintCipherText.equals(cipherText))
            app.getDataEditor().putBoolean(getString(GameState.IN_PROGRESS, textPack, level), false);

        // reset curBottomLetters
        for (int i = 0; i < 26; i++)
            app.getDataEditor().remove(getStringForLetter(CUR_BOTTOM_LETTER, i, textPack, level));
        app.getDataEditor().apply();

        // reset bottom letters and update the text
        resetBottomLetters();
        updateText();
    }

    private void resetBottomLetters() {

        // TODO first set text to hint_bottom_letter, then cur_bottom_letter
        
        int textPack = getTextPack(), level = getLevel();

        // resets bottom letters so that they are blank unless filled in with a hint
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i = 0; i < 26; i++)

            // if the regular text doesn't contain a letter, it gets a dash, otherwise blank/hint
            if (!regularText.contains("" + alphabet[i]) || app.getData().getBoolean(getString(LEVEL_WON, textPack, level), false))
                bottomLetters[i].setText("-");
            else
                bottomLetters[i].setText(app.getData().getString(getStringForLetter(HINT_BOTTOM_LETTER, i, textPack, level),
                        app.getData().getString(getStringForLetter(CUR_BOTTOM_LETTER, i, textPack, level), "")));
    }

    private void resetText() {
        int color = app.getApplicationContext().getColor(R.color.nearWhite);

        // add button listeners back if the level hasn't been won
        if (!app.getData().getBoolean(getString(LEVEL_WON, getTextPack(), getLevel()), false))
            addButtonListeners();

        // reset topletters
        for (int i = 0; i < 26; i++) {
            topLetters[i].setBackgroundResource(R.drawable.basic_rectangle);
            topLetters[i].setText(cipherText.contains("" + (char) (i + Cipher.UPPER_CASE_START)) ? "" + (char) (i + Cipher.UPPER_CASE_START) : "-");
        }

        // reset bottomLetters
        for (int i = 0; i < 26; i++) {
            bottomLetters[i].setBackgroundResource(R.drawable.basic_rectangle);
            bottomLetters[i].setTextColor(color);
        }
        resetBottomLetters();
    }

    private void checkAnswer() {
        selectedLetter = -1;
        for (Button button : topLetters) button.setBackgroundResource(R.drawable.basic_rectangle);
        if (withoutHtml(curCipherText).equals(regularText.toLowerCase())) {
            // meaning they've won

            // the level is won
            app.getDataEditor().putBoolean(getString(LEVEL_WON, getTextPack(), getLevel()), true).apply();
            String winText = "You won!<br>Coins: " + app.addCoins(100);
            text.setText(Html.fromHtml(winText, 0));

            // go back to levelstate
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    app.setState(MainActivity.LEVELSTATE);
                }
            }, 1000);

//            if (getLevel() == LevelState.NUM_BUTTONS) app.setState(MainActivity.LEVELSTATE);
//            else {
//                removeButtonListeners();
//                for (int i = 0; i < 26; i++) {
//                    topLetters[i].setBackgroundResource(R.drawable.basic_rectangle_green);
//                    bottomLetters[i].setBackgroundResource(R.drawable.basic_rectangle_green);
//                }
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() { app.setState(MainActivity.INLEVELSTATE); }
//                }, 1000);
//
//                app.getDataEditor().remove(getString(CIPHER_TEXT, getTextPack(), getLevel())).apply();
//                app.getDataEditor().remove(getString(REGULAR_TEXT, getTextPack(), getLevel())).apply();
//                app.getDataEditor().remove(getString(HINT_CIPHER_TEXT, getTextPack(), getLevel())).apply();
//                app.getDataEditor().remove(getString(CUR_CIPHER_TEXT, getTextPack(), getLevel())).apply();
//                for (int i = 0; i < 26; i++) {
//                    app.getDataEditor().remove(getStringForLetter(TOP_LETTER, i, getTextPack(), getLevel())).apply();
//                    app.getDataEditor().remove(getStringForLetter(CUR_BOTTOM_LETTER, i, getTextPack(), getLevel())).apply();
//                }
//            }
        } else wrongAnswerAnimation();
    }

    private void wrongAnswerAnimation() {
        for (int i = 0; i < 26; i++) {
            topLetters[i].setBackgroundResource(R.drawable.basic_rectangle_red);
            bottomLetters[i].setBackgroundResource(R.drawable.basic_rectangle_red);
        }
        removeButtonListeners();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 26; i++) {
                    topLetters[i].setBackgroundResource(R.drawable.basic_rectangle);
                    bottomLetters[i].setBackgroundResource(R.drawable.basic_rectangle);
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

    private void changeText(int topLetter, int bottomLetter) {

        // finds what letter is currently in the place of the topletter
        char letter = 0;
        for (int i = 0; i < cipherText.length(); i++)
            if (cipherText.charAt(i) == (char) (topLetter + Cipher.UPPER_CASE_START)) {
                letter = withoutHtml(curCipherText).charAt(i);
                break;
            }

        // save bottom letter
        app.getDataEditor().putString(getStringForLetter(CUR_BOTTOM_LETTER, bottomLetter, getTextPack(), getLevel()), (char) (topLetter + Cipher.UPPER_CASE_START) + "");

        // switches all instances of the topletter with the bottomletter
        curCipherText = curCipherText.replace(WHITE, "*").replace(FONT, "@")
                .replace("*" + letter + "@", "" + letter).replace("" + letter,
                        WHITE + (char) (bottomLetter + Cipher.LOWER_CASE_START) + FONT)
                .replace("@", FONT).replace("*",  WHITE);

        // update text and save text
        updateText();
//        app.getDataEditor().putString("curCipherText", curCipherText).apply();
        app.getDataEditor().putString(getString(CUR_CIPHER_TEXT, getTextPack(), getLevel()), curCipherText).apply();

        // check progress
        checkProgress();
    }

    private void changeHint(int topLetter, int bottomLetter) {

        // first change the text
        changeText(topLetter, bottomLetter);

        // then change hintciphertext to contain the letter for the hint
        hintCipherText = hintCipherText.replace(WHITE, "*").replace(FONT, "@")
                .replace("*" + (char) (topLetter + Cipher.UPPER_CASE_START) + "@", "" + (char) (topLetter + Cipher.UPPER_CASE_START))
                .replace("" + (char) (topLetter + Cipher.UPPER_CASE_START), "*" + (char) (bottomLetter + Cipher.LOWER_CASE_START) + "@")
                .replace("*", WHITE).replace("@", FONT);
        app.getDataEditor().putString(getString(HINT_CIPHER_TEXT, getTextPack(), getLevel()), hintCipherText).apply();
        app.getDataEditor().putString(getStringForLetter(HINT_BOTTOM_LETTER, bottomLetter, getTextPack(), getLevel()), (char) (topLetter + Cipher.UPPER_CASE_START) + "").apply();
        app.getDataEditor().putString(getStringForLetter(TOP_LETTER, topLetter, getTextPack(), getLevel()), (char) (bottomLetter + Cipher.UPPER_CASE_START) + "").apply();
    }

    private void removeLetter(int bottomLetter) {

        // deselects a bottom letter only if it's not a hint
        if (!isBottomLetterHint(bottomLetter)) {
            curCipherText = curCipherText.replace(WHITE + (char) (bottomLetter + Cipher.LOWER_CASE_START) + FONT, bottomLetters[bottomLetter].getText());
//            app.getDataEditor().putString("curCipherText", curCipherText).apply();
            app.getDataEditor().putString(getString(CUR_CIPHER_TEXT, getTextPack(), getLevel()), curCipherText).apply();
            updateText();
        }
    }

    private View.OnClickListener topClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClick();
            if (app.getData().getBoolean(getString(GameState.LEVEL_WON, getTextPack(), getLevel()), false)) return;
            int id = view.getId();

            for (int i = 0; i < 26; i++)
                // only clicks if the top letter is available (not a dash)
                if (id == topLetters[i].getId() && !topLetters[i].getText().equals("-")) {

                    // if the clicked button is the selected one, deselect it
                    if (selectedLetter == i) {
                        topLetters[i].setBackgroundResource(R.drawable.basic_rectangle);
                        selectedLetter = -1;
                        changeTextColor(-1);
                    } else if (topLetterSelected()) {
                        topLetters[selectedLetter].setBackgroundResource(R.drawable.basic_rectangle);
                        topLetters[selectedLetter = i].setBackgroundResource(R.drawable.basic_rectangle_green);
                        changeTextColor(-1);
                        changeTextColor(i);
                    } else {
                        // meaning selectedLetter = -1
                        topLetters[selectedLetter = i].setBackgroundResource(R.drawable.basic_rectangle_green);
                        changeTextColor(i);
                    }
                }
        }
    };

    private View.OnClickListener bottomClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClick();
            if (app.getData().getBoolean(getString(GameState.LEVEL_WON, getTextPack(), getLevel()), false)) return;
            int id = view.getId();

            // check for bottom letters
            for (int i = 0; i < 26; i++)
                // TODO fix
                // if the bottom letter isn't a dash and a top letter is selected
                if (id == bottomLetters[i].getId() && !bottomLetters[i].getText().equals("-") && topLetterSelected())

                    // checks if the top letter is checked and the letter isn't a hint
                    if (topLetterSelected() && !isBottomLetterHint(i) && !isTopLetterHint(selectedLetter)) {

                        app.getDataEditor().putString(getStringForLetter(CUR_BOTTOM_LETTER, i, getTextPack(), getLevel()), (char) (i + Cipher.UPPER_CASE_START) + "").apply();

                        // if there's already a letter there, switch it back
                        if (isDifferentThanNormal(i))
                            removeLetter(i);

                        // change the text to this letter
                        changeText(selectedLetter, i);

                        // reset the background of the selected top letter
                        topLetters[selectedLetter].setBackgroundResource(R.drawable.basic_rectangle);

                        // find the other letter that is already being used and erase it
                        String text = "" + (char) (selectedLetter + Cipher.UPPER_CASE_START);
                        bottomLetters[i].setText(text);
                        for (int j = 0; j < bottomLetters.length; j++)
                            if (bottomLetters[j].getText().equals("" + (char) (selectedLetter + Cipher.UPPER_CASE_START)) && j != i) {
                                bottomLetters[j].setText("");
                                break;
                            }

                        // deselect the selected letter
                        selectedLetter = -1;

                    // otherwise if the bottom letter is simply being deselected
                    // if the button has a letter on it that isn't from a hint
                        // TODO fix
                    } else if (firstLetterOf(bottomLetters[i]) != '!' &&
                            app.getData().getString(getStringForLetter(HINT_BOTTOM_LETTER, i, getTextPack(), getLevel()), "").equals("")) {

                        // remove the letter and erase its contents
                        removeLetter(i);
                        bottomLetters[i].setText("");
                    }
        }
    };

    public boolean peek() {

        // make sure a hint is available
        if (!canUseHint())
            return false;

        // create variables used to check letters and bottomLetters
        char[] cipherAlphabet = cipher.getCipherAlphabet();
        char[] curCipherAlphabet = getCurAlphabet();
        boolean[] canCorrect = new boolean[26];

        // checks which letters can be used as hints
        boolean req1, req2;
        for (int i = 0; i < 26; i++) {
            req1 = cipherAlphabet[i] != curCipherAlphabet[i];
            req2 = firstLetterOf(bottomLetters[i]) != '-';

            canCorrect[i] = req1 && req2;
        }

        // pick a random letter to switch
        int randChoice = (int) (Math.random() * 25);
        do {
            randChoice++;
            if (randChoice == 25)
                randChoice = 0;
        } while (!canCorrect[randChoice]);

        // change the text of the changed cipherLetter
        String text = "" + cipherAlphabet[randChoice];
        bottomLetters[randChoice].setText(text);

        // change the text in the form of a hint
        changeHint(cipherAlphabet[randChoice] - Cipher.UPPER_CASE_START, randChoice);

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
                if (letter > 21)
                    top.smoothScrollTo((int) ViewHelper.percentWidth(SCROLL_END), 0);
                else if (letter < 4)
                    top.smoothScrollTo(0, 0);
                else
                    top.smoothScrollTo((int) ViewHelper.percentWidth(letter * 12.5 - 39), 0);
            }
        });
        // bottom goes to randchoice
        bottom.post(new Runnable() {
            @Override
            public void run() {
                if (choice > 21)
                    bottom.smoothScrollTo((int) ViewHelper.percentWidth(SCROLL_END), 0);
                else if (choice < 4)
                    bottom.smoothScrollTo(0, 0);
                else
                    bottom.smoothScrollTo((int) ViewHelper.percentWidth(choice * 12.5 - 39), 0);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                topLetters[letter].setBackgroundResource(R.drawable.basic_rectangle_green);
                bottomLetters[choice].setBackgroundResource(R.drawable.basic_rectangle_green);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        topLetters[letter].setBackgroundResource(R.drawable.basic_rectangle);
                        bottomLetters[choice].setBackgroundResource(R.drawable.basic_rectangle);
                    }
                }, 375);
            }
        }, 250);

        return true;
    }

    public boolean choose() {

        // check if the hint can be used
        if (!canUseHint())
            return false;

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

        return true;
    }

    private View.OnClickListener hintClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClick();
            int id = view.getId();
            if (app.getData().getBoolean(getString(GameState.LEVEL_WON, getTextPack(), getLevel()), false)) return;

            for (int i = 0; i < 26; i++)
                if (id == topLetters[i].getId()) {
                    if (topLetters[i].getText().equals("-") || isTopLetterHint(i)) return;
                    char[] alphabet = cipher.getCipherAlphabet();
                    for (int j = 0; j < 26; j++)
                        if (alphabet[j] == (char) (i + Cipher.UPPER_CASE_START)) {
                            String text = alphabet[j] + "";
                            bottomLetters[j].setText(text);
                            changeHint(i, j);

                            final int num1 = i;
                            final int num2 = j;

                            // scroll on bottom to the letter that was solved
                            final HorizontalScrollView bottom = getView(R.id.scrollViewBottom);
                            bottom.post(new Runnable() {
                                @Override
                                public void run() {
                                    bottom.smoothScrollTo((int) ViewHelper.percentWidth(num2 * 12.5 - 39), 0);
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    topLetters[num1].setBackgroundResource(R.drawable.basic_rectangle_green);
                                    bottomLetters[num2].setBackgroundResource(R.drawable.basic_rectangle_green);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            topLetters[num1].setBackgroundResource(R.drawable.basic_rectangle);
                                            bottomLetters[num2].setBackgroundResource(R.drawable.basic_rectangle);
                                        }
                                    }, 375);
                                }
                            }, 250);
                        }
                    addButtonListeners();
                    getView(R.id.chooseLetterText).startAnimation(ViewHelper.fadeOutAnimation(getView(R.id.chooseLetterText)));
                }
        }
    };

    public boolean reveal() {
        if (!canUseHint())
            return false;

        app.setState(MainActivity.INLEVELSTATE);
//        char[] alphabet = cipher.getCipherAlphabet();
//        for (int i = 0; i < 26; i++) {
//            changeHint(alphabet[i] - Cipher.UPPER_CASE_START, i);
//            app.getDataEditor().putString(getStringForLetter(TOP_LETTER, i, getTextPack(), getLevel()), "" + alphabet[i]).apply();
//        }

        curCipherText = hintCipherText = WHITE + regularText.toLowerCase() + FONT;
//        app.getDataEditor().putString(getString(CUR_CIPHER_TEXT, getTextPack(), getLevel()), curCipherText).apply();
        updateText();

        // remember that they've won
        app.getDataEditor().putBoolean(getString(LEVEL_WON, getTextPack(), getLevel()), true).apply();

        resetBottomLetters();

        return true;
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
//        for (Button letter : bottomLetters)
//            if (firstLetterOf(letter) == '!')
//                return true;
        int textPack = getTextPack(), level = getLevel();

        for (int i = 0; i < 26; i++) {
            if (app.getData().getString(getStringForLetter(CUR_BOTTOM_LETTER, i, textPack, level), "").equals("") &&
                    app.getData().getString(getStringForLetter(HINT_BOTTOM_LETTER, i, textPack, level), "").equals(""))
                return true;
        }

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
        // gets the first letter of a button in the form of char instead of string
        return button.getText().length() > 0 ? button.getText().charAt(0) : '!';
    }

    private String withoutHtml(String s) { return s.replace(FONT, "")
            .replace(RED, "").replace(WHITE, "");
    }

    // returns if there are hints on a given top or bottom letter
    private boolean isBottomLetterHint(int bottomLetter) {
        return !app.getData().getString(getStringForLetter(TOP_LETTER, bottomLetter, getTextPack(), getLevel()), "").equals("");
    }
    private boolean isTopLetterHint(int topLetter) {
        return !app.getData().getString(getStringForLetter(TOP_LETTER, topLetter, getTextPack(), getLevel()), "").equals("");
    }
    private void updateText() { text.setText(Html.fromHtml(curCipherText, 0)); }

    private char[] getCurAlphabet() {
        char[] alphabet = new char[26];
        for (int i = 0; i < bottomLetters.length; i++)
            alphabet[i] = Character.toUpperCase(firstLetterOf(bottomLetters[i]));
        return alphabet;
    }

    private void checkProgress() {
        // checks if progress has been made on the level
        app.getDataEditor().putBoolean(getString(IN_PROGRESS, getTextPack(), getLevel()),
                !hintCipherText.equals(cipherText) || !curCipherText.equals(cipherText)).apply();
    }

}