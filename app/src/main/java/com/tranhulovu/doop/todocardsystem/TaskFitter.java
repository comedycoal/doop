package com.tranhulovu.doop.todocardsystem;

import java.util.List;

public class TaskFitter
{
    public static class Task
    {
        public Task priorTask;
        public String name;
        public long start = -1;
        public long end = -1;
        public long length = -1;
    }

    private long mStart;
    private long mEnd;
    private long mMinLength;

    private long mLastEnd;

    public TaskFitter(long start, long end)
    {
        this(start, end, 60 * 15);
    }

    public TaskFitter(long start, long end, long minLength)
    {
        this.mStart = start;
        this.mEnd = end;
        this.mMinLength = minLength;
        mLastEnd = mStart;
    }

    public boolean fit(Task t)
    {
        Task prior = t.priorTask;
        try
        {
            long suitStart = mLastEnd;
            if (prior != null)
            {
                suitStart = prior.end;
            }

            if (t.start >= 0 && t.end >= 0)
            {
                mLastEnd = t.end;
            }
            else if (t.start >= 0)
            {
                t.end = t.start + getMinLength();
                assert (t.end <= mEnd);
                mLastEnd = t.end;
            }
            else if (t.length > 0)
            {
                t.start = suitStart;
                t.end = t.start + t.length;
                assert (t.end <= mEnd);
                mLastEnd = t.end;
            }
            return true;
        }
        catch (AssertionError e)
        {
            if (prior != null)
                return false;
            else
            {
                t.end = mEnd;
                t.start = -1;
                return true;
            }
        }
    }

    public long getStart()
    {
        return mStart;
    }

    public long getEnd()
    {
        return mEnd;
    }

    public long getMinLength()
    {
        return mMinLength;
    }
}
