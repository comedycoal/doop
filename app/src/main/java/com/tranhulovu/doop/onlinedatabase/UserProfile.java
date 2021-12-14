package com.tranhulovu.doop.onlinedatabase;

import android.net.Uri;

import java.util.Date;

public class UserProfile {
    private final String mUserID;
    private final String mFullName;
    private final Uri mUserProfileImageUri;
    private final Date mUserJoinDate;

    public UserProfile(String userID, String fullName, Uri userProfileImageUri, Date userJoinDate) {
        mUserID = userID;
        mFullName = fullName;
        mUserProfileImageUri = userProfileImageUri;
        mUserJoinDate = userJoinDate;
    }

    public String getUserID() {
        return mUserID;
    }

    public String getFullName() {
        return mFullName;
    }

    public Uri getUserProfileImageUri() {
        return mUserProfileImageUri;
    }

    public Date getUserJoinDate() {
        return mUserJoinDate;
    }
}
