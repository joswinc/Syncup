package com.example.practice;

public class postdisplay {
    String Desc;
    String Title;
    public postdisplay()
    {

    }
    public postdisplay(String notice, String title) {
        this.Desc = notice;
        this.Title = title;
    }

    public String getNotice() {
        return Desc;
    }

    public void setNotice(String notice) {
        this.Desc = notice;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }


}
