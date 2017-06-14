package com.skripsi.anna;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skripsi.anna.adapter.Barang;
import com.skripsi.anna.app.AppController;
import com.skripsi.anna.model.ModelMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Detail extends AppCompatActivity {
    private RecyclerView rv_barang;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmBarang;
    private List<ModelMenu> modelMenus = new ArrayList<ModelMenu>();

    Button peta;
    TextView namaKuliner,kategoriKuliner,alamatKuliner;
    NetworkImageView imageKuliner;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Bundle b = getIntent().getExtras();
        GetMenuByIdKuliner(b.getString("id_kuliner"));

        peta = (Button) findViewById(R.id.button);
        peta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("id_kuliner",b.getString("id_kuliner"));
                intent.putExtra("longitude",b.getString("longitude"));
                intent.putExtra("latitude",b.getString("latitude"));
                intent.putExtra("title",b.getString("nama_kuliner"));
                intent.putExtra("snippet",b.getString("kategori_kuliner") +" : "+b.getString("alamat_kuliner"));
                startActivity(intent);
            }
        });
        imageKuliner = (NetworkImageView) findViewById(R.id.image_kuliner);
        namaKuliner = (TextView) findViewById(R.id.nama_kuliner);
        kategoriKuliner = (TextView) findViewById(R.id.kategori_kuliner);
        alamatKuliner = (TextView) findViewById(R.id.alamat_kuliner);
        rv_barang = (RecyclerView) findViewById(R.id.rv_barang);
        rv_barang.setHasFixedSize(true);

        imageKuliner.setImageUrl(Constant.Image+b.getString("gambar_kuliner"),imageLoader);
        namaKuliner.setText(b.getString("nama_kuliner"));
        kategoriKuliner.setText(b.getString("kategori_kuliner"));
        alamatKuliner.setText(b.getString("alamat_kuliner"));

        lmBarang = new LinearLayoutManager(this);
        rv_barang.setLayoutManager(lmBarang);
        adapter = new Barang(modelMenus);
        rv_barang.setAdapter(adapter);
    }
    private void GetMenuByIdKuliner( String IdKuliner){
        StringRequest stringRequest = new StringRequest(Method.GET, Constant.GetMenu + IdKuliner, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("id_menu")!= "fail"){
                            ModelMenu modelMenu = new ModelMenu();
                            modelMenu.setIdMenu(jsonObject.getString("id_menu"));
                            modelMenu.setNamaMenu(jsonObject.getString("nama_menu"));
                            modelMenu.setHargaMenu(jsonObject.getString("harga_menu"));
                            modelMenus.add(modelMenu);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getCurrentFocus(), "Terjadi Galat Untuk Mendapatkan Menu", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
