package com.example.ciphergame;

import android.content.Context;

public class Cipher {

    public static final int LOWER_CASE_START = 97;
    public static final int UPPER_CASE_START = 65;

    private final char[] cipherAlphabet;
    private final FileHelper fileHelper;

    Cipher(Context context) {
        fileHelper = new FileHelper(context);
        cipherAlphabet = randomAlphabet();
    }

    private String encipher(String plainText) {
        StringBuilder text = new StringBuilder();
        for (char c : plainText.toLowerCase().toCharArray()) {
            if (!Character.isLetter(c)) {
                text.append(c);
                continue;
            }
            text.append(cipherAlphabet[c - LOWER_CASE_START]);
        }
        return text.toString().toUpperCase();
    }

    private char[] randomAlphabet() {
        char[] newAlphabet = getAlphabet();
        for (int i = 0; i < 625; i++)
            if ((int) (Math.random() * 2) == 1) {
                char temp = newAlphabet[i % 25];
                newAlphabet[i % 25] = newAlphabet[(i % 25) + 1];
                newAlphabet[(i % 25) + 1] = temp;
            }
        return newAlphabet;
    }

    private char[] getAlphabet() { return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); }
    public String getText(int textPack, int level) { return encipher(getRegularText(textPack, level)); }
    public String getRegularText(int textPack, int level) { return fileHelper.getText(textPack, level).replace("\"", ""); }
    public char[] getCipherAlphabet() { return cipherAlphabet; }
}
