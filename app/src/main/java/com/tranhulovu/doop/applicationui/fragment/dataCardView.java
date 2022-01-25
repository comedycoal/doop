package com.tranhulovu.doop.applicationui.fragment;

public class dataCardView
{
    public String id;
    public String name;
    public String description;
    public String start;
    public String end;
    public String status;
    public String notification;
    public String notificationType;

    public dataCardView(
            String id,
            String name,
            String description,
            String start,
            String end,
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
}
