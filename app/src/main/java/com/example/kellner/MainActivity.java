package com.example.kellner;

import androidx.appcompat.app.AppCompatActivity;
import at.orderlibrary.UnitTestVariables;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {
boolean connected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onStart() {
        super.onStart();
        perms();
    }

    private void perms(){
        if(checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET},0);
        }
    }
}
