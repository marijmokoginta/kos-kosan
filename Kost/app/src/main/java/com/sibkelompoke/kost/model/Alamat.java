package com.sibkelompoke.kost.model;

public class Alamat {
    private String alamatId;
    private String jalan;
    private String no;
    private String desa;
    private String kelurahan;
    private String kecamatan;
    private String kabupaten;
    private String informasiTambahan;

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
}
