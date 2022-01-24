package com.tranhulovu.doop.applicationcontrol;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authenticator {
    public enum SignInState {
        SIGNED_IN,
        SIGNED_UP,
        NOT_SIGNED_IN
    }

    public static final String auhTAG = "Authenticator";

    private SignInState mSignInState;
    private final FirebaseAuth mFirebaseAuth;

    public Authenticator() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null) {
            setSignInState(SignInState.SIGNED_IN);
        }
        else {
            setSignInState(SignInState.NOT_SIGNED_IN);
        }
    }

    public void setSignInState(SignInState state) {
        mSignInState = state;
    }

    /**
     * This validates user login information.
     * @param email: same as above.
     * @param password: same as above.
     */
    public void requestSignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(auhTAG, "signin:success");
                            setSignInState(SignInState.SIGNED_IN);
                            // TODO: Toast/Dialog for success
                        }
                        else {
                            Log.d(auhTAG, "signin:failure");
                            setSignInState(SignInState.NOT_SIGNED_IN);
                        }
                    }
                });
    }

    public void requestSignUp(String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(auhTAG, "signup:success");
                            setSignInState(SignInState.SIGNED_UP);
                        }
                        else {
                            Log.d(auhTAG, "signup:failure");
                        }
                    }
                });
    }

    public void requestSignOut() {
        mFirebaseAuth.signOut();
    }

    public SignInState getSignInState() {
        return mSignInState;
    }

    public FirebaseUser getCurrentUser() {
        if (getSignInState() == SignInState.SIGNED_IN) {
            return mFirebaseAuth.getCurrentUser();
        }
        return null;
    }
}
