package com.learntodroid.simplealarmclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.learntodroid.simplealarmclock.R;
import com.rbddevs.splashy.Splashy;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new Splashy(this)  // For JAVA : new Splashy(this)
                .setLogo(R.drawable.clouds2)
                .setDuration(1000)
                .setTitle("Remote Alarm")
                .setTitleSize(30.0f)
                .setTitleColor(R.color.colorAccent)
                .setFullScreen(true)
                .setAnimation(Splashy.Animation.SLIDE_IN_TOP_BOTTOM, 500)
                .setProgressColor(R.color.white)
                .show();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }, 1000);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
