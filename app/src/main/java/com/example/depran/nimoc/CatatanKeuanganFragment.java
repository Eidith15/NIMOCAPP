package com.example.depran.nimoc;


import android.content.Intent;
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

import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.buku.TambahBukuKeuanganActivity;
import com.example.depran.nimoc.function.CatatanKeuangan;
import com.example.depran.nimoc.function.CatatanKeuanganAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CatatanKeuanganFragment extends Fragment {
    public static BerandaActivity berandaActivity;
    ListView listView;
    private static  ArrayList<CatatanKeuangan> catatanKeuangans;
    public static CatatanKeuanganFragment newInstance(BerandaActivity activity, JSONArray jsonArray) {
        berandaActivity = activity;
        catatanKeuangans = new ArrayList<CatatanKeuangan>();
        try {
            setCatatanKeuangan(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json e : ",e.getMessage().toString());
        }
        return new CatatanKeuanganFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(berandaActivity).inflate(R.layout.fragment_catatan_keuangan, container, false);


        CatatanKeuanganAdapter adapter = new CatatanKeuanganAdapter(getActivity(), catatanKeuangans);
        listView = (ListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.id_buku)).getText().toString();

                Toast toast = Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT);
                toast.show();
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

}
