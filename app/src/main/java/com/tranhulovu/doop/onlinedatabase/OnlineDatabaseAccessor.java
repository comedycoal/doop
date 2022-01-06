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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnlineDatabaseAccessor {
    private final FirebaseAuth mFirebaseAuth;
    private final FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private String mUserID;
    private UserProfile mCachedUserProfile;
    private Statistics mCachedStatistics;

    /**
    * This init an Online Database Accessor to manipulate data stored in Firebase with
    * @param email: user's email.
     * @param password: user's password.
     * Also, this class init will cache user info and statistics from Firebase for future use.
     */
    public OnlineDatabaseAccessor(String email, String password) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        authorizeOnlineSignIn(email, password);
        if (mFirebaseUser != null) {
            mUserID = mFirebaseUser.getUid();
            String fullName = mFirebaseUser.getDisplayName();
            Uri profileImageUri = mFirebaseUser.getPhotoUrl();
            Date userJoinDate = new Date(Objects.requireNonNull(mFirebaseUser.getMetadata()).getCreationTimestamp());
            mCachedUserProfile = new UserProfile(mUserID, fullName, profileImageUri, userJoinDate);
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
                                Map<String, Object> data = Objects.requireNonNull(documentSnapshot.getData());
                                @SuppressWarnings("unchecked") Map<String, Object> lastWeek = (Map<String, Object>) data.get("lastWeek");
                                @SuppressWarnings("unchecked") Map<String, Object> lastMonth = (Map<String, Object>) data.get("lastMonth");
                                @SuppressWarnings("unchecked") Map<String, Object> total = (Map<String, Object>) data.get("total");
                                mCachedStatistics = new Statistics(mUserID, lastWeek, lastMonth, total);
                            }
                        }
                    }
                });
    }

    /**
     * This validate user login information.
     * @param email: same as above.
     * @param password: same as above.
     */
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error logging in with exception", e);
                    }
                });
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }

    public String getOnlineUserID() {
        return mUserID;
    }

    public UserProfile getUserProfile() {
        return mCachedUserProfile;
    }

    public  Statistics getUserStatistics() {
        return mCachedStatistics;
    }

    /**
     * This will modify user profile with an updated one.
     * @param newProfile: new profile info.
     */
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
        UserProfile userProfile;
        if (value instanceof String) {
            userProfile = new UserProfile(id, (String) value, mCachedUserProfile.getUserProfileImageUri(), null);
            modifyUserProfile(userProfile);
        }
        else if (value instanceof Uri) {
            userProfile = new UserProfile(id, mCachedUserProfile.getFullName(), (Uri) value, null);
            modifyUserProfile(userProfile);
        }
    }

    /**
     * This will change user's statistics.
     * @param newStats: new satistics.
     * This will replace the existing one if it already exists.
     */
    public void registerChangedStatistics(Statistics newStats) {
        if (mCachedStatistics != null) {
            Map<String, Object> deleteFields = new HashMap<>();
            deleteFields.put("lastWeek", FieldValue.delete());
            deleteFields.put("lastMonth", FieldValue.delete());
            deleteFields.put("total", FieldValue.delete());
            mFirebaseFirestore.collection("statistics")
                    .document(mUserID).update(deleteFields)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Do nothing
                        }
                    });
        }
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
