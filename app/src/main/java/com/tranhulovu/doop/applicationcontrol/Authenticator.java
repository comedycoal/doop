package com.tranhulovu.doop.applicationcontrol;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authenticator {
    public enum SignInState {
        SIGNED_IN,
        NOT_SIGNED_IN
    }

    private SignInState mSignInState;
    private final FirebaseAuth mFirebaseAuth;

    public Authenticator() {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void setSignInState(SignInState state) {
        mSignInState = state;
    }

    /**
     * This validates user login information.
     * @param email: same as above.
     * @param password: same as above.
     */
    public void requestOnlineSignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setSignInState(SignInState.SIGNED_IN);
                            // TODO: Toast/Dialog for success
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error signin in with exception", e);
                        setSignInState(SignInState.NOT_SIGNED_IN);
                        // TODO: Toast/Dialog for failure
                    }
                });
    }

    public FirebaseUser getCurrentUser() {
        if (getSignInState() == SignInState.SIGNED_IN) {
            return mFirebaseAuth.getCurrentUser();
        }
        return null;
    }

    public void requestSignOut() {
        mFirebaseAuth.signOut();
    }

    public SignInState getSignInState() {
        return mSignInState;
    }
}
