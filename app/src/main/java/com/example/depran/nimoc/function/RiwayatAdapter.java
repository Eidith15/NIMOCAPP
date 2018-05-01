package com.example.depran.nimoc.function;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.depran.nimoc.R;

import java.util.ArrayList;

public class RiwayatAdapter extends ArrayAdapter<Riwayat> {

    public RiwayatAdapter(Activity context, ArrayList<Riwayat> riwayats) {
        super(context, 0, riwayats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_riwayat, parent, false);
        }

        Riwayat currentRiwayat = getItem(position);


        TextView divisiTextView = (TextView) listItemView.findViewById(R.id.divisi_riwayat);
        divisiTextView.setText(currentRiwayat.getmDivisi());

        TextView nominalTextView = (TextView) listItemView.findViewById(R.id.nominal_riwayat);
        nominalTextView.setText(currentRiwayat.getmNominal());
        if (currentRiwayat.getKodeP()==1){
            nominalTextView.setTextColor(Color.GREEN);
        }else{
            nominalTextView.setTextColor(Color.RED);
        }

        TextView tanggalTextView = (TextView) listItemView.findViewById(R.id.hariRiwayat);
        tanggalTextView.setText(currentRiwayat.getmTanggal());



        return listItemView;
    }
}
