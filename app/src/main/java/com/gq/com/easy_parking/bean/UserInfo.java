package com.gq.com.easy_parking.bean;

/**
 * Created by hasee on 2017/4/18.
 */
public class UserInfo {
    private String name;
    private String imageurl;
    private boolean iscredit;
    private String phone;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean iscredit() {
        return iscredit;
    }

    public void setIscredit(boolean iscredit) {
        this.iscredit = iscredit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserInfo(String name, String imageurl, boolean iscredit, String phone) {
        this.name = name;
        this.imageurl = imageurl;
        this.iscredit = iscredit;
        this.phone = phone;
    }

    public UserInfo() {
        super();
    }
}
