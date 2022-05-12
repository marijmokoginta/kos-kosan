package com.sibkelompoke.kost.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class OrderKost implements Parcelable {
    private String orderId;
    private String userId;
    private String kostId;
    private String ownerKostId;
    private User user;
    private String catatan;
    private boolean isContract;
    private boolean isConfirm;
    private Date onOrder;
    private Date onContract;
    private String noKamar;

    public OrderKost () {}

    public OrderKost(String userId, String kostId, String ownerKostId) {
        this.userId = userId;
        this.kostId = kostId;
        this.ownerKostId = ownerKostId;
    }

    protected OrderKost(Parcel in) {
        orderId = in.readString();
        userId = in.readString();
        kostId = in.readString();
        ownerKostId = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        catatan = in.readString();
        isContract = in.readByte() != 0;
        isConfirm = in.readByte() != 0;
        noKamar = in.readString();
    }

    public static final Creator<OrderKost> CREATOR = new Creator<OrderKost>() {
        @Override
        public OrderKost createFromParcel(Parcel in) {
            return new OrderKost(in);
        }

        @Override
        public OrderKost[] newArray(int size) {
            return new OrderKost[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOwnerKostId() {
        return ownerKostId;
    }

    public void setOwnerKostId(String ownerKostId) {
        this.ownerKostId = ownerKostId;
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

    public boolean isContract() {
        return isContract;
    }

    public void setContract(boolean contract) {
        isContract = contract;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public Date getOnOrder() {
        return onOrder;
    }

    public void setOnOrder(Date onOrder) {
        this.onOrder = onOrder;
    }

    public Date getOnContract() {
        return onContract;
    }

    public void setOnContract(Date onContract) {
        this.onContract = onContract;
    }

    public String getNoKamar() {
        return noKamar;
    }

    public void setNoKamar(String noKamar) {
        this.noKamar = noKamar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(userId);
        parcel.writeString(kostId);
        parcel.writeString(ownerKostId);
        parcel.writeParcelable(user, i);
        parcel.writeString(catatan);
        parcel.writeByte((byte) (isContract ? 1 : 0));
        parcel.writeByte((byte) (isConfirm ? 1 : 0));
        parcel.writeString(noKamar);
    }
}
