package com.tranhulovu.doop.onlinedatabase;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.applicationcontrol.Authenticator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnlineDatabaseAccessor {
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;
    private String mUserID;
    private UserProfile mCachedUserProfile;
    private Statistics mCachedStatistics;

    private static String onlineDBTAG = "onlinedb";

    /**
    * This init an Online Database Accessor to manipulate data stored in Firebase with
    * @param authenticator: used for Firebase accesses
     * Also, this class init will cache user info and statistics from Firebase for future use.
     */
    public OnlineDatabaseAccessor(Authenticator authenticator) {
        if (authenticator.getSignInState() == Authenticator.SignInState.SIGNED_IN) {
            mFirebaseUser = authenticator.getCurrentUser();
            if (mFirebaseUser != null) {
                mUserID = mFirebaseUser.getUid();
                String fullName = mFirebaseUser.getDisplayName();
                Uri profileImageUri = mFirebaseUser.getPhotoUrl();
                Date userJoinDate = new Date(Objects.requireNonNull(mFirebaseUser.getMetadata()).getCreationTimestamp());
                mCachedUserProfile = new UserProfile(mUserID, fullName, profileImageUri, userJoinDate);
            }
            mFirebaseFirestore = FirebaseFirestore.getInstance();
        }
    }

    public String getOnlineUserID() {
        return mUserID;
    }

    public String getUsername() {
        return mCachedUserProfile.getFullName();
    }

    public String getUserEmail() {
        return mFirebaseUser.getEmail();
    }

    public UserProfile getUserProfile() {
        return mCachedUserProfile;
    }

    public  Statistics getUserStatistics() {
        getOnlineStatistics();
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
                            Log.d(onlineDBTAG, "modifyUserProfile:success");
                            Toast.makeText(MainActivity.getInstance(), "User profile modified", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d(onlineDBTAG, "modifyUserProfile:failure");
                            Toast.makeText(MainActivity.getInstance(), "Failed to modify user profile", Toast.LENGTH_SHORT).show();
                        }
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

    private void getOnlineStatistics() {
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
                    .document(mUserID).update(deleteFields);
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
