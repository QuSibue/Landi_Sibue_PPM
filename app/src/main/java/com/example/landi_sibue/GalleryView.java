package com.example.landi_sibue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

public class GalleryView extends View {

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mScrollGestureDetector;

    private float mScale = 1.f;
    private Bitmap resized =null;

    private int numColumns, numRows, nbPict;
    private int cellSize;

    private ArrayList<String> m_paths;
    private Context m_context;

    private ArrayList<Bitmap> m_imagesList = new ArrayList<>();

    private int nbRowPerDisplay;
    private int nbImageToDisplay;
    private int nbImagesAlreadyLoaded;

    public GalleryView(Context context, ArrayList<String> paths) {
        super(context);

        m_context = context;
        m_paths = paths;

        numColumns = 5;
        nbImagesAlreadyLoaded = 0;

        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
/*
        mScrollGestureDetector = new ScrollGesture(context, new ScrollGesture());
*/


        this.nbPict = m_paths.size();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapIndex = 0;
        Drawable d;

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < nbRowPerDisplay; j++) {
                resized = Bitmap.createScaledBitmap(m_imagesList.get(bitmapIndex), cellSize, cellSize, true);
                d = new BitmapDrawable(getResources(), resized);

                d.setBounds(i*cellSize, j*cellSize, cellSize + i*cellSize  , cellSize+j*cellSize);
                d.draw(canvas);


/*
                    canvas.drawBitmap(resized, i* cellSize, j * cellSize, null);
*/
                    bitmapIndex++;
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        calculationCoordinate();
    }

    public void calculationCoordinate(){
        numRows = nbPict/numColumns;
        cellSize = getWidth() / numColumns;

        nbRowPerDisplay = getHeight() / cellSize ;
        nbImageToDisplay = nbRowPerDisplay * numColumns;

        loadImages();
    }

    public void loadImages(){

        if(nbImageToDisplay > nbImagesAlreadyLoaded){
            int nbImagesToLoad = nbImagesAlreadyLoaded + nbImageToDisplay;
            for(int i = nbImagesAlreadyLoaded ; i< nbImagesToLoad; i++){
                m_imagesList.add(BitmapFactory.decodeFile(m_paths.get(i)));
                nbImagesAlreadyLoaded++;
            }
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class ZoomGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }

    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            System.out.println(detector.getScaleFactor());
            float factor = detector.getScaleFactor();
            if(numColumns > 1 && factor > 1){
                numColumns--;
            }
            else if (numColumns < 5 && factor < 1){
                numColumns ++;
            }
            calculationCoordinate();
            return true;
        }
    }

    public class ScrollGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("SCROLL", "onScroll");
            return true;
        }
    }
}
