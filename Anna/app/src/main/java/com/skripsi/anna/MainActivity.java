package com.skripsi.anna;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skripsi.anna.adapter.Pencarian;
import com.skripsi.anna.model.ModelPencarian;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ModelPencarian> modelPencarian = new ArrayList<ModelPencarian>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetData();
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        adapter = new Pencarian(modelPencarian);
        rvView.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cari(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cari(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent intent = new Intent(this,MainAdmin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void Cari(String keyword){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.CariKuliner+keyword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            modelPencarian.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelPencarian pencarian = new ModelPencarian();
                                pencarian.setNamaKuliner(jsonObject.getString("nama_kuliner"));
                                pencarian.setIdKuliner(jsonObject.getString("id_kuliner"));
                                pencarian.setFotoKuliner(jsonObject.getString("gambar_kuliner"));
                                pencarian.setKategoriKuliner(jsonObject.getString("kategori_kuliner"));
                                pencarian.setAlamatKuliner(jsonObject.getString("alamat_kuliner"));
                                pencarian.setLatitudeKuliner(jsonObject.getString("latitude"));
                                pencarian.setLongitudeKuliner(jsonObject.getString("longitude"));
                                modelPencarian.add(pencarian);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getCurrentFocus(), "Terjadi Galat Saat Mencari Kuliner", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
    private void GetData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Kuliner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            modelPencarian.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelPencarian pencarian = new ModelPencarian();
                                pencarian.setNamaKuliner(jsonObject.getString("nama_kuliner"));
                                pencarian.setIdKuliner(jsonObject.getString("id_kuliner"));
                                pencarian.setFotoKuliner(jsonObject.getString("gambar_kuliner"));
                                pencarian.setKategoriKuliner(jsonObject.getString("kategori_kuliner"));
                                pencarian.setAlamatKuliner(jsonObject.getString("alamat_kuliner"));
                                pencarian.setLatitudeKuliner(jsonObject.getString("latitude"));
                                pencarian.setLongitudeKuliner(jsonObject.getString("longitude"));
                                modelPencarian.add(pencarian);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getCurrentFocus(), "Terjadi Galat Saat Mengambil data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
