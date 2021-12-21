package com.tranhulovu.doop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tranhulovu.doop.applicationcontrol.Authenticator;
import com.tranhulovu.doop.applicationcontrol.SettingManager;
import com.tranhulovu.doop.applicationcontrol.UserManager;
import com.tranhulovu.doop.applicationui.ViewPagerAdapter;
import com.tranhulovu.doop.todocardsystem.CardManager;
import com.tranhulovu.doop.todocardsystem.NotificationManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private ViewPager2 mViewpager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mNavigationView = findViewById(R.id.bottom_nav);
//        mViewpager2 = findViewById(R.id.view_pager);
//
//        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
//        mViewpager2.setAdapter(viewPagerAdapter);
//
//        mNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id=item.getItemId();
//                switch (id){
//                    case R.id.action_tasks:
//                        mViewpager2.setCurrentItem(0);
//                        break;
//                    case R.id.action_statistics:
//                        mViewpager2.setCurrentItem(1);
//                        break;
//                    case R.id.action_settings:
//                        mViewpager2.setCurrentItem(2);
//                        break;
//                }
//                return true;
//            }
//        });
//
//        mViewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position){
//                    case 0:
//                        mNavigationView.getMenu().findItem(R.id.action_tasks).setChecked(true);
//                        break;
//                    case 1:
//                        mNavigationView.getMenu().findItem(R.id.action_statistics).setChecked(true);
//                        break;
//                    case 2:
//                        mNavigationView.getMenu().findItem(R.id.action_settings).setChecked(true);
//                        break;
//                }
//            }
//        });
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
            //mCardManager = new CardManager(); // CONSTRUCTOR CHANGED, sorry
        }
        return mCardManager;
    }

//    private static NotificationManager mNotificationManager;
//    public static NotificationManager getNotificationManagerInstance() {
//        if (mNotificationManager == null) {
//            mNotificationManager = new NotificationManager();
//        }
//        return mNotificationManager;
//    }

    public void initialize() {
    }

//    public void onNavigateTo(ManagedFragment dest) {
//
//    }

    public void finalize() {

    }



}