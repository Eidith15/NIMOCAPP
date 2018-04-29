package com.example.depran.nimoc;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.depran.nimoc.function.Divisi;
import com.example.depran.nimoc.function.DivisiAdapter;
import com.example.depran.nimoc.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DivisiFragment extends Fragment {
    public static DashboardBukuActivity dashboardBA;
    SwipeMenuListView listView;
    private static ArrayList<Divisi> divisiArrayList;

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


        DivisiAdapter adapter = new DivisiAdapter(getActivity(), divisiArrayList);
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

                switch (index) {
                    case 0:
                        Log.d("click", "edit");
                        Intent intent = new Intent(getActivity(), EditBukuKeuanganActivity.class);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        Log.d("click", "delete");
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

                Session.createDivisiSession(getActivity(), selected);

                Toast toast = Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT);
                toast.show();
//                Intent intent = new Intent(getActivity(), DashboardBukuActivity.class);
//                startActivity(intent);
            }
        });
        return view;
    }

    private void konfirmasiHapus() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_logout)
                .content(R.string.content_hapus_divisi)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

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
}
