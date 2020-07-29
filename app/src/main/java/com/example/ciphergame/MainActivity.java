package com.example.ciphergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.VideoView;

import com.example.ciphergame.GameState.*;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

/*
    Codebreaker made by Ethan Fisher
    This is a game in which the user will decipher messages enciphered through a monoalphabetic cipher
    Version: v1.0
 */

// TODO music
// TODO add purchases
// TODO add packs to buy

public class MainActivity extends AppCompatActivity {

    private SharedPreferences data;
    private SharedPreferences.Editor dataEditor;

    private MediaPlayer song, click;
    private static Currencies currencies;
    private static Cipher cipher;
    private boolean volumeOn;

    private int songFile, clickFile;

    public static final String[] TEXT_PACKS = new String[] {
            "Beta Pack 1", "Beta Pack 2", "Stay Tuned: More Coming Soon!"
    };

//    private int currentState;
//    private int prevState;
    public static final int MENU = 0;
    public static final int TEXTPACKSTATE = 1;
    public static final int LEVELSTATE = 2;
    public static final int INLEVELSTATE = 3;
    public static final int PURCHASE = 4;

//    private Purchase purchase;
    private InLevelState inLevelState;
    private ArrayList<GameState> gameStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(false);
        setContentView(R.layout.menu_state);

        MobileAds.initialize(this, (OnInitializationCompleteListener) null);
        ViewHelper.setDisplayMetrics(getResources().getDisplayMetrics());

        // TODO play video for one second or so, then go to the main menu

        final VideoView logo = findViewById(R.id.logo); // TODO fix
        Uri uri = Uri.parse("android.resource://com.example.ciphergame/" + R.raw.wink);
        logo.setVideoURI(uri);
        logo.requestFocus();
        logo.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(ViewHelper.fadeOutAnimation(logo));
                init();
            }
        }, 1000);

        cipher = new Cipher(getApplicationContext());

        songFile = R.raw.pop; // TODO
        clickFile = R.raw.pop;
    }

    private void init() {
        data = getApplicationContext().getSharedPreferences("data", 0);
        dataEditor = data.edit();
        dataEditor.apply();

        currencies = new Currencies(this);
        volumeOn = data.getBoolean("volume", true);

        // TODO find song for background music
        song = MediaPlayer.create(this, songFile /*TODO*/);
        song.setVolume((float) 0.2, (float) 0.2);
        song.setLooping(true);
        song.start();
        
        click = MediaPlayer.create(this, clickFile);

//        purchase = new Purchase(this);
        inLevelState = new InLevelState(this);

        gameStates = new ArrayList<>();
        gameStates.add(new Menu(this));
        gameStates.add(new TextPackState(this));
        gameStates.add(new LevelState(this));
        gameStates.add(inLevelState);
        gameStates.add(new Purchase(this));

        gameStates.get(MENU).init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (song != null) {
            song.reset();
            song.release();
            song = null;
        }
        if (click != null) {
            click.reset();
            click.release();
            click = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // TODO find song for background music
        song = MediaPlayer.create(this, songFile);
        song.setVolume((float) 0.2, (float) 0.2);
        song.setLooping(true);
        song.start();

        click = MediaPlayer.create(this, clickFile);
    }

    @Override
    protected void onDestroy() {
        if (song != null) {
            song.reset();
            song.release();
            song = null;
        }
        if (click != null) {
            click.reset();
            click.release();
            click = null;
        }
        dataEditor.apply();
        super.onDestroy();
    }

    public void setState(int state) {
        gameStates.get(state).init();
    }

    public SharedPreferences getData() { dataEditor.apply(); return data; }
    public SharedPreferences.Editor getDataEditor() { return dataEditor; }
    public MediaPlayer getClick() { return click; }
    public MediaPlayer getSong() { return song; }
    public void playButtonClick() { click.start(); }
    public static Currencies getCurrencies() { return currencies; }
    public InLevelState getInLevelState() { return inLevelState; }
    public static Cipher getCipher() { return cipher; }
    public int addCoins(int coins) {
        currencies.addCoins(coins);
        return currencies.getCoins();
    }

    public boolean isVolumeOn() { return volumeOn; }
    public void setVolumeOn(boolean b) { volumeOn = b; }

}