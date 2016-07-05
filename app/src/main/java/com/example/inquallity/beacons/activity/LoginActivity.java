package com.example.inquallity.beacons.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.presenter.LoginPresenter;
import com.example.inquallity.beacons.view.LoginView;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

/**
 * @author Maksim Radko
 */
public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private static final String PROXIMITY_AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    private static final String TAG = LoginActivity.class.getName();

    private static final int RC_SIGN_IN = 1;

    private static final int RC_PERMISSIONS = 2;

    private TextView mCurrentLogin;

    private View mContinue;

    private LoginPresenter mPresenter;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnContinue) {
            mPresenter.findAccountAndUpdate(PreferenceManager.getDefaultSharedPreferences(this),
                    (AccountManager) getSystemService(ACCOUNT_SERVICE));
        }
    }

    @Override
    public void showCurrentLogin(String currentLogin) {
        mCurrentLogin.setText(currentLogin);
        findGoogleAccount();
    }

    @Override
    public void showLoginChooser() {
        final Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
                false, null, null, null, null);
        startActivityForResult(intent, RC_SIGN_IN);
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
            findGoogleAccount();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Bundle extras = data.getExtras();
        if (requestCode == RC_SIGN_IN && extras != null) {
            final String accountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
            mPresenter.updateCurrentAccount(PreferenceManager.getDefaultSharedPreferences(this), accountName);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        mPresenter = new LoginPresenter(this);

        mCurrentLogin = (TextView) findViewById(R.id.tvCurrentLogin);
        mContinue = findViewById(R.id.btnContinue);
        mContinue.setOnClickListener(this);

        mPresenter.checkLogin(PreferenceManager.getDefaultSharedPreferences(this));
    }

    private void findGoogleAccount() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, RC_PERMISSIONS);
        } else {
            mPresenter.findAccountAndUpdate(PreferenceManager.getDefaultSharedPreferences(this), AccountManager.get(this));
        }
    }
}
