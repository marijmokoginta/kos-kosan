package com.sibkelompoke.kost.model;

public class OrderedKost {
    private String id;
    private String userId;
    private String kostId;
    private User user;
    private String catatan;

    public OrderedKost(String userId, String kostId) {
        this.userId = userId;
        this.kostId = kostId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKostId() {
        return kostId;
    }

    public void setKostId(String kostId) {
        this.kostId = kostId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
