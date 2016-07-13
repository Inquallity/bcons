package com.example.inquallity.beacons.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.presenter.SplashPresenter;
import com.example.inquallity.beacons.view.SplashView;

/**
 * @author Maksim Radko
 */
public class SplashActivity extends AppCompatActivity implements SplashView {

    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startActivity(LoginActivity.makeIntent(this));
        finish();
    }
}
