package com.example.depran.nimoc;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.depran.nimoc.buku.DashboardBukuActivity;
import com.example.depran.nimoc.buku.EditBukuKeuanganActivity;
import com.example.depran.nimoc.divisi.EditDivisiActivity;
import com.example.depran.nimoc.divisi.TambahDivisiActivity;
import com.example.depran.nimoc.function.Divisi;
import com.example.depran.nimoc.function.DivisiAdapter;
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
public class DivisiFragment extends Fragment {
    public static DashboardBukuActivity dashboardBA;
    SwipeMenuListView listView;
    private static ArrayList<Divisi> divisiArrayList;
    DivisiAdapter adapter;
    int pos;

    public static DivisiFragment newInstance(DashboardBukuActivity activity, JSONArray jsonArray) {
        dashboardBA = activity;
        divisiArrayList = new ArrayList<Divisi>();
        try {
            setDivisi(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json e : ", e.getMessage().toString());
        }
        return new DivisiFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(dashboardBA).inflate(R.layout.fragment_catatan_keuangan, container, false);


        adapter = new DivisiAdapter(getActivity(), divisiArrayList);
        listView = (SwipeMenuListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editBukuItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                editBukuItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                editBukuItem.setWidth(170);
                // set item title
                editBukuItem.setTitle("Edit");
                // set item title fontsize
                editBukuItem.setTitleSize(18);
                // set item title font color
                editBukuItem.setTitleColor(Color.WHITE);
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

                String idDivisi = ((TextView) listView.getChildAt(position).findViewById(R.id.id_divisi)).getText().toString().trim();
                String namaDivisi = ((TextView) listView.getChildAt(position).findViewById(R.id.nama_divisi)).getText().toString().trim();
                switch (index) {

                    case 0:
                        Log.d("click", "edit");
                        Intent intent = new Intent(getActivity(), EditDivisiActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("id_divisi", idDivisi);
                        intent.putExtra("nama_divisi", namaDivisi);
                        startActivityForResult(intent, 1);
                        break;
                    case 1:
                        Log.d("click", "delete");
                        pos = position;
                        konfirmasiHapus(idDivisi);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.id_divisi)).getText().toString();

                Session.createDivisiSession(getActivity(), selected);

                Toast toast = Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT);
                toast.show();
//                Intent intent = new Intent(getActivity(), DashboardBukuActivity.class);
//                startActivity(intent);
            }
        });
        view.findViewById(R.id.btnTambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahDivisiActivity.class);
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

                String idDivisi = data.getStringExtra("com.example.depran.nimoc.id_divisi")
                        ,namaDivisi =data.getStringExtra("com.example.depran.nimoc.nama_divisi");

                int position = data.getIntExtra("com.example.depran.nimoc.position", -99);

                SharedPreferences preferences = getActivity()
                        .getSharedPreferences(Session.PREF_NAME, 0);
                String idBuku = preferences.getString("id_buku", null);
                //idDivisi, idBuku, namaDivisi
                divisiArrayList.set(position, new Divisi(idDivisi, idBuku, namaDivisi));
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void refreshListHapus(int i){
        divisiArrayList.remove(i);
        adapter.notifyDataSetChanged();
    }

    private void konfirmasiHapus(final String idDivisi) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_logout)
                .content(R.string.content_hapus_divisi)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        new HapusDivisiAsyncTask().execute(idDivisi);
                    }
                })
                .show();
    }

    public static void setDivisi(JSONArray jsonArray) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String idDivisi = object.getString("id_divisi");
            String idBuku = object.getString("f_id_ck");
            String namaDivisi = object.getString("nama_divisi");
            divisiArrayList.add(new Divisi(idDivisi, idBuku, namaDivisi));
        }
    }

    //berfungsi untuk menghapus data buku ke database
    private class HapusDivisiAsyncTask extends AsyncTask<String, String, String> {

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
                String url = Constrants.URL_GET_DIVISI;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences(Session.PREF_NAME, 0);
                postParameters.add(new BasicNameValuePair("action", "delete"));
                postParameters.add(new BasicNameValuePair("id_divisi", params[0]));

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
                    Toast.makeText(getActivity(), "Divisi anda telah berhasil di hapus", Toast.LENGTH_LONG).show();
                    refreshListHapus(pos);
                } else {
                    Toast.makeText(getActivity(), "Hapus Divisi gagal", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk", "-> " + e.getMessage());
            }
        }
    }

}
