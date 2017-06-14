package com.skripsi.anna;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MenuTambah extends Fragment {
    private String idKuliner;
    EditText NamaMenu, HargaMenu;
    Button Tambah;

    private OnFragmentInteractionListener mListener;

    public MenuTambah() {
    }
    public static MenuTambah newInstance(String idKuliner) {
        MenuTambah fragment = new MenuTambah();
        Bundle args = new Bundle();
        args.putString("id_kuliner", idKuliner);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idKuliner = getArguments().getString("id_kuliner");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_tambah, container, false);
        NamaMenu = (EditText) view.findViewById(R.id.NamaMenu);
        HargaMenu = (EditText) view.findViewById(R.id.HargaMenu);
        Tambah = (Button) view.findViewById(R.id.Tambah);
        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Kuliner,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Menu menu = new Menu();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                                Snackbar.make(v, "Menu Sudah Ditambahkan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar.make(v, "Terjadi Galat", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        }
                ) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_kuliner",idKuliner);
                        params.put("nama_menu",NamaMenu.getText().toString());
                        params.put("harga_menu",HargaMenu.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
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
}
