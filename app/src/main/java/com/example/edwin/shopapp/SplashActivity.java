package com.example.edwin.shopapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

public class SplashActivity extends Activity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SystemClock.sleep(2000); //detiene en el splash dos segundos
        if (!TextUtils.isEmpty(Utilidades.getUserPrefs(prefs)) // si el usuario marco la casi la casila de recordar en login
                && !TextUtils.isEmpty(Utilidades.getPassPrefs(prefs))) {
            Intent in = new Intent(getApplicationContext(), MenusTabs.class);
            startActivity(in);
            finish();

        } else {
            Intent i = new Intent(getApplicationContext(), Principal.class);
            startActivity(i);
            finish();
        }
    }
}
