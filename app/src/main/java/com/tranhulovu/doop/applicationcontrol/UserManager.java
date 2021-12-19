package com.tranhulovu.doop.applicationcontrol;

import com.tranhulovu.doop.onlinedatabase.Statistics;
import com.tranhulovu.doop.onlinedatabase.UserProfile;

public class UserManager {
    private UserProfile mUserProfile;

    public UserProfile getUserProfile() {
        return mUserProfile;
    }

    public Statistics getUserStatistics() {
        return null;
    }

    public void updateUserInfo(String name, Object value) {

    }
}
