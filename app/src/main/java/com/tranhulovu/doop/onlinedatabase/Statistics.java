package com.tranhulovu.doop.onlinedatabase;

import java.util.Map;
import java.util.Objects;

public class Statistics {
    private final String mUserID;
    private final Map<String, Object> mLastWeekCard;
    private final Map<String, Object> mLastMonthCard;
    private final Map<String, Object> mTotalCard;


    public Statistics(String userID, Map<String, Object> lastWeekCard,
                      Map<String, Object> lastMonthCard,
                      Map<String, Object> totalCard) {
        mUserID = userID;
        mLastWeekCard = lastWeekCard;
        mLastMonthCard = lastMonthCard;
        mTotalCard = totalCard;

    }

    public String getUserID() {
        return mUserID;
    }

    public long getDoneCardCount(String time) {
        switch (time) {
            case "lastWeek":
                return (long) Objects.requireNonNull(mLastWeekCard.get("doneCardCount"));
            case "lastMonth":
                return (long) Objects.requireNonNull(mLastMonthCard.get("doneCardCount"));
            default:
                return (long) Objects.requireNonNull(mTotalCard.get("doneCardCount"));
        }
    }

    public long getMissedCardCount(String time) {
        switch (time) {
            case "lastWeek":
                return (long) Objects.requireNonNull(mLastWeekCard.get("missedCardCount"));
            case "lastMonth":
                return (long) Objects.requireNonNull(mLastMonthCard.get("missedCardCount"));
            case "total":
                return (long) Objects.requireNonNull(mTotalCard.get("missedCardCount"));
            default:
                return -1;
        }
    }

    public long getArchivedCardCount(String time) {
        switch (time) {
            case "lastWeek":
                return (long) Objects.requireNonNull(mLastWeekCard.get("archivedCardCount"));
            case "lastMonth":
                return (long) Objects.requireNonNull(mLastMonthCard.get("archivedCardCount"));
            case "total":
                return (long) Objects.requireNonNull(mTotalCard.get("archivedCardCount"));
            default:
                return -1;
        }
    }

    public long getTotalCardCount(String time) {
        switch (time) {
            case "lastWeek":
                return (long) Objects.requireNonNull(mLastWeekCard.get("totalCardCount"));
            case "lastMonth":
                return (long) Objects.requireNonNull(mLastMonthCard.get("totalCardCount"));
            case "total":
                return (long) Objects.requireNonNull(mTotalCard.get("totalCardCount"));
            default:
                return -1;
        }
    }
}
