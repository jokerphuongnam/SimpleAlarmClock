package com.learntodroid.simplealarmclock.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.activities.userinfodialog.UserInfoFragment;
import com.learntodroid.simplealarmclock.alarmslist.AlarmsListFragment;
import com.rbddevs.splashy.Splashy;

import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.app_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.setting) {
                new UserInfoFragment().show(getSupportFragmentManager(), "userInfo");
            }
            return false;
        });
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Nullable
    private Fragment isAlarmsFragment() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_nav_host_fragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    public void onBackPressed() {
        if (isAlarmsFragment() instanceof AlarmsListFragment) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.message_click_back, Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            super.onBackPressed();
        }
    }
}