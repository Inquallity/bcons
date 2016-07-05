package com.example.inquallity.beacons.view;

import android.accounts.Account;
import android.content.Intent;

/**
 * @author Maksim Radko
 */
public interface LoginView {

    void showCurrentLogin(String currentLogin);

    void showLoginChooser();

    void dispatchWithName(Account account);

    void showMainActivity();

    void startAuthActivity(Intent intent);
}
