package com.sibkelompoke.kost.model;

import java.util.Date;

public class Tagihan{
    private String tagihanId;
    private String orderId;
    private Date tanggalTagihan;
    private int jumlahTagihan;
    private int waktuPenundaan;
    private String role;
    private boolean lunas;
    private boolean past2Bulan;

    public boolean isPast2Bulan() {
        return past2Bulan;
    }

    public void setPast2Bulan(boolean past2Bulan) {
        this.past2Bulan = past2Bulan;
    }

    public boolean isLunas() {
        return lunas;
    }

    public void setLunas(boolean lunas) {
        this.lunas = lunas;
    }

    public String getTagihanId() {
        return tagihanId;
    }

    public void setTagihanId(String tagihanId) {
        this.tagihanId = tagihanId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getTanggalTagihan() {
        return tanggalTagihan;
    }

    public void setTanggalTagihan(Date tanggalTagihan) {
        this.tanggalTagihan = tanggalTagihan;
    }

    public int getJumlahTagihan() {
        return jumlahTagihan;
    }

    public void setJumlahTagihan(int jumlahTagihan) {
        this.jumlahTagihan = jumlahTagihan;
    }

    public int getWaktuPenundaan() {
        return waktuPenundaan;
    }

    public void setWaktuPenundaan(int waktuPenundaan) {
        this.waktuPenundaan = waktuPenundaan;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
