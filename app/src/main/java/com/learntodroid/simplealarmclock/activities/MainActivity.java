package com.learntodroid.simplealarmclock.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.activities.userinfodialog.UserInfoFragment;
import com.learntodroid.simplealarmclock.data.Quote;
import com.learntodroid.simplealarmclock.data.QuoteNetwork;
import com.learntodroid.simplealarmclock.data.RetrofitQuoteNetworkImpl;
import com.rbddevs.splashy.Splashy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.quote_tv)
    TextView quoteTV;

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
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getQuoteLiveData().observe(this, observer->{
            quoteTV.setText(observer);
        });
        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.app_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.setting) {
                new UserInfoFragment().show(getSupportFragmentManager(), "userInfo");
            } else {

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
}
