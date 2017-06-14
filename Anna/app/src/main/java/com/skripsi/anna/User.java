package com.skripsi.anna;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link User.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link User#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User extends Fragment {
    EditText Nama,Email,Hp;
    Button Update;
    SessionManager session;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public User() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User.
     */
    // TODO: Rename and change types and number of parameters
    public static User newInstance(String param1, String param2) {
        User fragment = new User();
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
        final View view =  inflater.inflate(R.layout.fragment_user, container, false);
        session = new SessionManager(view.getContext());
        final HashMap<String, String> user = session.getUserDetails();
        Nama = (EditText) view.findViewById(R.id.Nama);
        Email = (EditText) view.findViewById(R.id.Email);
        Hp = (EditText) view.findViewById(R.id.Hp);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GetUser+user.get(SessionManager.KEY_ID), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!jsonObject.getString("id_user").equals("fail")){
                            Nama.setText(jsonObject.getString("nama_user"));
                            Email.setText(jsonObject.getString("email_user"));
                            Hp.setText(jsonObject.getString("hp_user"));
                        }
                        else {
                            Snackbar.make(getView(), "Data Pengguna Tidak Ditemukan", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getView(), "Terjadi Galat saat mengambil data pengguna", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);

        Update = (Button) view.findViewById(R.id.Update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.Login, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!jsonObject.getString("nama_user").equals("gagal_update_data")){
                                    session.createLoginSession(jsonObject.getString("nama_user"),jsonObject.getString("email_user"),user.get(SessionManager.KEY_ID),jsonObject.getString("hp_user"),user.get(SessionManager.KEY_PASSWORD));
                                    Snackbar.make(v, "Data pengguna Sudah diupdate", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else{
                                    Snackbar.make(v, "Gaga Merubah Data Pengguna", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(v, "Terjadi Galat Saat merubah data pengguna", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("upd", "y");
                        params.put("id_user",user.get(SessionManager.KEY_ID));
                        params.put("nama_user", Nama.getText().toString());
                        params.put("email_user", Email.getText().toString());
                        params.put("hp_user", Hp.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                requestQueue.add(stringRequest);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
