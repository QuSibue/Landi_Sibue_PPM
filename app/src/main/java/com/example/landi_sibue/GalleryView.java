package com.example.landi_sibue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class GalleryView extends View {
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScale = 1;
    private Bitmap resized =null;
    private int numColumns, numRows, nbPict;
    private int cellSize;

    public GalleryView(Context context) {
        super(context);

        numColumns = 7;
        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
    }

    public void setPicturesNumber(int nbPict) {
        this.nbPict = nbPict;
        calculateDimensions();
    }


    private void calculateDimensions() {
        numRows = nbPict/numColumns;
        cellSize = getWidth() / numColumns;

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

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                    resized = Bitmap.createScaledBitmap(bitmap, cellSize, cellSize, true);
                    canvas.drawBitmap(resized, i* cellSize, j * cellSize, null);
            }
        }

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
            return true;
        }
    }

    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(numColumns !=1){
                numColumns--;
                calculateDimensions();
            }
            invalidate();
            return true;
        }
    }
}
