package com.tranhulovu.doop.applicationcontrol;

import com.tranhulovu.doop.onlinedatabase.OnlineDatabaseAccessor;
import com.tranhulovu.doop.onlinedatabase.Statistics;
import com.tranhulovu.doop.onlinedatabase.UserProfile;

public class UserManager {
    private final OnlineDatabaseAccessor mOnlineDatabaseAccessor;
    private final Authenticator mAuth;


    public UserManager(String email, String password) {
        mAuth = new Authenticator(email, password);
        mOnlineDatabaseAccessor = new OnlineDatabaseAccessor(email, password);
    }

    public UserProfile getUserProfile() {
        if (mAuth.getSignInState() == Authenticator.SignInState.ONLINE_SIGN_IN
                    || mAuth.getSignInState() == Authenticator.SignInState.LOCAL_SIGN_IN) {
            return mOnlineDatabaseAccessor.getUserProfile();
        }
        return null;
    }

    public Statistics getUserStatistics() {
        if (mAuth.getSignInState() == Authenticator.SignInState.ONLINE_SIGN_IN
                || mAuth.getSignInState() == Authenticator.SignInState.LOCAL_SIGN_IN) {
            return mOnlineDatabaseAccessor.getUserStatistics();
        }
        return null;
    }

    public void updateUserInfo(String id, Object value) {
        if (mAuth.getSignInState() == Authenticator.SignInState.ONLINE_SIGN_IN
                || mAuth.getSignInState() == Authenticator.SignInState.LOCAL_SIGN_IN) {
            mOnlineDatabaseAccessor.modifyUserProfile(id, value);
        }
    }
}
