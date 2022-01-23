package com.tranhulovu.doop.applicationcontrol;

import com.tranhulovu.doop.onlinedatabase.OnlineDatabaseAccessor;
import com.tranhulovu.doop.onlinedatabase.Statistics;
import com.tranhulovu.doop.onlinedatabase.UserProfile;

public class UserManager {
    private final OnlineDatabaseAccessor mOnlineDatabaseAccessor;
    private final Authenticator.SignInState signInState;

    public UserManager(Authenticator authenticator) {
        signInState = authenticator.getSignInState();
        mOnlineDatabaseAccessor = new OnlineDatabaseAccessor(authenticator);
    }

    public UserProfile getUserProfile() {
        if (signInState == Authenticator.SignInState.SIGNED_IN) {
            return mOnlineDatabaseAccessor.getUserProfile();
        }
        return null;
    }

    public Statistics getUserStatistics() {
        if (signInState == Authenticator.SignInState.SIGNED_IN) {
            return mOnlineDatabaseAccessor.getUserStatistics();
        }
        return null;
    }

    public void updateUserInfo(String id, Object value) {
        if (signInState == Authenticator.SignInState.SIGNED_IN) {
            mOnlineDatabaseAccessor.modifyUserProfile(id, value);
        }
    }
}
