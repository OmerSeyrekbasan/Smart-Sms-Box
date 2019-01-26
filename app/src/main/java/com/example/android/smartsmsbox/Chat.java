package com.example.android.smartsmsbox;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {
    private String address;
    private String name;
    private ArrayList<SMS> smsList;
    private int category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chat (String address) {
        smsList = new ArrayList<SMS>();
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    public ArrayList<SMS> getSmsList() {
        return smsList;
    }

    public void addSmsToList(SMS s) {
        smsList.add(s);
    }
    public void addSmsToFrontofList(SMS s) {
        smsList.add(0, s);
    }

    public int getSize() {
        return smsList.size();
    }

    public void setSmsList(ArrayList<SMS> smsList) {
        this.smsList = smsList;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        String str = " ";
        for (SMS s : smsList) {
            str += " " + s.toString() + " ";
        }
        return "Chat with: " + getAddress() + " " + str ;
    }
}
