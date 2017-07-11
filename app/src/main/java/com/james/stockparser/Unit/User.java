package com.james.stockparser.Unit;

import java.util.ArrayList;

/**
 * Created by 101716 on 2017/7/11.
 */

public class User {

    private String name;
    private String email;
    private String UID;
    private ArrayList <String>favorite = new ArrayList(){};

    public User(String name, String email, ArrayList favorite) {
        this.name = name;
        this.email = email;
        this.favorite = favorite;
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
