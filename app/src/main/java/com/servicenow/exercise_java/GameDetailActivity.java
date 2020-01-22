package com.servicenow.exercise_java;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.servicenow.exercise.R;
import com.servicenow.resources.Game;

public class GameDetailActivity extends AppCompatActivity {

    TextView detailView;

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_game_detail);
        detailView = findViewById(R.id.detail);
        Game game = getIntent().getParcelableExtra("game");
        detailView.setText(game.getLongDescription());
    }
}
