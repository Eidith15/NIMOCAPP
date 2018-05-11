package com.example.depran.nimoc;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.buku.EditBukuKeuanganActivity;
import com.example.depran.nimoc.buku.TambahBukuKeuanganActivity;
import com.example.depran.nimoc.function.CatatanKeuangan;
import com.example.depran.nimoc.function.CatatanKeuanganAdapter;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatatanKeuanganFragment extends Fragment {
    public static BerandaActivity berandaActivity;
    SwipeMenuListView listView;
    private static ArrayList<CatatanKeuangan> catatanKeuangans;
    private Object mAppList;
    CatatanKeuanganAdapter adapter;
    int pos;

    public static CatatanKeuanganFragment newInstance(BerandaActivity activity, JSONArray jsonArray) {
        berandaActivity = activity;
        catatanKeuangans = new ArrayList<CatatanKeuangan>();
        try {
            setCatatanKeuangan(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json e : ", e.getMessage().toString());
        }
        return new CatatanKeuanganFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(berandaActivity).inflate(R.layout.fragment_catatan_keuangan, container, false);
        mAppList = getActivity().getPackageManager().getInstalledApplications(0);

        adapter = new CatatanKeuanganAdapter(getActivity(), catatanKeuangans);
        listView = (SwipeMenuListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "arsip" item
                SwipeMenuItem arsipBukuItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                arsipBukuItem.setBackground(new ColorDrawable(Color.rgb(0x99, 0x99, 0x99)));
                // set item width
                arsipBukuItem.setWidth(170);
                // set a icon
                arsipBukuItem.setIcon(R.drawable.ic_archive_black_24dp);
                // add to menu
                menu.addMenuItem(arsipBukuItem);


                // create "open" item
                SwipeMenuItem editBukuItem = new SwipeMenuItem(
                        getActivity());
                // set item background
//                editBukuItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));

                editBukuItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x98, 0x00)));
                // set item width
                editBukuItem.setWidth(170);
                // set a icon
                editBukuItem.setIcon(R.drawable.ic_edit_black_24dp);
                // add to menu
                menu.addMenuItem(editBukuItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background #F44336
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF4,
                        0x43, 0x36)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.e("get view", getView().toString());
                String idBuku = ((TextView) listView.getChildAt(position).findViewById(R.id.id_buku)).getText().toString().trim();
                String namaBuku = ((TextView) listView.getChildAt(position).findViewById(R.id.nama_buku)).getText().toString().trim();
                String ketBuku = ((TextView) listView.getChildAt(position).findViewById(R.id.ket_buku)).getText().toString().trim();
                switch (index) {
                    case 0:
                        Log.d("click", "edit");
                        Log.d("position", position + "");
                        pos = position;

                        new ArsipBukuAsyncTask().execute(idBuku);
                        break;
                    case 1:
                        Log.d("click", "edit");
                        Log.d("position", position + "");
                        Intent intent = new Intent(getActivity(), EditBukuKeuanganActivity.class);

                        String nama_buku_d = ((TextView) listView.getChildAt(position).findViewById(R.id.nama_buku)).getText().toString().trim();
                        Log.d("nama_buku_d", nama_buku_d);
                        Log.d("item ke", listView.getItemAtPosition(position).toString() + "");
                        intent.putExtra("position", position);
                        intent.putExtra("id_buku", idBuku);
                        intent.putExtra("nama_buku", namaBuku);
                        intent.putExtra("ket_buku", ketBuku);
                        startActivityForResult(intent, 1);
                        break;
                    case 2:
                        Log.d("click", "delete");
                        pos = position;
                        konfirmasiHapus(idBuku);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;

            }
        });
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                // selected item
                String idBuku1 = ((TextView) view.findViewById(R.id.id_buku)).getText().toString();
                String namaBuku = ((TextView) view.findViewById(R.id.nama_buku)).getText().toString().trim();

                Session.createBukuSession(getActivity(), idBuku1, namaBuku);
                final String idBuku = ((TextView) listView.getChildAt(i).findViewById(R.id.id_buku)).getText().toString().trim();
                Intent intent = new Intent(getActivity(), DashboardBukuActivity.class);
//                Toast toast = Toast.makeText(getActivity(), idBuku1, Toast.LENGTH_SHORT);
//                toast.show();
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btnTambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahBukuKeuanganActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("balasan", "edit 1");

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d("balasan", data.toString());

                String idBuku = data.getStringExtra("com.example.depran.nimoc.id_buku"), namaBuku = data.getStringExtra("com.example.depran.nimoc.nama_buku"), ketBuku = data.getStringExtra("com.example.depran.nimoc.ket_buku");

                int position = data.getIntExtra("com.example.depran.nimoc.position", -99);

                SharedPreferences preferences = getActivity()
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String owner = preferences.getString("id_u", null);
                catatanKeuangans.set(position, new CatatanKeuangan(new String[]{idBuku, owner, namaBuku, ketBuku, "0"}));
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void refreshListHapus(int i) {
        catatanKeuangans.remove(i);
        adapter.notifyDataSetChanged();
    }

    private void konfirmasiHapus(final String id) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_logout)
                .content(R.string.content_hapus_buku)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        new HapusBukuAsyncTask().execute(id);
                    }
                })
                .show();
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
    private class HapusBukuAsyncTask extends AsyncTask<String, String, String> {

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
                postParameters.add(new BasicNameValuePair("action", "delete"));
                postParameters.add(new BasicNameValuePair("id_buku", params[0]));

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
                    Toast.makeText(getActivity(), "Buku anda telah berhasil di hapus", Toast.LENGTH_LONG).show();
                    refreshListHapus(pos);
                } else {
                    Toast.makeText(getActivity(), "Hapus Buku gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }

    //berfungsi untuk menghapus data buku ke database
    private class ArsipBukuAsyncTask extends AsyncTask<String, String, String> {

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
                postParameters.add(new BasicNameValuePair("status_simpan", "1"));

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
                    Toast.makeText(getActivity(), "Buku anda telah berhasil di arsipkan", Toast.LENGTH_LONG).show();
                    refreshListHapus(pos);
                } else {
                    Toast.makeText(getActivity(), "Arsip Buku gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }
}
