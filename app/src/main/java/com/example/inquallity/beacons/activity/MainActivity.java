package com.example.inquallity.beacons.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.api.Provider;
import com.example.inquallity.beacons.loader.OauthTokenLoader;
import com.example.inquallity.beacons.presenter.MainPresenter;
import com.example.inquallity.beacons.view.MainView;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int RC_SIGN_IN = 1;

    private static final int RC_PERMISSIONS = 2;

    private static final MessagesOptions MESSAGES_OPTIONS = new MessagesOptions.Builder()
            .setPermissions(NearbyPermissions.BLE).build();

    private static final SubscribeOptions SUBSCRIBE_OPTIONS = new SubscribeOptions.Builder().setStrategy(Strategy.BLE_ONLY).build();

    private MainPresenter mPresenter;

    private GoogleApiClient mApiClient;

    private TextView mMessages;

    private String mAccountName;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    public void onProceedClick(View view) {
//        Nearby.Messages.subscribe(mApiClient, mPresenter, SUBSCRIBE_OPTIONS).setResultCallback(null);
        startActivity(ProbeActivity.makeIntent(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSIONS && grantResults.length > 0) {
            findGoogleAccount();
        }
    }

    public void onSignInClick(View view) {
        final Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    public void onSignOutClick(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Bundle extras = data.getExtras();
        if (requestCode == RC_SIGN_IN && extras != null) {
            mAccountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
            findGoogleAccount();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mPresenter = new MainPresenter(this);
        mMessages = (TextView) findViewById(R.id.tvMessages);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API, MESSAGES_OPTIONS)
                .addConnectionCallbacks(mPresenter)
                .enableAutoManage(this, mPresenter)
                .build();
    }

    @Override
    protected void onStop() {
        Nearby.Messages.unsubscribe(mApiClient, mPresenter);
        super.onStop();
    }

    private void findGoogleAccount() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, RC_PERMISSIONS);
        } else {
            getLoaderManager().initLoader(R.id.oauth_token_loader, Bundle.EMPTY, new TokenLoaderCallbacks());
        }
    }

    private class TokenLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new OauthTokenLoader(MainActivity.this, mAccountName);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            Provider.refreshKey(data);
            startActivity(SettingsActivity.makeIntent(MainActivity.this));
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            if (loader != null && !loader.isAbandoned()) {
                loader.reset();
            }
        }
    }
}
