package com.tranhulovu.doop.applicationui.fragment;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class dataCardView
{
    public String id;
    public String name;
    public String description;
    public ZonedDateTime start;
    public ZonedDateTime end;
    public String status;
    public String notification;
    public String notificationType;

    public dataCardView(
            String id,
            String name,
            String description,
            ZonedDateTime start,
            ZonedDateTime end,
            String status,
            String notification,
            String notificationType)
    {
        this.id = id;
        this.name=name;
        this.description=description;
        this.start=start;
        this.end=end;
        this.status = status;
        this.notification=notification;
        this.notificationType = notificationType;
    }

    public String getStartDate()
    {
        return start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getStartTime()
    {
        return start.format(DateTimeFormatter.ofPattern("hh:mm"));
    }

    public String getEndDate()
    {
        return end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getEndTime()
    {
        return end.format(DateTimeFormatter.ofPattern("hh:mm"));
    }
}
