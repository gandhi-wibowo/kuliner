package com.skripsi.anna;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class MainAdmin extends AppCompatActivity implements
        MenuTambah.OnFragmentInteractionListener,
        MenuEdit.OnFragmentInteractionListener,
        My.OnFragmentInteractionListener,
        Logout.OnFragmentInteractionListener,
        User.OnFragmentInteractionListener,
        Password.OnFragmentInteractionListener,
        Menu.OnFragmentInteractionListener,
        Kuliner.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {


    TextView NamaKuliner,AlamatKuliner;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = Kuliner.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        NamaKuliner = (TextView) view.findViewById(R.id.NamaKuliner);
        AlamatKuliner = (TextView) view.findViewById(R.id.AlamatKuliner);
        navigationView.setNavigationItemSelectedListener(this);
        GetKuliner();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_kuliner) {
            fragmentClass = Kuliner.class;
        }else if (id == R.id.nav_user) {
            fragmentClass = User.class;
        }else if (id == R.id.nav_menu) {
            fragmentClass = Menu.class;
        }else if (id == R.id.nav_password) {
            fragmentClass = Password.class;
        }else if (id == R.id.nav_logout) {
            fragmentClass = Logout.class;
        }else if (id == R.id.nav_maps) {
            fragmentClass = My.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void jump(){
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = MenuEdit.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
    private void GetKuliner(){
        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(SessionManager.KEY_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GetByIdUser+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("id_kuliner").equals("fail")){
                                    Snackbar.make(getCurrentFocus(), "Kuliner Belum Dibuat", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else {
                                    NamaKuliner.setText(jsonObject.getString("nama_kuliner"));
                                    AlamatKuliner.setText(jsonObject.getString("alamat_kuliner"));
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
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
