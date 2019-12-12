package com.example.Y3840030.Y3840030mobiledevassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void getStarted (View view ) {
        Intent i = new Intent(this, GetLocation.class);
        startActivity(i);
    }

}