package com.skripsi.anna.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skripsi.anna.R;
import com.skripsi.anna.model.ModelMenu;

import java.util.List;

/**
 * Created by gandhi on 6/11/17.
 */

public class Barang extends RecyclerView.Adapter<Barang.ViewHolder> {

    private List<ModelMenu> modelMenus;

    public Barang(List<ModelMenu> modelMenus){
        this.modelMenus = modelMenus;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView namaBarang,hargaBarang;
        public ViewHolder(View v) {
            super(v);
            namaBarang = (TextView) v.findViewById(R.id.nama_barang);
            hargaBarang = (TextView) v.findViewById(R.id.harga);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelMenu modelMenu = modelMenus.get(position);
        holder.hargaBarang.setText(modelMenu.getHargaMenu());
        holder.namaBarang.setText(modelMenu.getNamaMenu());

    }

    @Override
    public int getItemCount() {
        return modelMenus.size();
    }



}
