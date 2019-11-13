package com.example.practice;

import androidx.annotation.NonNull;

public class Notify {
    String Title,Desc;

    public Notify() {
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    @NonNull
    @Override
    public String toString() {
        return Title+":"+ Desc;
        //return super.toString();
    }
}
