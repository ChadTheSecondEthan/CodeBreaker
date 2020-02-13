package com.example.ciphergame;

import java.io.InputStream;

import android.content.Context;

class FileHelper {

    private final Context context;

    FileHelper(Context context) { this.context = context; }

    String getText(int textPack, int level) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.texts1);
        switch (textPack) {
            case 0:
                inputStream = context.getResources().openRawResource(R.raw.texts1);
                break;
            case 1:
                inputStream = context.getResources().openRawResource(R.raw.texts2);
                break;
            case 2:
                inputStream = context.getResources().openRawResource(R.raw.texts3);
                break;
            case 3:
                inputStream = context.getResources().openRawResource(R.raw.texts4);
                break;
            case 4:
                inputStream = context.getResources().openRawResource(R.raw.texts5);
                break;
            case 5:
                inputStream = context.getResources().openRawResource(R.raw.texts6);
                break;
        }
        int offset = findOffsetOfText(inputStream, level);
        int end = findEndOfText(inputStream, level);
        char[] chars = new char[] {};
        try {
            inputStream.reset();
            chars = new char[inputStream.available()];
            for (int i = 0; i < chars.length; i++)
                chars[i] = (char) inputStream.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder string = new StringBuilder();
        for (int i = offset + 1; i < end; i++)
            string.append(chars[i]);
        return string.toString();
    }

    private int findOffsetOfText(InputStream inputStream, int level) {
        try {
            inputStream.reset();
            int[] bytes = new int[inputStream.available()];
            for (int i = 0; i < bytes.length; i++)
                bytes[i] = inputStream.read();
            int numQuotes = 0;
            for (int i = 0; i < bytes.length; i++)
                if (bytes[i] == '"') {
                    numQuotes++;
                    if (numQuotes >= (level * 2) + 1)
                        return i;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int findEndOfText(InputStream inputStream, int level) {
        // returns offset and length in that order of the text
        int offset = findOffsetOfText(inputStream, level);
        try {
            inputStream.reset();
            int[] bytes = new int[inputStream.available()];
            for (int i = 0; i < bytes.length; i++)
                bytes[i] = inputStream.read();
            for (int i = offset + 2; i < bytes.length; i++) {
                if (bytes[i] == '"') {
                    return i;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }
}
