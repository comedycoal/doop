package com.tranhulovu.doop.applicationcontrol;

import com.tranhulovu.doop.onlinedatabase.OnlineDatabaseAccessor;

public class Authenticator {
    public enum SignInState {
        ONLINE_SIGN_IN,
        LOCAL_SIGN_IN,
        NOT_SIGN_IN
    }

    private SignInState mSignInState;
    OnlineDatabaseAccessor mOnlineDatabseAccessor;

    public Authenticator(String email, String password) {
        mOnlineDatabseAccessor = new OnlineDatabaseAccessor(email, password);
    }

    public void setSignInState(SignInState state) {
        mSignInState = state;
    }

    public void requestOnlineSignIn() {
        if (!mOnlineDatabseAccessor.getOnlineUserID().equals("")) {
            setSignInState(SignInState.ONLINE_SIGN_IN);
        } else {
            setSignInState(SignInState.NOT_SIGN_IN);
        }
    }

    public void requestLocalSignIn() {
        // TODO: Local auth
    }

    public void requestSignOut() {
        mOnlineDatabseAccessor.signOut();
    }

    public SignInState getSignInState() {
        return mSignInState;
    }
}
