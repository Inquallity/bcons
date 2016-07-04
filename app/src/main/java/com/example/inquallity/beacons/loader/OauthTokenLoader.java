package com.example.inquallity.beacons.loader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * @author Maksim Radko
 */
public class OauthTokenLoader extends AsyncTaskLoader<String> {

    private static final String TAG = OauthTokenLoader.class.getName();

    private static final String PROXIMITY_AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    private final String mAccountName;

    private String mCachedToken;

    public OauthTokenLoader(Context context, String accountName) {
        super(context);
        mAccountName = accountName;
    }

    @Override
    public void deliverResult(String data) {
        if (isReset()) {
            mCachedToken = null;
            return;
        }
        if (!TextUtils.isEmpty(data)) {
            mCachedToken = data;
        }
        if (isStarted()) {
            super.deliverResult(data);
        }

    }

    @Override
    public String loadInBackground() {
        final AccountManager am = (AccountManager) getContext().getSystemService(Context.ACCOUNT_SERVICE);
        final Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accounts.length == 0) {
            Toast.makeText(getContext(), "accounts length is 0", Toast.LENGTH_SHORT).show();
        } else {
            for (Account account : accounts) {
                if (mAccountName.equalsIgnoreCase(account.name)) {
                    try {
                        mCachedToken = GoogleAuthUtil.getToken(getContext(), account, PROXIMITY_AUTH_SCOPE);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    } catch (UserRecoverableAuthException e) {
                        final Intent intent = e.getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } catch (GoogleAuthException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mCachedToken;
    }

    @Override
    protected void onStartLoading() {
        if (TextUtils.isEmpty(mCachedToken)) {
            forceLoad();
        } else {
            deliverResult(mCachedToken);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        mCachedToken = null;
    }
}
