package com.tranhulovu.doop.applicationcontrol;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.onlinedatabase.OnlineDatabaseAccessor;

public class Authenticator {
    public enum SignInState {
        SIGNED_IN,
        SIGNED_UP,
        NOT_SIGNED_IN
    }

    public static final String auhTAG = "Authenticator";

    private SignInState mSignInState;
    private final FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public Authenticator() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
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
                        }
                        else {
                            Log.d(auhTAG, "signin:failure");
                            setSignInState(SignInState.NOT_SIGNED_IN);
                        }
                    }
                });
    }

    public void requestSignUp(String email, String password, String username) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(auhTAG, "signup:success");
                            setSignInState(SignInState.SIGNED_UP);
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .setPhotoUri(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                            + "://" + MainActivity.getInstance().getResources().getResourcePackageName(R.drawable.ic_account_circle)
                                            + '/' + MainActivity.getInstance().getResources().getResourceTypeName(R.drawable.ic_account_circle)
                                            + '/' + MainActivity.getInstance().getResources().getResourceEntryName(R.drawable.ic_account_circle)))
                                    .build();
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            mFirebaseUser.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.getInstance(), "Account created successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(MainActivity.getInstance(), "Failed create account", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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
