package com.example.depran.nimoc;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.buku.TambahPemasukanActivity;
import com.example.depran.nimoc.buku.TambahPengeluaranActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardBukuFragment extends Fragment {
    public static DashboardBukuActivity dashboardBukuActivity;
    PieChart pieChart;

    ImageButton pemasukkanBtn, pengeluaranBtn;
    TextView totalPemasukkanView, totalPengeluaranView, sisaUang;


    public static DashboardBukuFragment newInstance(DashboardBukuActivity activity) {
        dashboardBukuActivity = activity;
        return new DashboardBukuFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(dashboardBukuActivity).inflate(R.layout.fragment_dashboard_buku, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);


        pengeluaranBtn = (ImageButton) view.findViewById(R.id.pengeluaran_btn);
        pemasukkanBtn = (ImageButton) view.findViewById(R.id.pemasukkan_btn);
        totalPemasukkanView = (TextView) view.findViewById(R.id.total_pemasukkan);
        totalPengeluaranView = (TextView) view.findViewById(R.id.total_pengeluaran);
        sisaUang = (TextView) view.findViewById(R.id.sisa_uang);

        pengeluaranBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahPengeluaranActivity.class);
                getActivity().startActivity(intent);
            }
        });
        pemasukkanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahPemasukanActivity.class);
                getActivity().startActivity(intent);
            }
        });
        new GraphPPAsyncTask().execute();
        return view;
    }

    public String ubahKeRupiah(int nominal) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format((double) nominal);
    }

    private void setDataKeuangan(int pemasukkan, int pengeluaran) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(41f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        if (String.valueOf(pemasukkan).isEmpty() || String.valueOf(pengeluaran).isEmpty()) {
            yValues.add(new PieEntry(0, "Pemasukkan"));
            yValues.add(new PieEntry(0, "Pengeluaran"));
        } else {
            yValues.add(new PieEntry(pemasukkan, "Pemasukkan"));
            yValues.add(new PieEntry(pengeluaran, "Pengeluaran"));
        }

        SharedPreferences preferences = getActivity()
                .getSharedPreferences(Session.PREF_NAME, 0);
        PieDataSet dataSet = new PieDataSet(yValues, preferences.getString("nama_buku","catatan keuangan"));
        dataSet.setSliceSpace(1f);
        dataSet.setSelectionShift(3f);

        /*
        CUSTOM COLOR jika mau

        final int[] MY_COLORS = {Color.rgb(192,0,0), Color.rgb(255,0,0), Color.rgb(255,192,0),
                Color.rgb(127,127,127), Color.rgb(146,208,80), Color.rgb(0,176,80), Color.rgb(79,129,189)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        dataSet.setColors(colors);

         */
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        totalPemasukkanView.setText(ubahKeRupiah(pemasukkan));
        totalPengeluaranView.setText(ubahKeRupiah(pengeluaran));
        sisaUang.setText(ubahKeRupiah(pemasukkan-pengeluaran));
    }

    //berfungsi untuk mengambil data buku ke database
    private class GraphPPAsyncTask extends AsyncTask<String, String, String> {

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
                Log.e("id user", idU);
                Log.e("id buku", idBuku);
                String url = Constrants.URL_GET_PP + "&owner=" + idU + "&id_buku=" + idBuku;
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
                    int totalPemasukkan = 0, totalPengeluaran = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        totalPemasukkan += object.getInt("total_pemasukkan");
                        totalPengeluaran += object.getInt("total_pengeluaran");
                    }
                    setDataKeuangan(totalPemasukkan, totalPengeluaran);

                } else {
                    setDataKeuangan(0, 0);

                }

            } catch (Exception e) {
                setDataKeuangan(0, 0);
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }

}
