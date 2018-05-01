package com.example.depran.nimoc;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.function.Riwayat;
import com.example.depran.nimoc.function.RiwayatAdapter;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class RiwayatFragment extends Fragment {
    public static DashboardBukuActivity dashboardBukuActivity;
    ListView listView;
    ArrayList<Riwayat> riwayats;
    RiwayatAdapter adapter;

    public static RiwayatFragment newInstance(DashboardBukuActivity activity) {
        dashboardBukuActivity = activity;
        return new RiwayatFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(dashboardBukuActivity).inflate(R.layout.fragment_riwayat, container, false);
        riwayats = new ArrayList<Riwayat>();

        adapter = new RiwayatAdapter(getActivity(), riwayats);
        listView = (ListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);
        new RiwayatAsyncTask().execute();
        return view;
    }

    public String ubahKeRupiah(int nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format((double) nominal);
    }

    //berfungsi untuk mengambil data buku ke database
    private class RiwayatAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String idU = preferences.getString("id_u", null);
                String idBuku = preferences.getString("id_buku", null);
                String url = Constrants.URL_GET_RIWAYAT + "?id_buku=" + idBuku + "&owner=" + idU;
                respon = CustomHttpClient.executeHttpGet(url);

            } catch (Exception e) {
                respon = e.toString();
            }
            return respon;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.e("Respon", " -> " + result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String divisi = object.getString("nama_divisi");
                        int pemasukanDrDvs = object.getInt("pemasukan_dr_dvs");
                        int pengeluaranKeDvs = object.getInt("pengeluaran_ke_dvs");
                        String createAt = object.getString("create_at");

                        //get current date
                        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date dateC = new Date();
                        String tanggalSekarang = dateFormat.format(dateC); //2016-11-16

                        //convert string to date
                        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = inputFormatter.parse(createAt);

                        DateFormat outputFormatter = new SimpleDateFormat("MM-dd-yyyy");
                        String tanggalBuat = outputFormatter.format(date); // Output : 01-20-2012

//                        String tanggal = dateFormat.format(dateC);
                        Log.e("tanggal riwayat", tanggalBuat);
                        Log.e("tanggal sekarang", tanggalSekarang);
                        if (pemasukanDrDvs != 0 || pengeluaranKeDvs != 0) {
                            if (pemasukanDrDvs > 0) {
                                riwayats.add(new Riwayat(divisi, ubahKeRupiah(pemasukanDrDvs), (tanggalBuat.trim().equalsIgnoreCase(tanggalSekarang.trim()) ? "Hari ini" : tanggalBuat), 1));
                            } else {
                                riwayats.add(new Riwayat(divisi, ubahKeRupiah(pengeluaranKeDvs), (tanggalBuat.trim().equalsIgnoreCase(tanggalSekarang.trim()) ? "Hari ini" : tanggalBuat), 2));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
