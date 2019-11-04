package com.example.landi_sibue;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryView extends View {
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private int mScale = 1;
    private Bitmap resized =null;
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private boolean[][] cellChecked;

    public GalleryView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());

    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }


    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }


    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;


        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.alterralogo);
        /*if(resized==null)
            resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
*/


        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {

                    resized = Bitmap.createScaledBitmap(bitmap, cellWidth, cellWidth, true);
                    canvas.drawBitmap(resized, i*cellWidth, j * cellWidth, null);

            }
        }

/*
        canvas.drawBitmap(resized,0,0,null);
*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class ZoomGesture extends GestureDetector.SimpleOnGestureListener {
        private boolean normal = true;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mScale = normal ? 3 : 1;
            resized = Bitmap.createScaledBitmap(resized, 100*mScale, 100*mScale, true);
            normal = !normal;
            invalidate();
            return true;
        }
    }

    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            resized = Bitmap.createScaledBitmap(resized, 100*mScale, 100*mScale, true);
            invalidate();
            return true;
        }
    }
}
