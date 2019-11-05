package com.example.landi_sibue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryView extends View {

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScale = 1;
    private Bitmap resized =null;
    private int numColumns, numRows, nbPict;
    private int cellSize;
    private ArrayList<String> m_paths;
    private Context m_context;

    private int nbRowPerDisplay;
    private int nbImageToDisplay;

    public GalleryView(Context context, ArrayList<String> paths) {
        super(context);

        m_context = context;
        m_paths = paths;
        numColumns = 2;
        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
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

        numRows = (nbPict/numColumns)+1;
        cellSize = getWidth() / numColumns;

        System.out.println("CELL SIZE CALCULATE " + getWidth());

        nbRowPerDisplay = getHeight() / cellSize ;
        nbImageToDisplay = nbRowPerDisplay * numColumns;

        //Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.alterralogo);
        int bitmapIndex = 0;
        ArrayList<Bitmap> list = new ArrayList<>();

        for(int i = 0 ; i< nbImageToDisplay;i++){
            list.add(BitmapFactory.decodeFile(m_paths.get(i)));
        }

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < nbRowPerDisplay; j++) {
                    System.out.println("CELL SIZE IN FOR " + cellSize);
                    resized = Bitmap.createScaledBitmap(list.get(bitmapIndex), cellSize, cellSize, true);
                    canvas.drawBitmap(resized, i* cellSize, j * cellSize, null);
                    bitmapIndex++;
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
            System.out.println(detector.getScaleFactor());
            float factor = detector.getScaleFactor();
            if(numColumns > 1 && factor > 1){
                numColumns--;
            }
            else if (numColumns < 7 && factor < 1){
                numColumns ++;
            }
            invalidate();
            return true;
        }
    }
}
