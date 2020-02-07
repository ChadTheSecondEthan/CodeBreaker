package com.example.ciphergame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciphergame.GameState.*;
import com.example.ciphergame.Views.ViewHelper;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences data;
    private SharedPreferences.Editor dataEditor;

    private MediaPlayer mediaPlayer;
    private static Currencies currencies;
    private boolean volumeOn;

    private int currentState;
    private int prevState = 0;
    public static final int TEXTPACKSTATE = 0;
    public static final int CREDITS = 1;
    public static final int LEVELSTATE = 2;
    public static final int INLEVELSTATE = 3;
    public static final int HINTSTATE = 4;
    public static final int CURRENCYSTATE = 5;

    private CurrencyState currencyState;
    private InLevelState inLevelState;
    private ArrayList<GameState> gameStates;
//    int[] musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_pack_state);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        ViewHelper.setDisplayMetricsAndContext(getResources().getDisplayMetrics(), getApplicationContext());

        final ImageView logo = getView(R.id.logo);
        logo.setLayoutParams(new ConstraintLayout.LayoutParams((int) ViewHelper.percentWidth(60), (int) ViewHelper.percentHeight(266 / 4.0 * 0.6)));
        ViewHelper.center(logo);
        final long animationTime = 1400;
        logo.startAnimation(ViewHelper.fadeInAnimation(animationTime));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(ViewHelper.fadeOutAnimation(logo, animationTime));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                }, animationTime);
            }
        }, animationTime + 1000);
    }

    private void init() {

        data = getApplicationContext().getSharedPreferences("data", 0);
        dataEditor = data.edit();
        // TODO when finished with the game, delete these bottom lines
        dataEditor.remove("cipherText").remove("curCipherText").remove("hintCipherText").apply();
        for (int i = 0; i < 26; i++)
            dataEditor.remove("cipherLetter" + i).apply();

        currencies = new Currencies(this);
        volumeOn = data.getBoolean("volume", true);

        currentState = TEXTPACKSTATE;

        currencyState = new CurrencyState(this);
        inLevelState = new InLevelState(this);

        gameStates = new ArrayList<>();
        gameStates.add(new TextPackState(this));
        gameStates.add(new Credits(this));
        gameStates.add(new LevelState(this));
        gameStates.add(inLevelState);
        gameStates.add(new HintState(this));
        gameStates.add(currencyState);
//        musicFiles = new int[] {};

        setState(TEXTPACKSTATE);
//        addMusic();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    public MainActivity setState(int state) {
        switch (state) {
            case CREDITS:
                prevState = TEXTPACKSTATE;
                break;
            case LEVELSTATE:
                prevState = TEXTPACKSTATE;
                break;
            case INLEVELSTATE:
                prevState = LEVELSTATE;
                break;
            case HINTSTATE:
                prevState = currentState;
                break;
            case CURRENCYSTATE:
                prevState = INLEVELSTATE;
                break;
        }
        currentState = state;
        gameStates.get(currentState).init();
        return this;
    }

    public SharedPreferences getData() { return data; }
    public SharedPreferences.Editor getDataEditor() { return dataEditor; }
    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
    public static Currencies getCurrencies() { return currencies; }
    public int getPrevState() { return prevState; }
    public InLevelState getInLevelState() { return inLevelState; }
    public <T extends View> T getView(int id) { return findViewById(id); }
    public Button[] getButtons(int[] ids) {
        Button[] views = new Button[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public ImageView[] getImageViews(int[] ids) {
        ImageView[] views = new ImageView[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public TextView[] getTextViews(int[] ids) {
        TextView[] views = new TextView[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public View[] getViews(int[] ids) {
        View[] views = new View[ids.length];
        for (int i = 0; i < ids.length; i++)
            views[i] = findViewById(ids[i]);
        return views;
    }
    public void removeTexts() {
        for (int i = 0; i < 26; i++) dataEditor.remove("cipherLetter" + i).apply();
        dataEditor.remove("curCipherText").remove("cipherText").remove("hintCipherText").apply();
    }

    public boolean isVolumeOn() { return volumeOn; }
    public void setVolumeOn(boolean b) { volumeOn = b; }

    public void startNoMoneyAnimation() {
        if (currentState == CURRENCYSTATE)
            currencyState.startNoMoneyAnimation();
    }
}