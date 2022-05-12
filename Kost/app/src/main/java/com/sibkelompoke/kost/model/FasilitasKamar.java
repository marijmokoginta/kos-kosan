package com.sibkelompoke.kost.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FasilitasKamar implements Parcelable {
    private int fasilitasImg;
    private String fasilitasName;

    public FasilitasKamar(int fasilitasImg, String fasilitasName) {
        this.fasilitasImg = fasilitasImg;
        this.fasilitasName = fasilitasName;
    }

    protected FasilitasKamar(Parcel in) {
        fasilitasImg = in.readInt();
        fasilitasName = in.readString();
    }

    public static final Creator<FasilitasKamar> CREATOR = new Creator<FasilitasKamar>() {
        @Override
        public FasilitasKamar createFromParcel(Parcel in) {
            return new FasilitasKamar(in);
        }

        @Override
        public FasilitasKamar[] newArray(int size) {
            return new FasilitasKamar[size];
        }
    };

    public int getFasilitasImg() {
        return fasilitasImg;
    }

    public void setFasilitasImg(int fasilitasImg) {
        this.fasilitasImg = fasilitasImg;
    }

    public String getFasilitasName() {
        return fasilitasName;
    }

    public void setFasilitasName(String fasilitasName) {
        this.fasilitasName = fasilitasName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(fasilitasImg);
        parcel.writeString(fasilitasName);
    }
}
