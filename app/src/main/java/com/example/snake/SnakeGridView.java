package com.example.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SnakeGridView extends View {

    private final int GRID_NUM = 25;

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

    private void init(){
    }

    private int canvasSize;
    private float gridSize;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        canvasSize = getMeasuredWidth();
        gridSize = (float) canvasSize / GRID_NUM;
        setMeasuredDimension(canvasSize, canvasSize);
    }

    private Paint paintFill = new Paint();
    private Paint paintStroke = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintFill.setColor(Color.GRAY);
        paintFill.setStyle(Paint.Style.FILL);

        paintStroke.setColor(Color.BLACK);
        paintStroke.setStyle(Paint.Style.STROKE);

        for(int i = 0; i < GRID_NUM; i++){
            for(int j = 0; j < GRID_NUM; j++){
                float top = i * gridSize;
                float bottom = top + gridSize;
                float left = j* gridSize;
                float right = left + gridSize;
                canvas.drawRect(left, top, right, bottom, paintFill);
                canvas.drawRect(left, top, right, bottom, paintStroke);
            }
        }
    }

}
