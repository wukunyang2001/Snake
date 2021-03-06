package com.example.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    private SnakeGridView snakeGridView;

    private final int DIRECTION_UP = 100;
    private final int DIRECTION_DOWN = -100;
    private final int DIRECTION_LEFT = 101;
    private final int DIRECTION_RIGHT = -101;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snakeGridView = findViewById(R.id.snake);
        snakeGridView.setButton((Button)findViewById(R.id.button));

    }

    public void onClick(View button){
        switch (button.getId()){
            case R.id.button_up:
                snakeGridView.setSnakeDirection(DIRECTION_LEFT);
                break;
            case R.id.button_down:
                snakeGridView.setSnakeDirection(DIRECTION_RIGHT);
                break;
            case R.id.button_left:
                snakeGridView.setSnakeDirection(DIRECTION_DOWN);
                break;
            case R.id.button_right:
                snakeGridView.setSnakeDirection(DIRECTION_UP);
                break;
            case R.id.button:
                Button myButton = (Button) button;
                if (!snakeGridView.isGameRunning && !snakeGridView.isGameOver){
                    //paused, need to resume
                    snakeGridView.start();
                    myButton.setText(R.string.button_pause);
                } else if (snakeGridView.isGameRunning && !snakeGridView.isGameOver){
                    //game is running, need to pause
                    snakeGridView.isGameRunning = false;
                    myButton.setText(R.string.button_resume);
                } else {
                    snakeGridView.init();
                    snakeGridView.generateFood();
                    snakeGridView.start();
                    myButton.setText(R.string.button_pause);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
