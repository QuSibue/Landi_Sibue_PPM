package com.example.landi_sibue;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ScanPictures {

    Context m_application_context;

    public ScanPictures(Context activity_context){
        m_application_context = activity_context;
    }

    public ArrayList<String> getImages(){

        ArrayList<String> paths = new ArrayList<String>(); //liste qui va contenir les chemins vers les images de la gallerie

        String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ? "; //On rajoute une requete

        String selectionArgs[] = {"Camera"}; //on precise où on veut chercher, dans la galerie dans notre cas

        String orderBy = MediaStore.Images.Media.DATE_TAKEN; //On trie les photo par ordre de capture (de la plus ancienne à la plus récente)

        //On stock toutes les images de la galerie dans le Cursor
        Cursor cursor = m_application_context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, orderBy);

        //On récupere le nombre total d'images
        int count = cursor.getCount();

        for (int i = 0; i < count; i++) { //Pour chaque élements du cursor
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            //On ajoute le chemin de l'image au tableau de paths
            paths.add(cursor.getString(dataColumnIndex));

        }

        return  paths;
    }
}
