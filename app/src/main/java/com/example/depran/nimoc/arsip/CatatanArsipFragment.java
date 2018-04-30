package com.example.depran.nimoc.arsip;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.function.CatatanArsipAdapter;
import com.example.depran.nimoc.function.CatatanKeuangan;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CatatanArsipFragment extends Fragment {
    public static BerandaActivity berandaActivity;
    ListView listView;
    CatatanArsipAdapter adapter;
    int pos;

    private static ArrayList<CatatanKeuangan> catatanKeuangans;

    public static CatatanArsipFragment newInstance(BerandaActivity activity, JSONArray jsonArray) {
        berandaActivity = activity;
        catatanKeuangans = new ArrayList<CatatanKeuangan>();
        try {
            setCatatanKeuangan(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json e : ", e.getMessage().toString());
        }
        return new CatatanArsipFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(berandaActivity).inflate(R.layout.fragment_catatan_arsip, container, false);


        adapter = new CatatanArsipAdapter(getActivity(), catatanKeuangans);
        listView = (ListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                // selected item
                final String selected = ((TextView) view.findViewById(R.id.id_buku)).getText().toString();

                ImageView unArsipBtn = (ImageView) view.findViewById(R.id.unarsip_btn);
                unArsipBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "btn click", Toast.LENGTH_SHORT).show();
                     new UnArsipBukuAsyncTask().execute(selected);
                     pos = i;
                    }
                });

            }
        });
        return view;
    }


    private void refreshListHapus(int i) {
        catatanKeuangans.remove(i);
        adapter.notifyDataSetChanged();
    }
    public static void setCatatanKeuangan(JSONArray jsonArray) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String idBuku = object.getString("id_buku");
            String idUser = object.getString("f_id_r");
            String namaBuku = object.getString("nama_buku");
            String keterangan = object.getString("keterangan");
            String statusSimpan = object.getString("status_simpan");
            catatanKeuangans.add(new CatatanKeuangan(new String[]{idBuku, idUser, namaBuku, keterangan, statusSimpan}));
        }
    }

    //berfungsi untuk menghapus data buku ke database
    private class UnArsipBukuAsyncTask extends AsyncTask<String, String, String> {

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
                String url = Constrants.URL_BUKU;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "update"));
                postParameters.add(new BasicNameValuePair("id_buku", params[0]));
                postParameters.add(new BasicNameValuePair("status_simpan", "0"));

                respon = CustomHttpClient.executeHttpPost(url, postParameters);

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
                JSONObject object = new JSONObject(result);
                if (object.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(getActivity(), "Buku anda telah berhasil di unarsipkan", Toast.LENGTH_LONG).show();
                    refreshListHapus(pos);
                } else {
                    Toast.makeText(getActivity(), "Unarsip Buku gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
