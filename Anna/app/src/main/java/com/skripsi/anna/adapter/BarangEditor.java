package com.skripsi.anna.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentController;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skripsi.anna.MenuEdit;
import com.skripsi.anna.R;
import com.skripsi.anna.model.ModelMenu;

import java.util.List;

/**
 * Created by gandhi on 6/11/17.
 */

public class BarangEditor extends RecyclerView.Adapter<BarangEditor.ViewHolder> {

    private List<ModelMenu> modelMenus;

    public BarangEditor(List<ModelMenu> modelMenus){
        this.modelMenus = modelMenus;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView namaBarang,hargaBarang;
        public ViewHolder(View v) {
            super(v);
            namaBarang = (TextView) v.findViewById(R.id.nama_barang);
            hargaBarang = (TextView) v.findViewById(R.id.harga);
            v.setOnClickListener(new View.OnClickListener() {
                public FragmentController appCompatActivity;

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ModelMenu modelMenu = modelMenus.get(position);
                    MenuEdit menuEdit = new MenuEdit();
                    Bundle bundle = new Bundle();
                    bundle.putString("idMenu",modelMenu.getIdMenu());
                    bundle.putString("namaMenu",modelMenu.getNamaMenu());
                    bundle.putString("hargaMenu",modelMenu.getHargaMenu());
                    menuEdit.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, menuEdit).addToBackStack(null).commit();
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang_editor, parent, false);
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
