package com.example.depran.nimoc.arsip;

import android.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CatatanArsipFragment extends Fragment {
    public static BerandaActivity berandaActivity;
    ListView listView;
    private static ArrayList<CatatanKeuangan> catatanKeuangans;
    public static CatatanArsipFragment newInstance(BerandaActivity activity, JSONArray jsonArray) {
        berandaActivity = activity;
        catatanKeuangans = new ArrayList<CatatanKeuangan>();
        try {
            setCatatanKeuangan(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json e : ",e.getMessage().toString());
        }
        return new CatatanArsipFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(berandaActivity).inflate(R.layout.fragment_catatan_arsip, container, false);


        CatatanArsipAdapter adapter = new CatatanArsipAdapter(getActivity(), catatanKeuangans);
        listView = (ListView) view.findViewById(R.id.lsView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.id_divisi)).getText().toString();

                ImageView unArsipBtn = (ImageView) view.findViewById(R.id.unarsip_btn);
                unArsipBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "btn click", Toast.LENGTH_SHORT).show();
                    }
                });

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
