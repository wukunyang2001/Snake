package com.example.snake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class SnakeGridView extends View {

    SharedPreferences sharedPref;

    //preferences
    private int GRID_NUM;
    private int SNAKE_SPEED;
    private int COLOR_GRID;
    private int COLOR_SNAKE;
    private int COLOR_FOOD;
    private int COLOR_GRID_LINE;

    private final int DIRECTION_UP = 100;
    private final int DIRECTION_DOWN = -100;
    private final int DIRECTION_LEFT = 101;
    private final int DIRECTION_RIGHT = -101;

    //console related vars
    private float gridSize;
    private Paint paintFill = new Paint();
    private Paint paintStroke = new Paint();
    private int[][] gridColor;
    private int[][] INITIAL_GRID_COLOR;

    //other vars
    private LinkedList<Point> snakePoints = new LinkedList<>();
    private int snakeDirection;
    private Point foodPoint = new Point();
    private boolean isFood = false;
    public boolean isGameRunning = false;
    public boolean isGameOver = true;
    private int snakeLen;
    private Button button;

    public SnakeGridView(Context context) {
        super(context);
    }
    public SnakeGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public SnakeGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
        int canvasSize = getMeasuredWidth();
        gridSize = (float) canvasSize / GRID_NUM;
        setMeasuredDimension(canvasSize, canvasSize);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintFill.reset();
        paintFill.setStyle(Paint.Style.FILL);
        paintStroke.reset();
        paintStroke.setColor(COLOR_GRID_LINE);
        paintStroke.setStyle(Paint.Style.STROKE);

        for(int i = 0; i < GRID_NUM; i++){
            for(int j = 0; j < GRID_NUM; j++){
                float top = i * gridSize;
                float bottom = top + gridSize;
                float left = j* gridSize;
                float right = left + gridSize;
                paintFill.setColor(gridColor[i][j]);
                canvas.drawRect(left, top, right, bottom, paintFill);
                canvas.drawRect(left, top, right, bottom, paintStroke);
            }
        }
    }

    public void init(){

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        GRID_NUM = Integer.valueOf(sharedPref.getString("pref_key_grid_num", "15"));
        SNAKE_SPEED = Integer.valueOf(sharedPref.getString("pref_key_snake_speed", "5"));
        COLOR_GRID = Integer.valueOf(sharedPref.getString("pref_key_color_grid", "-1"));
        COLOR_SNAKE = Integer.valueOf(sharedPref.getString("pref_key_color_snake", "-16776961"));
        COLOR_FOOD = Integer.valueOf(sharedPref.getString("pref_key_color_food", "-65536"));
        COLOR_GRID_LINE = Integer.valueOf(sharedPref.getString("pref_key_color_grid_line", "-7829368"));

        snakePoints.clear();
        isFood = false;
        snakeLen = 3;
        isGameRunning = false;
        isGameOver = true;

        INITIAL_GRID_COLOR = new int[GRID_NUM][GRID_NUM];
        for(int i = 0; i < GRID_NUM; i++){
            for(int j = 0; j < GRID_NUM; j++) {
                INITIAL_GRID_COLOR[i][j] = COLOR_GRID;
            }
        }
        gridColor = INITIAL_GRID_COLOR;

        snakePoints.add(new Point(6, 5));
        snakePoints.add(new Point(5, 5));
        snakePoints.add(new Point(4, 5));
        snakeDirection = DIRECTION_RIGHT;
        for(Point point : snakePoints){
            gridColor[point.x][point.y] = COLOR_SNAKE;
        }

    }

    public void generateFood(){
        if(isFood) return;
        isFood = true;
        Random random = new Random();
        int x = random.nextInt(GRID_NUM);
        int y = random.nextInt(GRID_NUM);
        while(gridColor[x][y] == COLOR_SNAKE) {
            x = random.nextInt(GRID_NUM);
            y = random.nextInt(GRID_NUM);
        }
        foodPoint.set(x, y);
        gridColor[x][y] = COLOR_FOOD;
    }

    private void moveSnake() throws Exception {
        Point currentHeadPos = snakePoints.getFirst();
        Point newPoint;
        switch (snakeDirection){
            case DIRECTION_UP:
                newPoint = new Point(currentHeadPos.x, currentHeadPos.y + 1);
                isGameOver(newPoint);
                snakePoints.addFirst(newPoint);
                gridColor[newPoint.x][newPoint.y] = COLOR_SNAKE;
                break;
            case DIRECTION_DOWN:
                newPoint = new Point(currentHeadPos.x, currentHeadPos.y - 1);
                isGameOver(newPoint);
                snakePoints.addFirst(newPoint);
                gridColor[newPoint.x][newPoint.y] = COLOR_SNAKE;
                break;
            case DIRECTION_LEFT:
                newPoint = new Point(currentHeadPos.x - 1, currentHeadPos.y);
                isGameOver(newPoint);
                snakePoints.addFirst(newPoint);
                gridColor[newPoint.x][newPoint.y] = COLOR_SNAKE;
                break;
            case DIRECTION_RIGHT:
                newPoint = new Point(currentHeadPos.x + 1, currentHeadPos.y);
                isGameOver(newPoint);
                snakePoints.addFirst(newPoint);
                gridColor[newPoint.x][newPoint.y] = COLOR_SNAKE;
                break;
        }
        if(isEating()) {
            isFood = false;
            snakeLen++;
        } else{
            Point point = snakePoints.removeLast();
            gridColor[point.x][point.y] = COLOR_GRID;
        }
    }

    private boolean isEating(){
        Point currentHeadPos = snakePoints.getFirst();
        return currentHeadPos.equals(foodPoint);
    }

    private void isGameOver(Point point) throws Exception{
        if(gridColor[point.x][point.y] == COLOR_SNAKE) throw new Exception();
    }

    private void gameOver() {
        post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getContext()).setTitle(getResources().getString(R.string.dialog_msg))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                button.setText(R.string.button_pause);
                                init();
                                generateFood();
                                start();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                button.setText(R.string.button_start);
                                isGameRunning = false;
                                isGameOver = true;
                            }
                        })
                        .setMessage(String.format(Locale.CHINA, getResources().getString(R.string.dialog_result), snakeLen))
                        .create()
                        .show();
            }
        });
    }

    public void setButton(Button button) {
        this.button = button;
    }

    private class GameThread extends Thread{
        @Override
        public void run() {
            while(isGameRunning){
                try {
                    moveSnake();
                } catch (Exception e) {
                    gameOver();
                    break;
                }
                generateFood();
                postInvalidate();
                applySpeed();
            }
        }

        private void applySpeed(){
            try {
                sleep((long)1000 / SNAKE_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        if(isGameRunning) return;
        isGameRunning = true;
        isGameOver = false;
        new GameThread().start();
    }

    public void setSnakeDirection(int direction){
        if(!isGameRunning) return;
        snakeDirection = direction;
    }

}
