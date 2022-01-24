package com.tranhulovu.doop.applicationui.fragment;

public class dataCardView {
    private String name;
    private String description;
    private String start;
    private String end;
    private String status;
    private String notification;

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
