package com.noahhalpern.sweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by noahhalpern on 4/12/17.
 */

public class CellView extends TextView {
    private Context mContext;
    private Paint mBorderPaint = new Paint();
    private Paint mBackgroundPaint = new Paint();
    private TextPaint mTextPaint = new TextPaint();
    private String mHint;
    public boolean isMined = false;
    public boolean isRevealed = false;
    public int row;
    public int column;

    public CellView(Context context) {
        super(context);
        mContext = context;
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
        mTextPaint.setTextSize(80);
    }

    public boolean select(){
        isRevealed = true;

        if (isMined){
            mBackgroundPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
        } else {
            mBackgroundPaint.setColor(ContextCompat.getColor(mContext, R.color.grey));
        }

        this.invalidate();
        return !isMined;
    }

    public void setMine(){
        isMined = true;
    }

    public void setHint(String string){
        mHint = string;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Snap to width
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBorderPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
        mBorderPaint.setStrokeWidth(2);

        canvas.drawPaint(mBackgroundPaint);
        canvas.drawLine(0, 0, getWidth(), 0, mBorderPaint);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), mBorderPaint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), mBorderPaint);
        canvas.drawLine(0, 0, 0, getHeight(), mBorderPaint);
        if (mHint != null){
            Log.i("CELL VIEW", "SETTING HINT TEXT");
            canvas.drawText(mHint, getWidth()/2 - 10, getWidth()/2 - 10, mTextPaint);
        }
    }

}

