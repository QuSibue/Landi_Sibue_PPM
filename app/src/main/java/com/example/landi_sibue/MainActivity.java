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


    //Objet qui va nous permettre de récuperer les images de la gallerie du téléphone
    ScanPictures m_scan;

    //liste des chemins des images de la gallerie
    ArrayList<String>m_paths;

    //objet qui permet d'afficher les images
    GalleryView m_gallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On créer l'object scanner pour récuperer les photos de la gallerie
        m_scan = new ScanPictures(this.getApplicationContext());

        //Boolean nous indiquant si l'application à l'autorisation d'accéder au contenu interne du téléphone
        Boolean isAuthorized = this.isReadStoragePermissionGranted();

        //Si on est authorisé, on scan la gallerie et on récuperer les chemins des photos
        if(isAuthorized){
            System.out.println("SCAN IMAGES ALREADY GRANTED");
            m_paths = m_scan.getImages();
            System.out.println("SCAN IMAGES ALREADY GRANTED DONE");
        }

        m_gallery = new GalleryView(this.getApplicationContext(),m_paths);
        setContentView(m_gallery);

    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) { //le sdk est supérieur a 23, l'autorisation n'est pas donnée automatiquement
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                //L'autorisation n'est pas accordée, on la demande à l'utilisateur
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
            //SI l'utilisateur à donner l'autorisation, on scan les photos
            if (requestCode == 3 ) {
                System.out.println("SCAN IMAGES FIRST GRANTED");
                this.m_scan.getImages();
                System.out.println("SCAN IMAGES FIRST GRANTED DONE");
            }
        }
    }
}
