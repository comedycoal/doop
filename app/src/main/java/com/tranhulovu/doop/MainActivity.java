package com.tranhulovu.doop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tranhulovu.doop.applicationcontrol.Authenticator;
import com.tranhulovu.doop.applicationcontrol.SettingManager;
import com.tranhulovu.doop.applicationcontrol.UserManager;
import com.tranhulovu.doop.todocardsystem.CardManager;
import com.tranhulovu.doop.todocardsystem.NotificationManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static Authenticator mAuthenticator;
    public static Authenticator getAuthenticatorInstance() {
        if (mAuthenticator == null) {
            mAuthenticator = new Authenticator();
        }
        return  mAuthenticator;
    }

    private static UserManager mUserManager;
    public static UserManager getUserManagerInstance() {
        if (mUserManager == null) {
            mUserManager = new UserManager();
        }
        return  mUserManager;
    }

    private static SettingManager mSettingManager;
    public static SettingManager getSettingManagerInstance() {
        if (mSettingManager == null) {
            mSettingManager = new SettingManager();
        }
        return mSettingManager;
    }

    private static CardManager mCardManager;
    public static CardManager getCardManagerInstance() {
        if (mCardManager == null) {
            mCardManager = new CardManager();
        }
        return mCardManager;
    }

    private static NotificationManager mNotificationManager;
    public static NotificationManager getNotificationManagerInstance() {
        if (mNotificationManager == null) {
            mNotificationManager = new NotificationManager();
        }
        return mNotificationManager;
    }

    public void initialize() {

    }

//    public void onNavigateTo(ManagedFragment dest) {
//
//    }

    public void finalize() {

    }
}