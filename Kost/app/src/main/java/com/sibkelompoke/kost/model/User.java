package com.sibkelompoke.kost.model;

import static com.sibkelompoke.kost.constant.KostKonstan.GUEST;
import static com.sibkelompoke.kost.constant.KostKonstan.MISSING;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String namaLengkap;
    private String username;
    private String password;
    private String role;
    private String noTelepon;
    private String imageUrl;
    private String pekerjaan;

    public User () {
        this.id = MISSING;
        this.username = GUEST;
        this.role = GUEST;
    }

    public User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected User(Parcel in) {
        id = in.readString();
        namaLengkap = in.readString();
        username = in.readString();
        password = in.readString();
        role = in.readString();
        noTelepon = in.readString();
        imageUrl = in.readString();
        pekerjaan = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(namaLengkap);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(role);
        parcel.writeString(noTelepon);
        parcel.writeString(imageUrl);
        parcel.writeString(pekerjaan);
    }
}
