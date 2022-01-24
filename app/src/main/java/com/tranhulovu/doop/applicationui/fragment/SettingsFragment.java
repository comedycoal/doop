package com.tranhulovu.doop.applicationui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tranhulovu.doop.MainActivity;
import com.google.android.material.card.MaterialCardView;
import com.tranhulovu.doop.R;

public class SettingsFragment extends ManagerFragment {
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settingsfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView notification = view.findViewById(R.id.settings_notification);
        ImageView notificationIcon = view.findViewById(R.id.settings_notification_icon);
        TextView notificationState = view.findViewById(R.id.settings_notification_state);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationState.getText() == "ON"){
                    notificationIcon.setImageResource(R.drawable.ic_notification_off);
                    notificationState.setText("OFF");
                }
                else {
                    notificationIcon.setImageResource(R.drawable.ic_notification_on);
                    notificationState.setText("ON");
                }
            }
        });

        MaterialCardView autoArchiveCard = view.findViewById(R.id.settings_autoArchiveCard);
        ImageView autoArchiveCardIcon = view.findViewById(R.id.settings_autoArchiveCard_icon);
        TextView autoArchiveCardState = view.findViewById(R.id.settings_autoArchiveCard_state);

        autoArchiveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoArchiveCardState.getText() == "ON"){
                    autoArchiveCardIcon.setImageResource(R.drawable.ic_auto_archive_card_off);
                    autoArchiveCardState.setText("OFF");
                }
                else {
                    autoArchiveCardIcon.setImageResource(R.drawable.ic_auto_archive_card_on);
                    autoArchiveCardState.setText("ON");
                }
            }
        });

        MaterialCardView timeSetting = view.findViewById(R.id.settings_timeSetting);
        timeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout timesetting = view.findViewById(R.id.settings_timeSetting_timeformat_layout);
                RelativeLayout datesetting = view.findViewById(R.id.settings_timeSetting_dateformat_layout);
                timesetting.setVisibility((timesetting.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                datesetting.setVisibility((datesetting.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            }
        });

        MaterialCardView about = view.findViewById(R.id.settings_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView about_info = view.findViewById(R.id.settings_about_info);
                about_info.setVisibility((about_info.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            }
        });

        navController = Navigation.findNavController(view);
        MaterialCardView logoutButton = view.findViewById(R.id.settings_logOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getAuthenticator().requestSignOut();
                navController.navigate(R.id.action_mainFragment_to_authenticatorFragment);
            }
        });

    }
}
