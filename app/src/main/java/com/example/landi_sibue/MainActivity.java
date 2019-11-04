package com.example.landi_sibue;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GalleryView view = new GalleryView(this);
        view.setNumColumns(7);
        view.setNumRows(2);
        setContentView(view);

    }
}
