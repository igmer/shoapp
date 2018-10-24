package com.example.edwin.shopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(2000); //detiene en el splash dos segundos
        Intent i = new Intent(getApplicationContext(),Principal.class);
        startActivity(i);
        finish();
    }
}
