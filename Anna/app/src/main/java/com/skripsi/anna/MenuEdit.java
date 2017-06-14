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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MenuEdit extends Fragment {


    private String idMenu,namaMenu,hargaMenu;
    EditText NamaMenu,HargaMenu;
    TextView Delete;
    Button Update;

    private OnFragmentInteractionListener mListener;

    public MenuEdit() {
    }

    public static MenuEdit newInstance(String idMenu, String namaMenu, String hargaMenu) {
        MenuEdit fragment = new MenuEdit();
        Bundle args = new Bundle();
        args.putString("idMenu", idMenu);
        args.putString("namaMenu", namaMenu);
        args.putString("hargaMenu",hargaMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idMenu = getArguments().getString("idMenu");
            namaMenu = getArguments().getString("namaMenu");
            hargaMenu = getArguments().getString("hargaMenu");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu_edit, container, false);
        NamaMenu = (EditText) view.findViewById(R.id.NamaMenu);
        HargaMenu = (EditText) view.findViewById(R.id.HargaMenu);
        Update = (Button) view.findViewById(R.id.Update);
        NamaMenu.setText(namaMenu);
        HargaMenu.setText(hargaMenu);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // tinggal di update
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.Kuliner, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("status").equals("sukses")){
                                    // intent ke menu
                                    Menu menu = new Menu();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                                    Snackbar.make(v, "Menu Sudah Di update", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else{
                                    Menu menu = new Menu();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                                    Snackbar.make(v, "Menu Gagal Di update", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Menu menu = new Menu();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                        Snackbar.make(v, "Terjadi Galat", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_menu", idMenu);
                        params.put("nama_menu", NamaMenu.getText().toString());
                        params.put("harga_menu", HargaMenu.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
            }
        });
        Delete = (TextView) view.findViewById(R.id.Delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.Kuliner, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("status").equals("sukses")){
                                    // intent ke menu
                                    Menu menu = new Menu();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                                    Snackbar.make(v, "Menu Sudah Di Hapus", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else{
                                    Menu menu = new Menu();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                                    Snackbar.make(v, "Menu Gagal Di Hapus", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Menu menu = new Menu();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, menu).commit();
                        Snackbar.make(v, "Terjadi Galat", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_del", idMenu);
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
