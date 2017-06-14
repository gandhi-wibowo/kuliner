package com.skripsi.anna.model;

/**
 * Created by gandhi on 6/11/17.
 */

public class ModelMenu {
    private String IdMenu,NamaMenu,HargaMenu;

    public ModelMenu(){}
    public ModelMenu(String IdMenu,String NamaMenu,String HargaMenu){
        this.IdMenu = IdMenu;
        this.NamaMenu = NamaMenu;
        this.HargaMenu = HargaMenu;
    }
    public String getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(String idMenu) {
        IdMenu = idMenu;
    }

    public String getNamaMenu() {
        return NamaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        NamaMenu = namaMenu;
    }

    public String getHargaMenu() {
        return HargaMenu;
    }

    public void setHargaMenu(String hargaMenu) {
        HargaMenu = hargaMenu;
    }


}
