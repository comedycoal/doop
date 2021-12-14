package com.tranhulovu.doop.onlinedatabase;

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
