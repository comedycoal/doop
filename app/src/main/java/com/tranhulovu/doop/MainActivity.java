package com.tranhulovu.doop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tranhulovu.doop.applicationcontrol.Authenticator;
import com.tranhulovu.doop.applicationcontrol.SettingManager;
import com.tranhulovu.doop.applicationcontrol.UserManager;

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

    private String email;
    private String password;

    private Authenticator mAuthenticator = new Authenticator(email, password);

    private UserManager mUserManager = new UserManager(email, password);

    private SettingManager mSettingManager = new SettingManager(getApplicationContext());

//    private CardManager mCardManager = new CardManager();

//    private NotificationManager mNotificationManager;

    public void initialize() {
    }

//    public void onNavigateTo(ManagedFragment dest) {
//
//    }

    public void finalize() {

    }



}