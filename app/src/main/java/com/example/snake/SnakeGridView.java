package com.example.snake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

public class SnakeGridView extends View {

    //consts
    private final int GRID_NUM = 25;
    private final int SNAKE_SPEED = 8;
    private final int COLOR_GRID = Color.WHITE;
    private final int COLOR_SNAKE = Color.BLUE;
    private final int COLOR_FOOD = Color.RED;
    private final int COLOR_GRID_LINE = Color.GRAY;

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
    private boolean isGameRunning = false;
    private int snakeLen;

    public SnakeGridView(Context context) {
        super(context);
        init();
    }
    public SnakeGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public SnakeGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    private void init(){

        snakePoints.clear();
        isFood = false;
        snakeLen = 3;

        INITIAL_GRID_COLOR = new int[GRID_NUM][GRID_NUM];
        for(int i = 0; i < GRID_NUM; i++){
            for(int j = 0; j < GRID_NUM; j++) {
                INITIAL_GRID_COLOR[i][j] = COLOR_GRID;
            }
        }
        gridColor = INITIAL_GRID_COLOR;

        snakePoints.add(new Point(12, 10));
        snakePoints.add(new Point(11, 10));
        snakePoints.add(new Point(10, 10));
        snakeDirection = DIRECTION_RIGHT;
        for(Point point : snakePoints){
            gridColor[point.x][point.y] = COLOR_SNAKE;
        }

        generateFood();

        test();

    }

    private void generateFood(){
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
                new AlertDialog.Builder(getContext()).setMessage(getResources().getString(R.string.dialog_msg))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                init();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage(String.format(Locale.CHINA, getResources().getString(R.string.dialog_result), snakeLen))
                        .create()
                        .show();
            }
        });
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

    private void test(){
        isGameRunning = true;
        new GameThread().start();
    }

    public void setSnakeDirection(int direction){
        snakeDirection = direction;
    }

}
