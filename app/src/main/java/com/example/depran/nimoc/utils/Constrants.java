package com.example.depran.nimoc.utils;

/**
 * Created by user on 26/03/2017.
 */

public class Constrants {
    //hosting public
//    public static String URL = "https://adepranaya.000webhostapp.com/";

    //hosting local
    //hotspot unpas
//    public static String URL = "http://192.168.10.30/api_nimoc/";

    //dkm uab
    public static String URL = "http://10.11.12.180/api_nimoc/";

    //register / sign up / daftar
    public static final String URL_ADD_USER = URL + "user_apps";

    //login / sign in / masuk
    public static final String URL_LOGIN = URL + "user_apps";


    public static final String URL_GET_BUKU= URL + "buku_ctt_keuangan?status_simpan=0"; //read
    public static final String URL_BUKU= URL + "buku_ctt_keuangan"; //add, update, delete,

    public static final String URL_GET_DIVISI= URL + "divisi";
    public static final String URL_GET_ARSIP= URL + "buku_ctt_keuangan?status_simpan=1";

//    public static final String URL_ADD_POSTING = URL + "posting.php?action=add_posting";
//    public static final String URL_GET_POSTING = URL + "posting.php?action=get_posting";

}
