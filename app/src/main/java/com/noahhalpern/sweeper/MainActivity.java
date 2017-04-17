package com.noahhalpern.sweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.message;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String GAME_LEVEL = "com.noahhalpern.sweeper.LEVEL";
    public static final int EASY = 0;
    public static final int HARD = 1;

    @BindView(R.id.easy_button) Button easyButton;
    @BindView(R.id.hard_button) Button hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.easy_button)
    public void startEasyGame(Button button){
        startGame(EASY);
    }

    @OnClick(R.id.hard_button)
    public void startHardGame(Button button){
        startGame(HARD);
    }

    private void startGame(int level){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_LEVEL, level);
        startActivity(intent);
    }
}
