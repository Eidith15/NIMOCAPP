package com.example.depran.nimoc.etc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.depran.nimoc.R;

public class BantuanActivity extends AppCompatActivity {
    ListView listView;
    String arrMenu[] = {
            "Arsip",
            "Pemasukkan",
            "Pengeluaran",
            "Divisi",
            "Riwayat",
            "Kontak Kami"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrMenu));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent i;
                switch (position) {
                    case 0:
                        i= new Intent(BantuanActivity.this, BantuanDetailActivity.class);
                        i.putExtra("label","Arsip");
                        i.putExtra("deskripsi","List bantuan arsip  merupakan media penyimpanan untuk catatn atau laporan yang sudah digunakan dan sudah tidak terpakai.");
                        i.putExtra("fungsi","Arsip berfungsi untuk menyimpan semua laporan dari setiap divi yang sudah digunakan dan laporan yang sudah lama tidak terpakai dapat disimpan pada arsip agar data pada beranda tidak terlalu penuh");
                        i.putExtra("cara_pakai","Cara menggunakan list bantuan arsip ialah ketika beranda catatan atau laporan keuangan pada setiap divisi  telah penuh maka catatan atau laporan tersebut dapat disimpan dengan cara mengklik icon arsip yang ada disamping list divisi pada beranda ");
                        startActivity(i);
                        break;
                    case 1:
                        i= new Intent(BantuanActivity.this, BantuanDetailActivity.class);
                        i.putExtra("label","Pemasukkan");
                        i.putExtra("deskripsi","Pemasukkan merupakan deskripsi dari laporan apasaja yang dilakukan oleh setiap divisi dan terdapat nonimal uang yang digunakan, divisi apa yang menggunakan, dan keterangan dari kegiatan yang dilakukan oleh setiap divisi");
                        i.putExtra("fungsi","List pemasukkan ini berfungsi untuk mendaftarkan kegiatan dari setiap divisi dengan mencantumkan nominal uang, divisi apa yang menggunakan sampai keteragan laporan yang digunakan.");
                        i.putExtra("cara_pakai","Cara pakai list bantuan pemasukkan ini ialah pada menu dashboard terdapat icon plus berwarna hijau, dengan klik icon tersebut maka akan masuk pada menu pemasukkan yang didalamnya terdapat form untuk mengisi nominal, memilih nama divisi, dan keterangan catatan atau laporannya. Lalu klik button tambah maka catatan pemasukkan akan terdaftar.");
                        startActivity(i);
                        break;
                    case 2:
                        i= new Intent(BantuanActivity.this, BantuanDetailActivity.class);
                        i.putExtra("label","Pengeluaran");
                        i.putExtra("deskripsi","Pengeluaran merupakan deskripsi dari laporan apasaja yang dilakukan oleh setiap divisi dan terdapat nonimal uang yang digunakan, ke divisi yang menggunakan , dan keterangan dari kegiatan yang dilakukan oleh setiap divisi");
                        i.putExtra("fungsi","List pengeluaran ini berfungsi untuk mendaftarkan kegiatan dari setiap divisi dengan mencantumkan nominal uang, ke divisi apa yang menggunakan sampai keteragan laporan yang digunakan.");
                        i.putExtra("cara_pakai","Cara pakai list bantuan pengeluaran ini ialah pada menu dashboard terdapat icon minus berwarna merah, dengan klik icon tersebut maka akan masuk pada menu pengeluaran yang didalamnya terdapat form untuk mengisi nominal, memilih nama divisi, dan keterangan catatan atau laporannya. Lalu klik button tambah maka catatan pengeluaran akan terdaftar.");
                        startActivity(i);
                        break;
                    case 3:
                        i= new Intent(BantuanActivity.this, BantuanDetailActivity.class);
                        i.putExtra("label","Divisi");
                        i.putExtra("deskripsi","Divisi merupakan media untuk mendaftarkan setiap kelompok pada organisasi yaitu divisi");
                        i.putExtra("fungsi","Divisi berfungsi untuk membedakan setiap laporan atau catatan dari setiap divisi yang ada");
                        i.putExtra("cara_pakai","Pada menu dashboard terdapat list menu divisi, dan ada beberapa list divisi untuk menambahkan divisi yang akan didaftarkan dengan cara klik icon plus berwana hijau maka akan masuk pada menu  edit divisi yang didalamnya terdapat form isi nama divisi");
                        startActivity(i);
                        break;
                    case 4:
                        i= new Intent(BantuanActivity.this, BantuanDetailActivity.class);
                        i.putExtra("label","Riwayat");
                        i.putExtra("deskripsi","Riwayat merupakan laporan dari pemasukkan dan pengeluaran yang sudah dilakukan oleh setiap divisi. List laporan yang berwarna merah merupakan pengeluaran dan list laporan berwarna hijau merupakan pemasukkan");
                        i.putExtra("fungsi","Riwayat merupakan laporan dari pemasukkan dan pengeluaran yang sudah dilakukan oleh setiap divisi. List laporan yang berwarna merah merupakan pengeluaran dan list laporan berwarna hijau merupakan pemasukkan");
                        i.putExtra("cara_pakai","Ketika mengisi pada menu pemasukkan dan pengeluaran maka laporan tersebut akan masuk listnya pada riwayat");
                        startActivity(i);
                        break;
                    case 5:
                        String mailto = "mailto:ade.pranaya@mail.unpas.ac.id" +
                                "?cc=" + "" +
                                "&subject=" + Uri.encode("BANTUAN_NIMOC_FINANCE") +
                                "&body=" + Uri.encode("");

                        i = new Intent(Intent.ACTION_SENDTO);
                        i.setData(Uri.parse(mailto));

                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(BantuanActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
    }
}
