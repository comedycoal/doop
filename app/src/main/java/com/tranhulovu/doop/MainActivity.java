package com.tranhulovu.doop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tranhulovu.doop.applicationcontrol.Authenticator;
import com.tranhulovu.doop.applicationcontrol.SettingManager;
import com.tranhulovu.doop.applicationcontrol.UserManager;
import com.tranhulovu.doop.localdatabase.LocalAccessorFacade;
import com.tranhulovu.doop.todocardsystem.CardManager;
import com.tranhulovu.doop.todocardsystem.NotificationManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private ViewPager2 mViewpager2;

    private static MainActivity instance;

    public static MainActivity getInstance()
    {
        if (instance == null)
        {
            throw new RuntimeException("Null MainActivity");
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

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

    private String email = "123@test.com";
    private String password = "123456";

    private Authenticator mAuthenticator = new Authenticator(email, password);

    private UserManager mUserManager = new UserManager(email, password);

    private SettingManager mSettingManager = new SettingManager(this);

    private LocalAccessorFacade mAccessor = new LocalAccessorFacade();
    private NotificationManager mNotifManager = new NotificationManager(mAccessor, this);
    private CardManager mCardManager = new CardManager(mNotifManager, mAccessor);

    public CardManager getCardManager()
    {
        return mCardManager;
    }

    public void initialize()
    {

    }

//    public void onNavigateTo(ManagedFragment dest) {
//
//    }

    public void finalize()
    {

    }



}