package com.example.landi_sibue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import java.util.HashMap;
import java.util.Random;

public class GalleryView extends View {

    private GestureDetector mScrollDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private float mScale = 1.f;
    private int mScroll = 0;
    int optSampleSize;

    private int numColumns;
    private int cellSize;
    private int nbPict;

    private ArrayList<String> m_paths;

    private int nbRowPerDisplay;
    private int firstRowToDiplay;


    private Paint[] mColors;

    private HashMap<Integer,Drawable> mCache = new HashMap<>();

    public GalleryView(Context context, ArrayList<String> paths) {
        super(context);
        m_paths = paths;

        numColumns = 7;

        mScrollDetector = new GestureDetector(context, new ScrollGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());

        nbPict = m_paths.size();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    /*@Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 64;

        Bitmap bitmap;
        Drawable currentImage;

        int currentImageIndex;

        for (int i = 0; i < nbRowPerDisplay ; i++){
            for (int j  = 0; j < numColumns; j++){
                currentImageIndex = (firstRowToDiplay * numColumns) + i * numColumns + j;
                if(currentImageIndex < nbPict){
                    if(mCache.containsKey(currentImageIndex)){
                        currentImage = mCache.get(currentImageIndex);
                        currentImage.setBounds(j * cellSize, i * cellSize, cellSize + j*cellSize  , cellSize+i*cellSize);
                        currentImage.draw(canvas);
                    }
                    else{
                        currentImage = addToCache(currentImageIndex,opt);
                        currentImage.setBounds(j * cellSize, i * cellSize, cellSize + j*cellSize  , cellSize+i*cellSize);
                        currentImage.draw(canvas);
                    }
                    /*bitmap =  BitmapFactory.decodeFile(m_paths.get(currentImageIndex), opt);
                    currentImage = new BitmapDrawable(getResources(), bitmap);
                    currentImage.setBounds(j * cellSize, i * cellSize, cellSize + j*cellSize  , cellSize+i*cellSize);
                    currentImage.draw(canvas);*/
                }

            }
        }
    }

    public Drawable addToCache(int index, BitmapFactory.Options opt){
        Bitmap bitmap =  BitmapFactory.decodeFile(m_paths.get(index), opt);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        //drawable.setBounds(left, top, right  , bottom);

        mCache.put(index,drawable);

        return  drawable;

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        calculationCoordinate();
    }

    public void calculationCoordinate(){
        cellSize = getWidth() / numColumns;
        nbRowPerDisplay = getHeight() / cellSize + 1 ;
        firstRowToDiplay = mScroll/cellSize;
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
                    mScroll = 0;
                }
                else {
                    mScroll += distanceY;
                }
            }
            calculationCoordinate();
            return super.onScroll(e1, e2, distanceX, distanceY);
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
            else if (numColumns < 7 && factor < 1){
                numColumns ++;
            }
            calculationCoordinate();
            return true;
        }
    }
}
