package com.example.depran.nimoc.function;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.depran.nimoc.R;

import java.util.ArrayList;

public class DivisiAdapter extends ArrayAdapter<Divisi> {

    public DivisiAdapter(Activity context, ArrayList<Divisi> riwayats) {
        super(context, 0, riwayats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_divisi, parent, false);
        }

        Divisi currentDivisi = getItem(position);

        TextView idTextView = (TextView) listItemView.findViewById(R.id.id_buku);
        idTextView.setText(currentDivisi.getIdDivisi());

        TextView divisiTextView = (TextView) listItemView.findViewById(R.id.nama_divisi);
        divisiTextView.setText(currentDivisi.getNamaDivisi());

        return listItemView;
    }
}
