package com.tranhulovu.doop.applicationui.fragment;

public class dataCardView {
    public String name;
    public String description;
    public String start;
    public String end;
    public String status;
    public String notification;

    public dataCardView(    String name,
            String description,
            String start,
            String end,
            String status,
            String notification){
        this.name=name;
        this.description=description;
        this.start=start;
        this.end=end;
        this.notification=notification;
    }
}
