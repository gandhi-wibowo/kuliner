package com.skripsi.anna;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skripsi.anna.adapter.BarangEditor;
import com.skripsi.anna.model.ModelMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Menu extends Fragment {
    private RecyclerView rv_barang;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmBarang;
    private List<ModelMenu> modelMenus = new ArrayList<ModelMenu>();
    SessionManager session;
    String idKuliner;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Menu() {
    }
    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        session = new SessionManager(view.getContext());
        final HashMap<String, String> user = session.getUserDetails();

        GetIdKuliner(user.get(SessionManager.KEY_ID));

        rv_barang = (RecyclerView) view.findViewById(R.id.rv_barang);
        rv_barang.setHasFixedSize(true);
        lmBarang = new LinearLayoutManager(getActivity());
        rv_barang.setLayoutManager(lmBarang);
        adapter = new BarangEditor(modelMenus);
        rv_barang.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuTambah menuTambah = new MenuTambah();
                Bundle bundle = new Bundle();
                System.out.println(idKuliner);
                bundle.putString("id_kuliner",idKuliner);
                menuTambah.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, menuTambah).commit();
            }
        });
        return view;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private void GetIdKuliner(final String idUser){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GetByIdUser+idUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!jsonObject.getString("id_kuliner").equals("fail")){
                                    idKuliner = jsonObject.getString("id_kuliner");
                                    GetMenuByIdKuliner(jsonObject.getString("id_kuliner"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getView(), "Terjadi Galat", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() { return "application/x-www-form-urlencoded";}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void GetMenuByIdKuliner(String IdKuliner){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GetMenu + IdKuliner, new Response.Listener<String>() {
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
                Snackbar.make(getView(), "Terjadi Galat Saat Mengambil data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
