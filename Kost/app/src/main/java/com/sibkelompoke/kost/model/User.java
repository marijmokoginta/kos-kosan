package com.sibkelompoke.kost.model;

import static com.sibkelompoke.kost.constant.KostKonstan.GUEST;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private String noTelepon;

    public User () {
        this.role = GUEST;
    }

    public User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
