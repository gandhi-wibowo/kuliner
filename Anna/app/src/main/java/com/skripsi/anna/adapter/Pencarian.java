package com.skripsi.anna.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.skripsi.anna.Constant;
import com.skripsi.anna.Detail;
import com.skripsi.anna.R;
import com.skripsi.anna.app.AppController;
import com.skripsi.anna.model.ModelPencarian;

import java.util.List;

/**
 * Created by gandhi on 6/11/17.
 */

public class Pencarian extends RecyclerView.Adapter<Pencarian.ViewHolder> {
    private List<ModelPencarian> pencarianList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public Pencarian(List<ModelPencarian> pencarianList){this.pencarianList = pencarianList;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKuliner;
        public TextView alamatKuliner;
        public TextView kategoriKuliner;
        public NetworkImageView imageKuliner;

        public ViewHolder(View itemView) {
            super(itemView);
            imageKuliner = (NetworkImageView)itemView.findViewById(R.id.image_kuliner);
            namaKuliner = (TextView) itemView.findViewById(R.id.nama_kuliner);
            alamatKuliner = (TextView) itemView.findViewById(R.id.alamat_kuliner);
            kategoriKuliner = (TextView) itemView.findViewById(R.id.kategori_kuliner);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    ModelPencarian modelPencarian = pencarianList.get(position);
                    Intent intent = new Intent(v.getContext(), Detail.class);
                    intent.putExtra("id_kuliner",modelPencarian.getIdKuliner());
                    intent.putExtra("nama_kuliner",modelPencarian.getNamaKuliner());
                    intent.putExtra("gambar_kuliner",modelPencarian.getFotoKuliner());
                    intent.putExtra("kategori_kuliner",modelPencarian.getKategoriKuliner());
                    intent.putExtra("alamat_kuliner",modelPencarian.getAlamatKuliner());
                    intent.putExtra("latitude",modelPencarian.getLatitudeKuliner());
                    intent.putExtra("longitude",modelPencarian.getLongitudeKuliner());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pencarian, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelPencarian item = pencarianList.get(position);
        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();
        holder.imageKuliner.setImageUrl(Constant.Image+item.getFotoKuliner(),imageLoader);
        holder.namaKuliner.setText(item.getNamaKuliner());
        holder.alamatKuliner.setText(item.getAlamatKuliner());
        holder.kategoriKuliner.setText(item.getKategoriKuliner());
    }

    @Override
    public int getItemCount() {
        return this.pencarianList.size();
    }


}
