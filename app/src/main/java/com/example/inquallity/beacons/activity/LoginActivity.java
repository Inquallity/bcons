package com.example.inquallity.beacons.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.presenter.LoginPresenter;
import com.example.inquallity.beacons.view.LoginView;
import com.google.android.gms.auth.GoogleAuthUtil;

/**
 * @author Maksim Radko
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = LoginActivity.class.getName();

    private static final String PROXIMITY_AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    private static final int RC_SIGN_IN = 0;

    private static final int RC_PERMISSIONS = 2;

    private SharedPreferences mPreferences;

    private TextView mCurrentLogin;

    private LoginPresenter mPresenter;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void showCurrentLogin(String currentLogin) {
        mCurrentLogin.setText(currentLogin);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            mPresenter.findAccountAndUpdate(mPreferences, AccountManager.get(this));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, RC_PERMISSIONS);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void showLoginChooser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, RC_PERMISSIONS);
        } else {
            final Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intent = AccountManager.newChooseAccountIntent(null, null,
                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, null, null, null, null);
            } else {
                intent = AccountManager.newChooseAccountIntent(null, null,
                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            }
            startActivityForResult(intent, RC_SIGN_IN);
        }
    }

    @Override
    public void dispatchWithName(Account account) {
        mPresenter.getToken(this, account, PROXIMITY_AUTH_SCOPE);
    }

    @Override
    public void showMainActivity() {
        startActivity(MainActivity.makeIntent(this));
    }

    @Override
    public void startAuthActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSIONS && grantResults.length > 0) {
            mPresenter.findAccountAndUpdate(mPreferences, AccountManager.get(this));
        } else {
            Log.d(TAG, "onRequestPermissionsResult: ERROR");
        }
    }

    public void onContinueClick(View view) {
        mPresenter.checkLogin(mPreferences);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Bundle extras = data.getExtras();
        if (requestCode == RC_SIGN_IN && extras != null) {
            final String accountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
            mPresenter.updateCurrentAccount(mPreferences, accountName);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        mPresenter = new LoginPresenter(this);

        mCurrentLogin = (TextView) findViewById(R.id.tvCurrentLogin);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

}
