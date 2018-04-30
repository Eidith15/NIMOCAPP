package com.example.depran.nimoc.function;

public class Divisi {
    String idDivisi, idBuku,namaDivisi;

    public Divisi(String idDivisi, String idBuku, String namaDivisi) {
        this.idDivisi = idDivisi;
        this.idBuku = idBuku;
        this.namaDivisi = namaDivisi;
    }

    public String getIdDivisi() {
        return idDivisi;
    }

    public String getIdBuku() {
        return idBuku;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }
    @Override
    public String toString() {
        return namaDivisi;
    }
}
