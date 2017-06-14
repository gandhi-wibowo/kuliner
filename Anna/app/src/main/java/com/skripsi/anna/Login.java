package com.skripsi.anna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class Login extends AppCompatActivity {
    EditText Email,Password;
    Button Login;
    TextView Daftar;
    ProgressDialog progressDialog;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);
        Login = (Button) findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(Email.getText().toString(),Password.getText().toString());
            }
        });
        Daftar = (TextView) findViewById(R.id.Daftar);
        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Daftar.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void Login(String Email,String Password){
        progressDialog = ProgressDialog.show(this,"Log in . .","Auth . .",true);
        if (!validate()) {
            onLoginFailed();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Login +"email="+Email+"&password="+Password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("status").equals("sukses")){
                            session.createLoginSession(jsonObject.getString("nama_user"),jsonObject.getString("email_user"),jsonObject.getString("id_user"),jsonObject.getString("hp_user"),jsonObject.getString("password_user"));
                            Intent intent = new Intent(getApplicationContext(),MainAdmin.class);
                            startActivity(intent);
                            Snackbar.make(getCurrentFocus(), "Selamat Datang " + jsonObject.getString("nama_user"), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        }
                        else{
                            progressDialog.dismiss();
                            onLoginFailed();
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
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void onLoginFailed() {
        Snackbar.make(getCurrentFocus(), "Gagal Login", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Login.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (email.isEmpty() ) {
            Email.setError("Email Tidak Boleh Kosong");
            valid = false;
        } else {
            Email.setError(null);
        }

        if (password.isEmpty() ) {
            Password.setError("Password Tidak Boleh Kosong");
            valid = false;
        } else {
            Password.setError(null);
        }

        return valid;
    }

}
