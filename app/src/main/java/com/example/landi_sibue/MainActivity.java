package com.example.landi_sibue;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ScanPictures m_scan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GalleryView view = new GalleryView(this);
        view.setPicturesNumber(70);
        setContentView(view);

        m_scan = new ScanPictures(this.getApplicationContext());

        Boolean isAuthorized = this.isReadStoragePermissionGranted();

        if(isAuthorized){
            System.out.println("SCAN IMAGES ALREADY GRANTED");
            ArrayList<String> paths = m_scan.getImages();
            System.out.println("SCAN IMAGES ALREADY GRANTED DONE");
        }

    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //les permissions sont automatiquement données en sdk < 23
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 3 ) {
                System.out.println("SCAN IMAGES FIRST GRANTED");
                this.m_scan.getImages();
                System.out.println("SCAN IMAGES FIRST GRANTED DONE");
            }
        }
    }
}
