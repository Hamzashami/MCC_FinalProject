package com.hamzashami.coronaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private void goToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    }

    private void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    @Override
     protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    goToMain();
                } else {
                    goToLogin();
                }
            }
        }.start();
    }
}
