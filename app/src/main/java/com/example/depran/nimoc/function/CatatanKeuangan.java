package com.example.depran.nimoc.function;

public class CatatanKeuangan {
    String mIdBuku, //idbuku
            mFIdR, //id_user
            mNamaBuku, //nama buku
            nKeterangan, //keterangan
            mStatus_simpan; //status simpan;

    public CatatanKeuangan(String[] arr) {
        this.mIdBuku = arr[0];
        this.mFIdR = arr[1];
        this.mNamaBuku = arr[2];
        this.nKeterangan = arr[3];
        this.mStatus_simpan = arr[4];
    }

    public String getmIdBuku() {
        return mIdBuku;
    }

    public String getmFIdR() {
        return mFIdR;
    }

    public String getmNamaBuku() {
        return mNamaBuku;
    }

    public String getmKeterangan() {
        return nKeterangan;
    }

    public String getmStatus_simpan() {
        return mStatus_simpan;
    }
}
