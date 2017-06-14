package com.skripsi.anna;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.skripsi.anna.app.AppController;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Kuliner.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Kuliner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Kuliner extends Fragment {
    EditText Kuliner,Alamat,Kategori;
    Button Tambah,Update;
    NetworkImageView imageView;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    SessionManager session;
    String IdKuliner;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Kuliner() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Kuliner.
     */
    // TODO: Rename and change types and number of parameters
    public static Kuliner newInstance(String param1, String param2) {
        Kuliner fragment = new Kuliner();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            System.out.println(filePath);
            Glide.with(this).load(filePath).into(imageView);

        }
    }
    public void loadImagefromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, " "), PICK_IMAGE_REQUEST);
    }

    public String getPath(Context context,Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_kuliner, container, false);
        session = new SessionManager(view.getContext());
        final HashMap<String, String> user = session.getUserDetails();

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();
        Kuliner = (EditText) view.findViewById(R.id.Kuliner);
        Kategori = (EditText) view.findViewById(R.id.Kategori);
        Alamat = (EditText) view.findViewById(R.id.Alamat);
        imageView = (NetworkImageView) view.findViewById(R.id.gambar);
        Tambah = (Button) view.findViewById(R.id.Tambah);
        Tambah.setVisibility(View.GONE);
        Update = (Button) view.findViewById(R.id.Update);
        Update.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GetByIdUser+user.get(SessionManager.KEY_ID),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!jsonObject.getString("id_kuliner").equals("fail")){
                                    IdKuliner = jsonObject.getString("id_kuliner");
                                    Kuliner.setText(jsonObject.getString("nama_kuliner"));
                                    Kategori.setText(jsonObject.getString("kategori_kuliner"));
                                    Alamat.setText(jsonObject.getString("alamat_kuliner"));
                                    imageView.setImageUrl(Constant.Image+jsonObject.getString("gambar_kuliner"),imageLoader);
                                    Update.setVisibility(View.VISIBLE);
                                }
                                else{
                                    imageView.setImageUrl(Constant.Image+"no_img.jpg",imageLoader);
                                    Tambah.setVisibility(View.VISIBLE);
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
                        Snackbar.make(view, "Terjadi Galat saat mengambil data kuliner", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                }
        ) {
            @Override
            public String getBodyContentType() { return "application/x-www-form-urlencoded";}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);


        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(filePath != null){
                    String uploadId = UUID.randomUUID().toString();
                    String path = getPath(v.getContext(),filePath);
                    try {
                        new MultipartUploadRequest(v.getContext(), uploadId, Constant.Kuliner)
                                .addFileToUpload(path, "images") //Adding file
                                .addParameter("new","y")
                                .addParameter("id_user", user.get(SessionManager.KEY_ID))
                                .addParameter("nama_kuliner", Kuliner.getText().toString())
                                .addParameter("alamat_kuliner",Alamat.getText().toString())
                                .addParameter("kategori_kuliner",Kategori.getText().toString())
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload();
                        Kuliner kuliner = new Kuliner();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, kuliner).commit();
                        Snackbar.make(v, "Kuliner Sudah Ditambahkan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Kuliner,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Kuliner kuliner = new Kuliner();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, kuliner).commit();
                                    Snackbar.make(v, "Kuliner Sudah Ditambahkan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Snackbar.make(v, "Terjadi Galat Saat menambahkan kuliner", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                    ) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded";
                        }

                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("new","y");
                            params.put("id_user",user.get(SessionManager.KEY_ID));
                            params.put("nama_kuliner",Kuliner.getText().toString());
                            params.put("alamat_kuliner",Alamat.getText().toString());
                            params.put("kategori_kuliner",Kategori.getText().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(filePath != null){
                    String uploadId = UUID.randomUUID().toString();
                    String path = getPath(v.getContext(),filePath);
                    try {
                        new MultipartUploadRequest(v.getContext(), uploadId, Constant.Kuliner)
                                .addFileToUpload(path, "images") //Adding file
                                .addParameter("upd","y")
                                .addParameter("id_kuliner", IdKuliner)
                                .addParameter("nama_kuliner", Kuliner.getText().toString())
                                .addParameter("alamat_kuliner",Alamat.getText().toString())
                                .addParameter("kategori_kuliner",Kategori.getText().toString())
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload();
                        Kuliner kuliner = new Kuliner();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, kuliner).commit();
                        Snackbar.make(v, "Kuliner Sudah Diupdate", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Kuliner,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Kuliner kuliner = new Kuliner();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.flContent, kuliner).commit();
                                    Snackbar.make(v, "Kuliner Sudah Diupdate", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Snackbar.make(v, "Terjadi Galat Saat Update", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                    ) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded";
                        }

                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("upd","y");
                            params.put("id_kuliner",IdKuliner);
                            params.put("nama_kuliner",Kuliner.getText().toString());
                            params.put("alamat_kuliner",Alamat.getText().toString());
                            params.put("kategori_kuliner",Kategori.getText().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
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
