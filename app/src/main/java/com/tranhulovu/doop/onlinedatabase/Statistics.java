package com.tranhulovu.doop.onlinedatabase;

import java.util.Map;
import java.util.Objects;

public class Statistics {
    private final String mUserID;
    private final long mDoneCardCount;
    private final long mMissedCardCount;
    private final long mArchivedCardCount;
    private final long mTotalCardCount;

    public Statistics(String userID, long doneCardCount, long missedCardCount, long archivedCardCount, long totalCardCount) {
        mUserID = userID;
        mDoneCardCount = doneCardCount;
        mMissedCardCount = missedCardCount;
        mArchivedCardCount = archivedCardCount;
        mTotalCardCount = totalCardCount;
    }

    public  Statistics(String userID, Map<String, Object> map) {
        mUserID = userID;
        mDoneCardCount = (long) Objects.requireNonNull(map.get("doneCardCount"));
        mMissedCardCount = (long) Objects.requireNonNull(map.get("missedCardCount"));
        mArchivedCardCount = (long) Objects.requireNonNull(map.get("archivedCardCount"));
        mTotalCardCount = (long) Objects.requireNonNull(map.get("totalCardCount"));
    }

    public String getUserID() {
        return mUserID;
    }

    public long getDoneCardCount() {
        return mDoneCardCount;
    }

    public long getMissedCardCount() {
        return mMissedCardCount;
    }

    public long getArchivedCardCount() {
        return mArchivedCardCount;
    }

    public long getTotalCardCount() {
        return mTotalCardCount;
    }
}
