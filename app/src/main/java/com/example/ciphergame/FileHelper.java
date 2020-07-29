package com.example.ciphergame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

class FileHelper {

    private final Context context;

    FileHelper(Context context) { this.context = context; }

    String getText(int textPack, int level) {
        // TODO add more text packs
        InputStream inputStream = context.getResources().openRawResource(
                new int[] { R.raw.texts1, R.raw.texts2 }[textPack]);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            for (int i = 0; i < level; i++) bufferedReader.readLine();
            return bufferedReader.readLine();
        } catch (Exception e) { e.printStackTrace(); }
        return "not found";
    }
}
