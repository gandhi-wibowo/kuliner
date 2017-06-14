package com.skripsi.anna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class Daftar extends AppCompatActivity {
    EditText Nama,Email,Hp,Password,RePassword;
    Button Daftar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        Nama = (EditText) findViewById(R.id.Nama);
        Email = (EditText) findViewById(R.id.Email);
        Hp = (EditText) findViewById(R.id.Hp);
        Password = (EditText) findViewById(R.id.Password);
        RePassword = (EditText) findViewById(R.id.RePassword);
        Daftar = (Button) findViewById(R.id.Daftar);
        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!validate()) {
                    onDaftarFailed("Gagal Mendaftar");
                    return;
                }
                progressDialog = ProgressDialog.show(v.getContext(),"Mendaftar . .","Auth . .",true);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Daftar, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("nama_user").equals("email_sudah_terdaftar")){
                                    progressDialog.dismiss();
                                    onDaftarFailed("Email Sudah Terdaftar");
                                }
                                else{
                                    Intent intent = new Intent(v.getContext(),Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Snackbar.make(v, "Silahkan Login Untuk Melanjutkan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nama_user", Nama.getText().toString());
                        params.put("email_user", Email.getText().toString());
                        params.put("hp_user", Hp.getText().toString());
                        params.put("password_user", RePassword.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });
    }
    public void onDaftarFailed(String pesan) {
        Snackbar.make(getCurrentFocus(), pesan, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Daftar.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String nama = Nama.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String repassword = RePassword.getText().toString();

        if (email.isEmpty() ) {
            Email.setError("Email Tidak Boleh Kosong");
            valid = false;
        } else {
            Email.setError(null);
        }
        if (nama.isEmpty() ) {
            Nama.setError("Nama Tidak Boleh Kosong");
            valid = false;
        } else {
            Nama.setError(null);
        }

        if (password.isEmpty() ) {
            Password.setError("Password Tidak Boleh Kosong");
            valid = false;
        } else {
            Password.setError(null);
        }

        if(repassword.isEmpty()){
            RePassword.setError("Password Tidak Boleh Kosong");
            valid = false;
        }else {
            RePassword.setError(null);
        }

        if(password.equals(repassword)){

        }
        else{
            RePassword.setError("Password Tidak Sesuai");
            valid = false;
        }


        return valid;
    }
}
