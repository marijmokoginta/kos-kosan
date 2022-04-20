package com.sibkelompoke.kost.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Kost implements Parcelable {
    private ArrayList<String> imageUrl;
    private String namaKost;
    private String waktuBukaKost;
    private String tipeKost;
    private Alamat alamat;
    private String harga;
    private String jumlahKamar;
    private String peraturanKost;
    private String catatanKost;
    private String kostId;
    private String userId;

    public Kost (){}

    public Kost (String kostId) {
        this.kostId = kostId;
        imageUrl = new ArrayList<>();
    }

    protected Kost(Parcel in) {
        imageUrl = in.createStringArrayList();
        namaKost = in.readString();
        waktuBukaKost = in.readString();
        tipeKost = in.readString();
        harga = in.readString();
        jumlahKamar = in.readString();
        peraturanKost = in.readString();
        catatanKost = in.readString();
        kostId = in.readString();
        userId = in.readString();
    }

    public static final Creator<Kost> CREATOR = new Creator<Kost>() {
        @Override
        public Kost createFromParcel(Parcel in) {
            return new Kost(in);
        }

        @Override
        public Kost[] newArray(int size) {
            return new Kost[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKostId() {
        return kostId;
    }

    public Alamat getAlamat() {
        return alamat;
    }

    public void setAlamat(Alamat alamat) {
        this.alamat = alamat;
    }

    public void setKostId(String kostId) {
        this.kostId = kostId;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNamaKost() {
        return namaKost;
    }

    public void setNamaKost(String namaKost) {
        this.namaKost = namaKost;
    }

    public String getWaktuBukaKost() {
        return waktuBukaKost;
    }

    public void setWaktuBukaKost(String waktuBukaKost) {
        this.waktuBukaKost = waktuBukaKost;
    }

    public String getTipeKost() {
        return tipeKost;
    }

    public void setTipeKost(String tipeKost) {
        this.tipeKost = tipeKost;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlahKamar() {
        return jumlahKamar;
    }

    public void setJumlahKamar(String jumlahKamar) {
        this.jumlahKamar = jumlahKamar;
    }

    public String getPeraturanKost() {
        return peraturanKost;
    }

    public void setPeraturanKost(String peraturanKost) {
        this.peraturanKost = peraturanKost;
    }

    public String getCatatanKost() {
        return catatanKost;
    }

    public void setCatatanKost(String catatanKost) {
        this.catatanKost = catatanKost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(imageUrl);
        parcel.writeString(namaKost);
        parcel.writeString(waktuBukaKost);
        parcel.writeString(tipeKost);
        parcel.writeString(harga);
        parcel.writeString(jumlahKamar);
        parcel.writeString(peraturanKost);
        parcel.writeString(catatanKost);
        parcel.writeString(kostId);
        parcel.writeString(userId);
    }
}
