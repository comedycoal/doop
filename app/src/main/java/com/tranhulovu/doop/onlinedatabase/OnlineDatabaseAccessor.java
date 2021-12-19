package com.tranhulovu.doop.onlinedatabase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

public class OnlineDatabaseAccessor {
    private final FirebaseAuth mFirebaseAuth;
    private final FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private String mUserID;
    private UserProfile mCachedUSerProfile;
    private Statistics mCachedStatistics;

    public OnlineDatabaseAccessor(String email, String password) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        authorizeOnlineSignIn(email, password);
        if (mFirebaseUser != null) {
            mUserID = mFirebaseUser.getUid();
            String fullName = mFirebaseUser.getDisplayName();
            Uri profileImageUri = mFirebaseUser.getPhotoUrl();
            Date userJoinDate = new Date(Objects.requireNonNull(mFirebaseUser.getMetadata()).getCreationTimestamp());
            mCachedUSerProfile = new UserProfile(mUserID, fullName, profileImageUri, userJoinDate);
        }

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("statistics")
                .document(mUserID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (Objects.requireNonNull(documentSnapshot).exists()) {
                                mCachedStatistics = new Statistics(mUserID, Objects.requireNonNull(documentSnapshot.getData()));
                            }
                        }
                    }
                });
    }

    public void syncUserProfile() {
        // Sync what???
    }

    public void syncUserStatistics() {
        // Same as above
    }

    public void authorizeOnlineSignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithEmail:failure", task.getException());
                            // TODO: Show failed login dialog
                        }
                    }
                });
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }

    public String getOnlineUserID() {
        return mUserID;
    }

    public UserProfile getUSerProfile() {
        return mCachedUSerProfile;
    }

    public  Statistics getUserStatistics() {
        return mCachedStatistics;
    }

    public void modifyUserProfile(UserProfile newProfile) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(newProfile.getFullName())
                                                            .setPhotoUri(newProfile.getUserProfileImageUri())
                                                            .build();
        mFirebaseUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // TODO: Show successful messages
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: Show error messages
                    }
                });
    }

    public void modifyUserProfile(String id, Object value) {
        // Dunno what should be here
    }

    public void registerChangedStatistics(Statistics newStats) {
        mFirebaseFirestore.collection("statistics")
                .document(mUserID).set(newStats)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // TODO: Show successful messages
                        }
                    }
                });
    }
}
