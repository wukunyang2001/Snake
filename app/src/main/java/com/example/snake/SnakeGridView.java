package com.example.snake;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SnakeGridView extends View {

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
        int sideLen = getMeasuredWidth();
        setMeasuredDimension(sideLen, sideLen);
    }

}
