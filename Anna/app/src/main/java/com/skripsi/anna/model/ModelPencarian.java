package com.skripsi.anna.model;

/**
 * Created by gandhi on 6/11/17.
 */

public class ModelPencarian {

    private String NamaKuliner,FotoKuliner,AlamatKuliner,KategoriKuliner,IdKuliner,LatitudeKuliner,LongitudeKuliner;

    public ModelPencarian(String NamaKuliner, String FotoKuliner, String AlamatKuliner, String KategoriKuliner, String IdKuliner, String LatitudeKuliner, String LongitudeKuliner){
        this.NamaKuliner = NamaKuliner;
        this.FotoKuliner = FotoKuliner;
        this.AlamatKuliner = AlamatKuliner;
        this.KategoriKuliner =KategoriKuliner;
        this.IdKuliner =IdKuliner;
        this.LatitudeKuliner = LatitudeKuliner;
        this.LongitudeKuliner = LongitudeKuliner;
    }

    public ModelPencarian() {

    }

    public String getNamaKuliner() {
        return NamaKuliner;
    }

    public void setNamaKuliner(String namaKuliner) {
        NamaKuliner = namaKuliner;
    }

    public String getFotoKuliner() {
        return FotoKuliner;
    }

    public void setFotoKuliner(String fotoKuliner) {
        FotoKuliner = fotoKuliner;
    }

    public String getAlamatKuliner() {
        return AlamatKuliner;
    }

    public void setAlamatKuliner(String alamatKuliner) {
        AlamatKuliner = alamatKuliner;
    }

    public String getKategoriKuliner() {
        return KategoriKuliner;
    }

    public void setKategoriKuliner(String kategoriKuliner) {
        KategoriKuliner = kategoriKuliner;
    }

    public String getIdKuliner() {
        return IdKuliner;
    }

    public void setIdKuliner(String idKuliner) {
        IdKuliner = idKuliner;
    }

    public String getLatitudeKuliner() {
        return LatitudeKuliner;
    }

    public void setLatitudeKuliner(String latitudeKuliner) {
        LatitudeKuliner = latitudeKuliner;
    }

    public String getLongitudeKuliner() {
        return LongitudeKuliner;
    }

    public void setLongitudeKuliner(String longitudeKuliner) {
        LongitudeKuliner = longitudeKuliner;
    }


}
