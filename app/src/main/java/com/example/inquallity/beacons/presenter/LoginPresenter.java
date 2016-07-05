package com.example.inquallity.beacons.presenter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.inquallity.beacons.api.Provider;
import com.example.inquallity.beacons.view.LoginView;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Maksim Radko
 */
public class LoginPresenter {

    private static final String TAG = LoginPresenter.class.getName();

    private static final String KEY_LOGIN = "CURRENT_LOGIN";

    @NonNull
    private final LoginView mView;

    public LoginPresenter(@NonNull LoginView view) {

        mView = view;
    }

    public void checkLogin(SharedPreferences sp) {
        final String currentLogin = sp.getString(KEY_LOGIN, "");
        if (!TextUtils.isEmpty(currentLogin)) {
            mView.showCurrentLogin(currentLogin);
        } else {
            mView.showLoginChooser();
        }
    }

    public void updateCurrentAccount(SharedPreferences sp, String accountName) {
        sp.edit().putString(KEY_LOGIN, accountName).apply();
        checkLogin(sp);
    }

    public void findAccountAndUpdate(SharedPreferences sp, AccountManager am) {
        final String accountName = sp.getString(KEY_LOGIN, "");
        if (!TextUtils.isEmpty(accountName)) {
            final Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            for (Account acc : accounts) {
                if (TextUtils.equals(acc.name, accountName)) {
                    mView.dispatchWithName(acc);
                }
            }
        } else {
            Log.e(TAG, "Error while findAccount");
        }
    }

    public void getToken(@NonNull final Context context, @NonNull final Account account, @NonNull final String scope) {
        final Context appCtx = context.getApplicationContext();
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            final String token = GoogleAuthUtil.getToken(appCtx, account, scope);
                            subscriber.onNext(token);
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        } catch (UserRecoverableAuthException e) {
                            final Intent intent = e.getIntent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mView.startAuthActivity(intent);
                        } catch (GoogleAuthException e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Provider.refreshKey(s);
                        mView.showMainActivity();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                });
    }
}
