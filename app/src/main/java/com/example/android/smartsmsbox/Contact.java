package com.example.android.smartsmsbox;

import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private String phoneNo;

    public Contact(String name, String telNo) {
        this.name = name;
        this.phoneNo = telNo;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Number: " + phoneNo;
    }
}
