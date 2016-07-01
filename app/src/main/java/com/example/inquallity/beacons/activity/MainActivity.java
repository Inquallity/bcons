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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inquallity.beacons.OauthTokenLoader;
import com.example.inquallity.beacons.Observations;
import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.api.Bcons;
import com.example.inquallity.beacons.api.Provider;
import com.example.inquallity.beacons.presenter.MainPresenter;
import com.example.inquallity.beacons.view.MainView;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>, View.OnClickListener, LoaderManager.LoaderCallbacks<String>, Callback<Observations> {

    private static final String TAG = MainActivity.class.getName();

    private static final int RC_SIGN_IN = 1;

    private static final int RC_PERMISSIONS = 2;

    private MainPresenter mPresenter;

    private GoogleApiClient mApiClient;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    @NonNull
    private SubscribeOptions mSubscribeOptions = SubscribeOptions.DEFAULT;

    private TextView mMessages;

    private MessageListener mMessageListener = new MessageListener() {
        @Override
        public void onFound(Message message) {
            Log.d(TAG, "onFound: msg: " + message);
            Log.d(TAG, "onFound: msg content: " + new String(message.getContent()));
            Log.d(TAG, "onFound: namespace/type: " + message.getNamespace() + "/ " + message.getType());
            final long currentTime = System.currentTimeMillis();
            final Date date = new Date(currentTime);
            mMessages.append(message.toString() + "\n" + message.getNamespace() + " "
                    + message.getType() + ":" + new String(message.getContent()) + "\n"
                    + "----------------------------" + mDateFormat.format(date));
        }

        @Override
        public void onLost(Message message) {
            super.onLost(message);
            Log.d(TAG, "onLost: " + new String(message.getContent()));
        }
    };

    private String mAccountName;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:");
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.d(TAG, "onResult: subscribe. " + status.getStatusMessage());
    }

    public void onProceedClick(View view) {
        Nearby.Messages.subscribe(mApiClient, mMessageListener, mSubscribeOptions)
                .setResultCallback(this);
        Log.d(TAG, "onProceedClick: ");
    }

    @Override
    public void onClick(View view) {
//        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
        final Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
        startActivityForResult(intent, RC_SIGN_IN);

    }

    public void onSignOutClick(View view) {
        Auth.GoogleSignInApi.signOut(mApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.d(TAG, "onResult: " + status.getStatusMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSIONS && grantResults.length > 0) {
            findGoogleAccount();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new OauthTokenLoader(this, mAccountName);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        Toast.makeText(this, "Oauth2 token is: " + s, Toast.LENGTH_LONG).show();
        Provider.provide(s).create(Bcons.class)
                .fetchObservations()
                .enqueue(this);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onResponse(Call<Observations> call, Response<Observations> response) {
        Log.d(TAG, "onResponse: ");
        final Observations body = response.body();
        if (body != null) {
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<Observations> call, Throwable t) {
        Log.e(TAG, t.getMessage(), t);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                mAccountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
                findGoogleAccount();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mPresenter = new MainPresenter(this);
//        mApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Nearby.MESSAGES_API)
//                .addConnectionCallbacks(this)
//                .enableAutoManage(this, this)
//                .build();
        mMessages = (TextView) findViewById(R.id.tvMessages);
        final SignInButton signInButton = (SignInButton) findViewById(R.id.btnSignIn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        //BLE Only
        final GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestServerAuthCode(getString(R.string.client_id))
                .requestEmail()
                .build();
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API,
                        new MessagesOptions.Builder()
                                .setPermissions(NearbyPermissions.BLE)
                                .build())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
        mSubscribeOptions = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .setCallback(new SubscribeCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.d(TAG, "onExpired: ");
                    }
                })
                .build();
    }

    @Override
    protected void onStop() {
        Nearby.Messages.unsubscribe(mApiClient, mMessageListener);
        super.onStop();
    }

    private void findGoogleAccount() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, RC_PERMISSIONS);
        } else {
            getLoaderManager().initLoader(R.id.oauth_token_loader, Bundle.EMPTY, this);
        }
    }

}
