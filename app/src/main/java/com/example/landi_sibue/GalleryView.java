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

    private int mScroll = 0; // entier utilisé pour le scroll des images

    private int numColumns; //entier indiquant le nombre de colonnes courantes (change suivant le zoom ou le dézoom)
    private int cellSize;   // entier indiquant la taille d'une image (carré dans notre cas)
    private int nbPict;     // entier indiquant le nombre d'images totale qui ont été trouvées dans la galerie

    private ArrayList<String> m_paths; //liste des liens des images trouvées dans la galerie

    private int nbRowPerDisplay; //nombre de lignes pouvant être affichées à la fois
    private int firstRowToDiplay; //Indice de la premiere ligne à afficher (en lien avec le scroll)

    private HashMap<Integer,Drawable> mCache = new HashMap<>(); //Liste des images déjà affichée au moins une fois

    public GalleryView(Context context, ArrayList<String> paths) {
        super(context);
        m_paths = paths; //On récupere la liste de paths passé au constructeur

        numColumns = 7; //on initialise notre nombre de colonnes au maximum

        mScrollDetector = new GestureDetector(context, new ScrollGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());

        nbPict = m_paths.size(); //on enregistre le nombre d'images au total

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        BitmapFactory.Options opt = new BitmapFactory.Options(); // On définit l'option qui va "raccourcir" les images rendues pour économiser de la mémoire
        opt.inSampleSize = 64;

        Drawable currentImage; //Drawable pour dessiner nos images

        int currentImageIndex; //indice de l'image courante à afficher

        for (int i = 0; i < nbRowPerDisplay ; i++){ //Pour chaque ligne affichables
            for (int j  = 0; j < numColumns; j++){ //Pour chaque colonne
                currentImageIndex = (firstRowToDiplay * numColumns) + i * numColumns + j; //On calcul l'indice de l'image courante (premiere image à afficher + le décalage courant)
                if(currentImageIndex < nbPict){ //on vérifie qu'on n'essaye pas d'afficher trop d'images
                    if(mCache.containsKey(currentImageIndex)){ //si notre cache possède l'image à afficher
                        currentImage = mCache.get(currentImageIndex); //on récupere l'image
                        currentImage.setBounds(j * cellSize, i * cellSize, cellSize + j*cellSize  , cellSize+i*cellSize); //on la place au bon endroit
                        currentImage.draw(canvas); //on l'affiche
                    }
                    else{
                        currentImage = addToCache(currentImageIndex,opt); //l'image n'a jamais été affichée auparavent, on la charge dans le cache et on la récupere
                        currentImage.setBounds(j * cellSize, i * cellSize, cellSize + j*cellSize  , cellSize+i*cellSize); //on la place au bon endroit
                        currentImage.draw(canvas); //on la dessine
                    }
                }

            }
        }
    }

    /*
        Fonction qui ajoute en cache les images déjà affichées une fois
     */
    public Drawable addToCache(int index, BitmapFactory.Options opt){

        Bitmap bitmap =  BitmapFactory.decodeFile(m_paths.get(index), opt);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        mCache.put(index,drawable);

        return  drawable;

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        calculationCoordinate();
    }

    /*
        Fonction qui calcul tout les éléments utiles pour les images
    */

    public void calculationCoordinate(){
        cellSize = getWidth() / numColumns; //Taille d'une cellule (en hauteur comme en largeur)
        nbRowPerDisplay = getHeight() / cellSize + 1 ; //nombre de lignes pouvant être affichées à la fois
        firstRowToDiplay = mScroll/cellSize; //première ligne à afficher
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScrollDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class ScrollGesture extends GestureDetector.SimpleOnGestureListener {

        /*
            Calcul du facteur de scroll afin de gérer les mouvement de l'utilisateur
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (0 > distanceY + mScroll){
                mScroll = 0;
            }
            else {
                mScroll += distanceY;
            }
            calculationCoordinate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float factor = detector.getScaleFactor();

            if(numColumns > 1 && factor > 1){ //si l'utilisateur éloigne ses doigts, on réduit le nombre de colonnes
                numColumns--;
            }
            else if (numColumns < 7 && factor < 1){ //si l'utilisateur rapproche ses doigts, on augmente le nombre de colonnes
                numColumns ++;
            }
            calculationCoordinate();
            return true;
        }
    }
}
