package com.learntodroid.simplealarmclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.learntodroid.simplealarmclock.R;
import com.rbddevs.splashy.Splashy;

import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

public class LaunchActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 199;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        new Handler().postDelayed(() -> {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }, 1000);
        super.onCreate(savedInstanceState);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
      //  finish();
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
