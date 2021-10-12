package com.learntodroid.simplealarmclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.application.App;
import com.rbddevs.splashy.Splashy;

import java.util.Collections;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 199;
    private static final long DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (App.isFirstTime()) {
            new Splashy(this)  // For JAVA : new Splashy(this)
                    .setLogo(R.drawable.clouds2)
                    .setDuration(DURATION)
                    .setTitle("Remote Alarm")
                    .setTitleSize(30.0f)
                    .setTitleColor(R.color.colorAccent)
                    .setFullScreen(true)
                    .setAnimation(Splashy.Animation.SLIDE_IN_TOP_BOTTOM, DURATION / 2)
                    .setProgressColor(R.color.white)
                    .show();

            new Handler().postDelayed(this::login, DURATION);
            App.setFirstTime(false);
        } else {
            login();
        }
        super.onCreate(savedInstanceState);
    }

    private void login() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag", "onActivityResult: ");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                finish();
            }
        }
    }
}
