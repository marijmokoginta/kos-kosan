package com.sibkelompoke.kost.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Alamat implements Parcelable {
    private String alamatId;
    private String jalan;
    private String no;
    private String desa;
    private String kelurahan;
    private String kecamatan;
    private String kabupaten;
    private String informasiTambahan;

    public Alamat(){}

    protected Alamat(Parcel in) {
        alamatId = in.readString();
        jalan = in.readString();
        no = in.readString();
        desa = in.readString();
        kelurahan = in.readString();
        kecamatan = in.readString();
        kabupaten = in.readString();
        informasiTambahan = in.readString();
    }

    public static final Creator<Alamat> CREATOR = new Creator<Alamat>() {
        @Override
        public Alamat createFromParcel(Parcel in) {
            return new Alamat(in);
        }

        @Override
        public Alamat[] newArray(int size) {
            return new Alamat[size];
        }
    };

    public String getJalan() {
        return jalan;
    }

    public String getAlamatId() {
        return alamatId;
    }

    public void setAlamatId(String alamatId) {
        this.alamatId = alamatId;
    }

    public void setJalan(String jalan) {
        this.jalan = jalan;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getInformasiTambahan() {
        return informasiTambahan;
    }

    public void setInformasiTambahan(String informasiTambahan) {
        this.informasiTambahan = informasiTambahan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(alamatId);
        parcel.writeString(jalan);
        parcel.writeString(no);
        parcel.writeString(desa);
        parcel.writeString(kelurahan);
        parcel.writeString(kecamatan);
        parcel.writeString(kabupaten);
        parcel.writeString(informasiTambahan);
    }
}
