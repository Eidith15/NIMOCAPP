package com.example.depran.nimoc.function;

public class Riwayat {
    private String mDivisi;
    private String mNominal;
    private int kodeP;
    private String mTanggal;

    public Riwayat(String mDivisi, String mNominal, String mTanggal, int kodeP) {
        this.mDivisi = mDivisi;
        this.mNominal = mNominal;
        this.mTanggal = mTanggal;
        this.kodeP = kodeP;
    }

    public String getmDivisi() {
        return mDivisi;
    }

    public String getmNominal() {
        return mNominal;
    }

    public String getmTanggal() {
        return mTanggal;
    }

    public int getKodeP() {
        return kodeP;
    }
}
