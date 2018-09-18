package com.example.snake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SnakeGridView snakeGridView;

    private final int DIRECTION_UP = 100;
    private final int DIRECTION_DOWN = -100;
    private final int DIRECTION_LEFT = 101;
    private final int DIRECTION_RIGHT = -101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snakeGridView = findViewById(R.id.snake);

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
        }
    }
}
