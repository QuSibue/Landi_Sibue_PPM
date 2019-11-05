package com.example.landi_sibue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryView extends View {

    private GestureDetector mScrollDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private float mScale = 1.f;
    private float mScroll = 0f;

    private Bitmap resized =null;

    private int numColumns, numRows, nbPict;
    private int cellSize;

    private ArrayList<String> m_paths;
    private Context m_context;

    private ArrayList<Bitmap> m_imagesList = new ArrayList<>();

    private int nbRowPerDisplay;
    private int nbImageToDisplay;
    private int nbImagesAlreadyLoaded;

    private int bitmapIndex;

    public GalleryView(Context context, ArrayList<String> paths) {
        super(context);

        m_context = context;
        m_paths = paths;

        numColumns = 3;
        nbImagesAlreadyLoaded = 0;

        mScrollDetector = new GestureDetector(context, new ScrollGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());

        nbPict = m_paths.size();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable d;

        /*for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < nbRowPerDisplay; j++) {
                    resized = Bitmap.createScaledBitmap(m_imagesList.get(bitmapIndex), cellSize, cellSize, true);
                    canvas.drawBitmap(resized, i* cellSize, j * cellSize - mScroll, null);
                    bitmapIndex++;
            }
        }*/

        for (int i = bitmapIndex; i - bitmapIndex < nbImageToDisplay && i < m_imagesList.size(); i++) {
            resized = Bitmap.createScaledBitmap(m_imagesList.get(bitmapIndex), cellSize, cellSize, true);
            canvas.drawBitmap(resized, (i%numColumns)*cellSize, (i/numColumns)*cellSize-mScroll, null);
            bitmapIndex++;
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

        nbRowPerDisplay = (getHeight() / cellSize) + 1 ;
        bitmapIndex = (int) mScroll / cellSize * numColumns;
        nbImageToDisplay = nbRowPerDisplay * numColumns;

        loadImages();
    }

    public void loadImages(){

        if(nbImageToDisplay > nbImagesAlreadyLoaded){
            int nbImagesToLoad = nbImagesAlreadyLoaded + nbImageToDisplay ;
            for(int i = nbImagesAlreadyLoaded ; i< nbImagesToLoad; i++){
                m_imagesList.add(BitmapFactory.decodeFile(m_paths.get(i)));
                nbImagesAlreadyLoaded++;
            }
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScrollDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class ScrollGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY != 1) {
                if (0 > distanceY + mScroll){
                    mScroll = 0f;
                }
                else {
                    mScroll += distanceY;
                }
                calculationCoordinate();

                
            }

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
            else if (numColumns < 3 && factor < 1){
                numColumns ++;
            }
            calculationCoordinate();
            return true;
        }
    }
}
