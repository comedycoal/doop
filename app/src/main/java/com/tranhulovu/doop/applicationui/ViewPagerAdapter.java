package com.tranhulovu.doop.applicationui;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tranhulovu.doop.applicationui.fragment.MainFragment;
import com.tranhulovu.doop.applicationui.fragment.ManagerFragment;
import com.tranhulovu.doop.applicationui.fragment.SettingsFragment;
import com.tranhulovu.doop.applicationui.fragment.StatisticsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //ManagerFragment managerFragment;
        switch (position){
            case 0:
                return new MainFragment();
            case 1:
                return new StatisticsFragment();
            case 2:
                return new SettingsFragment();
            default:
                return new MainFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
