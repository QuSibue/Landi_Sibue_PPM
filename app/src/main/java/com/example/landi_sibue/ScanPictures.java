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

        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ? ";

        String selectionArgs[] = {"Camera"};

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = m_application_context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, orderBy);

        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            //Store the path of the image
            arrPath[i]= cursor.getString(dataColumnIndex);
            paths.add(arrPath[i]);

        }

        System.out.println(paths.get(paths.size()-1));

        return  paths;
    }
}
