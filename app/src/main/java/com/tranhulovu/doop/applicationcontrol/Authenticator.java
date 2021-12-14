package com.tranhulovu.doop.applicationcontrol;

import java.util.PrimitiveIterator;

public class Authenticator {
    public enum SignInState {
        ONLINE_SIGN_IN,
        LOCAL_SIGN_IN,
        NOT_SIGN_IN
    }

    private SignInState mSignInState;

    public void setSignInState(SignInState state) {
        mSignInState = state;
    }

    public void requestGoogleSignIn() {

    }

    public void requestLocalSignIn() {

    }

    public void requestSignOut() {

    }

    public SignInState getSignInState() {
        return mSignInState;
    }
}
