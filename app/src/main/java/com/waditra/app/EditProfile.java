package com.waditra.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private EditText txtNama, txtAlamat, txtTelpon, txtFb, txtTw, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        proses();
    }

    private void proses() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(DataLogin.USERNAME_SHARED_PREF,"Not Available");
        String nama = sharedPreferences.getString(DataLogin.NAME_SHARED_PREF,"Not Available");
        String alamat = sharedPreferences.getString(DataLogin.ALAMAT_SHARED_PREF,"Not Available");
        String telpon = sharedPreferences.getString(DataLogin.TELPON_SHARED_PREF,"Not Available");
        String fb = sharedPreferences.getString(DataLogin.FACEBOOK_SHARED_PREF,"Not Available");
        String tw = sharedPreferences.getString(DataLogin.TWITTER_SHARED_PREF,"Not Available");
        String email = sharedPreferences.getString(DataLogin.EMAIL_SHARED_PREF,"Not Available");

        txtNama = (EditText)findViewById(R.id.ubahNama);
        txtAlamat = (EditText)findViewById(R.id.ubahAlamat);
        txtTelpon = (EditText)findViewById(R.id.ubahTelpon);
        txtFb = (EditText)findViewById(R.id.ubahFB);
        txtTw = (EditText)findViewById(R.id.ubahTW);
        txtEmail = (EditText)findViewById(R.id.ubahEmail);

        txtNama.setText(nama);
        txtAlamat.setText(alamat);
        txtTelpon.setText(telpon);
        txtFb.setText(fb);
        txtTw.setText(tw);
        txtEmail.setText(email);
    }

    public void onBack3(View view){
        finish();
    }

    public void simpan(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            ProsesiEdit();
        }else{
            Intent i = new Intent(EditProfile.this, Koneksi.class);
            startActivity(i);
        }
    }

    private void ProsesiEdit() {
        txtNama = (EditText)findViewById(R.id.ubahNama);
        txtAlamat = (EditText)findViewById(R.id.ubahAlamat);
        txtTelpon = (EditText)findViewById(R.id.ubahTelpon);
        txtFb = (EditText)findViewById(R.id.ubahFB);
        txtTw = (EditText)findViewById(R.id.ubahTW);
        txtEmail = (EditText)findViewById(R.id.ubahEmail);

        final String name = txtNama.getText().toString().trim();
        final String address = txtAlamat.getText().toString().trim();
        final String tlp = txtTelpon.getText().toString().trim();
        final String fb = txtFb.getText().toString().trim();
        final String tw = txtTw.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, DataLogin.URL+"?update=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase(DataLogin.LOGIN_SUCCESS)){
                            SharedPreferences sharedPreferences = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(DataLogin.NAME_SHARED_PREF, name);
                            editor.commit();

                            SharedPreferences sharedPreferences2 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                            editor2.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor2.putString(DataLogin.ALAMAT_SHARED_PREF, address);
                            editor2.commit();

                            SharedPreferences sharedPreferences3 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                            editor3.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor3.putString(DataLogin.TELPON_SHARED_PREF,tlp );
                            editor3.commit();

                            SharedPreferences sharedPreferences4 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                            editor4.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor4.putString(DataLogin.FACEBOOK_SHARED_PREF, fb);
                            editor4.commit();

                            SharedPreferences sharedPreferences5 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor5 = sharedPreferences5.edit();
                            editor5.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor5.putString(DataLogin.TWITTER_SHARED_PREF, tw);
                            editor5.commit();

                            SharedPreferences sharedPreferences6 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor6 = sharedPreferences6.edit();
                            editor6.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor6.putString(DataLogin.EMAIL_SHARED_PREF, email);
                            editor6.commit();

                            //Simpan Nilai ke Variabel editor
                            editor.commit();

                            Toast.makeText(EditProfile.this, response, Toast.LENGTH_LONG).show();

                        }else{
                            if(response.equalsIgnoreCase(DataLogin.KEY_ADA)){

                            }else if(response.equalsIgnoreCase(DataLogin.KEY_FAILED)){
                                Toast.makeText(EditProfile.this, response, Toast.LENGTH_LONG).show();
                            }else{
                                SharedPreferences sharedPreferences = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                                editor.putString(DataLogin.NAME_SHARED_PREF, name);
                                editor.commit();

                                SharedPreferences sharedPreferences2 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                editor2.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                                editor2.putString(DataLogin.ALAMAT_SHARED_PREF, address);
                                editor2.commit();

                                SharedPreferences sharedPreferences3 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                                editor3.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                                editor3.putString(DataLogin.TELPON_SHARED_PREF,tlp );
                                editor3.commit();

                                SharedPreferences sharedPreferences4 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                                editor4.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                                editor4.putString(DataLogin.FACEBOOK_SHARED_PREF, fb);
                                editor4.commit();

                                SharedPreferences sharedPreferences5 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor5 = sharedPreferences5.edit();
                                editor5.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                                editor5.putString(DataLogin.TWITTER_SHARED_PREF, tw);
                                editor5.commit();

                                SharedPreferences sharedPreferences6 = EditProfile.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor6 = sharedPreferences6.edit();
                                editor6.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                                editor6.putString(DataLogin.EMAIL_SHARED_PREF, email);
                                editor6.commit();

                                //Simpan Nilai ke Variabel editor
                                editor.commit();

                                Toast.makeText(EditProfile.this, response, Toast.LENGTH_LONG).show();

                                if(response.equals("success")){
                                    Intent i = new Intent(EditProfile.this, Profil.class);
                                    startActivity(i);
                                }

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            protected Map<String, String> getParams(){
                SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String username = sharedPreferences.getString(DataLogin.USERNAME_SHARED_PREF,"Not Available");

                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("name", name);
                params.put("alamat", address);
                params.put("telpon", tlp);
                params.put("facebook", fb);
                params.put("twitter", tw);
                params.put("email", email);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
