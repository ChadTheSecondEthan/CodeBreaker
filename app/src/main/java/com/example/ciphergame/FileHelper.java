package com.example.ciphergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.Context;

class FileHelper {

    private Context context;

    FileHelper(Context context) { this.context = context; }

    String getText(int textPack, int level) {
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(
                new int[] { R.raw.texts1, R.raw.texts2, R.raw.texts3, R.raw.texts4, R.raw.texts5 }[textPack]), Charset.forName("UTF-8")));
        try {
            for (int i = 1; i < level + 1; i++) br.readLine();
            return br.readLine();
        } catch (Exception e) { e.printStackTrace(); }
        return "Not found";
    }
}
