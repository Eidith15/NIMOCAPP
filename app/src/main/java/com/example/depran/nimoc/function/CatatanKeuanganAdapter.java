package com.example.depran.nimoc.function;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.depran.nimoc.R;

import java.util.ArrayList;

public class CatatanKeuanganAdapter extends ArrayAdapter<CatatanKeuangan> {

    public CatatanKeuanganAdapter(Activity context, ArrayList<CatatanKeuangan> catatanKeuangans) {
        super(context, 0, catatanKeuangans);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_catatan_keuangan, parent, false);
        }

        CatatanKeuangan currentKeuangan = getItem(position);


        TextView namaBukuTextView = (TextView) listItemView.findViewById(R.id.nama_buku);
        namaBukuTextView.setText(currentKeuangan.getmNamaBuku());

        TextView idBukuTextView = (TextView) listItemView.findViewById(R.id.id_buku);
        idBukuTextView.setText(currentKeuangan.getmIdBuku());


        return listItemView;
    }
}
