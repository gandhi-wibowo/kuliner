package com.skripsi.anna;

/**
 * Created by gandhi on 5/16/17.
 */

public class Constant {
    public static final String BASE = "http://192.168.43.50/kuliner/index.php/";
    public static final String Login = BASE + "Login?login=y&"; // kirim email dan password untuk login : method get
    public static final String CekLogin = BASE + "Login?cek_login=y&"; // cek login berdasarkan data pada shared pref
    public static final String Ping = BASE + "Login?ping=y"; // cek jaringan
    public static final String Daftar = BASE + "Login/"; // gunakan method post untuk mendaftar, nama_user,email_user,hp_user,password_user
    public static final String Update = BASE + "Login/"; // gunakan method put, untuk merubah password ataupun data pribadi
    public static final String GetUser = BASE + "Login?id_user="; // gunakan method get untuk mendapatkan data user berdasarkan id user

    public static final String Kuliner = BASE + "Kuliner/"; // gunakan method post untuk menambah menu / Kuliner
    public static final String CariKuliner = BASE + "Kuliner?keyword="; // cari berdasarkan keyword
    public static final String GetById = BASE + "Kuliner?id_kuliner="; // dapatkan kuliner berdasarkan id
    public static final String GetByIdUser = BASE + "Kuliner?id_user="; // dapatkan kuliner berdasarkan id user / pemilik
    public static final String GetMenu = BASE + "Kuliner?Mid_kuliner="; // dapatkan menu berdasarkan id kuliner
    public static final String GetMenuById = BASE + "Kuliner?id_menu="; // dapatkan satu menu berdasarkan id nya

    public static final String Image = BASE +"../img/";
}
