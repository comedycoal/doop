package com.tranhulovu.doop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tranhulovu.doop.applicationcontrol.Authenticator;
import com.tranhulovu.doop.applicationcontrol.SettingManager;
import com.tranhulovu.doop.applicationcontrol.UserManager;
import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.onlinedatabase.OnlineDatabaseAccessor;
import com.tranhulovu.doop.todocardsystem.CardManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private ViewPager2 mViewpager2;

    private static MainActivity instance;

    private String CHANNEL_ID = "com.doop.tranhulovu";

    public static MainActivity getInstance() {
        if (instance == null) {
            //throw new RuntimeException("Null MainActivity");
            instance = new MainActivity();
        }
        return instance;
    }

    public String getAppNotificationChannelId()
    {
        return CHANNEL_ID;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "com.tranhulovu.doop";
            String description = "Notification channel for com.tranhulovu.doop";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.activity_main);

        createNotificationChannel();
        initialize();

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

    private final Authenticator mAuthenticator = new Authenticator();
    private final UserManager mUserManager = new UserManager(mAuthenticator);
    private final OnlineDatabaseAccessor mOnlineDatabaseAccessor = new OnlineDatabaseAccessor(mAuthenticator);
    private final SettingManager mSettingManager = new SettingManager(this);

    private final LocalAccessorFacade mAccessor = new LocalAccessorFacade();
    private final com.tranhulovu.doop.todocardsystem.NotificationManager mNotifManager
            = new com.tranhulovu.doop.todocardsystem.NotificationManager(mAccessor, this);
    private final CardManager mCardManager = new CardManager(mNotifManager, mAccessor);

    public static Authenticator getAuthenticator() {
        return getInstance().mAuthenticator;
    }

    public CardManager getCardManager() {
        return mCardManager;
    }

    public static OnlineDatabaseAccessor getOnlineDatabaseAccessor() {
        return getInstance().mOnlineDatabaseAccessor;
    }

    public void initialize() {

    }

//    public void onNavigateTo(ManagedFragment dest) {
//
//    }

    public void finalize() {

    }


}