package com.example.depran.nimoc;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
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
import com.example.depran.nimoc.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatatanKeuanganFragment extends Fragment {
    public static BerandaActivity berandaActivity;
    SwipeMenuListView listView;
    private static ArrayList<CatatanKeuangan> catatanKeuangans;
    private Object mAppList;

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

        CatatanKeuanganAdapter adapter = new CatatanKeuanganAdapter(getActivity(), catatanKeuangans);
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
                    Log.e("get view",getView().toString());
                switch (index) {
                    case 0:
                        Log.d("click","edit");
                        Log.d("position",position+"");
                        Intent intent = new Intent(getActivity(), EditBukuKeuanganActivity.class);

                        String nama_buku_d = ((TextView) listView.getChildAt(position).findViewById(R.id.nama_buku)).getText().toString().trim();
                        Log.d("nama_buku_d", nama_buku_d);
                        Log.d("item ke",listView.getItemAtPosition(position).toString()+"");
                        String idBuku = ((TextView) listView.getChildAt(position).findViewById(R.id.id_buku)).getText().toString().trim();

                        String namaBuku = ((TextView) listView.getChildAt(position).findViewById(R.id.nama_buku)).getText().toString().trim();
                        String ketBuku = ((TextView) listView.getChildAt(position).findViewById(R.id.ket_buku)).getText().toString().trim();
                        intent.putExtra("id_buku", idBuku);
                        intent.putExtra("nama_buku", namaBuku);
                        intent.putExtra("ket_buku", ketBuku);
                        getActivity().startActivityForResult(intent, 1);
                        break;
                    case 1:
                        Log.d("click","delete");
                        konfirmasiHapus();
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
                String selected = ((TextView) view.findViewById(R.id.id_buku)).getText().toString();

                Session.createBukuSession(getActivity(), selected);
                ImageView arsipBtn = (ImageView) view.findViewById(R.id.arsip_btn);
                arsipBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "btn click", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(getActivity(), DashboardBukuActivity.class);

                Toast toast = Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT);
                toast.show();
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

    private void konfirmasiHapus(){
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_logout)
                .content(R.string.content_hapus_buku)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

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

    //berfungsi untuk mengambil data buku ke database
//    private class BukuAsyncTask extends AsyncTask<String, String, String> {
//
//        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(BerandaActivity.this);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String respon = "";
//            try {
//                SharedPreferences preferences = BerandaActivity.this
//                        .getSharedPreferences(Session.PREF_NAME, 0);
//                String id = preferences.getString("id_u", null);
//                String url = Constrants.URL_GET_BUKU+"&f_id_r="+id;
//                respon = CustomHttpClient.executeHttpGet(url);
//
//            } catch (Exception e) {
//                respon = e.toString();
//            }
//            return respon;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            progressDialog.dismiss();
//            Log.e("Respon"," -> "+result) ;
//            try {
//                JSONArray jsonArray = new JSONArray(result);
//                if (jsonArray.length()>0){
//                    changeContent(CatatanKeuanganFragment.newInstance(BerandaActivity.this,jsonArray));
//                }else{
//                    changeContent(BerandaFragment.newInstance(BerandaActivity.this));
//                }
//
//            } catch (Exception e) {
//                Toast.makeText(BerandaActivity.this, result.toString(), Toast.LENGTH_LONG).show();
//                Log.e("masuk","-> "+e.getMessage()) ;
//            }
//        }
//    }

}
