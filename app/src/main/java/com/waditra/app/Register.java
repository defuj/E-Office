package com.waditra.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.waditra.app.Alat.RequestHandler;
import com.waditra.app.HomeDrawer.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private String JSON_STRING;
    private EditText txtName, txtUsername, txtPassword1, txtPassword2;
    private CheckBox setuju;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setuju = (CheckBox)findViewById(R.id.cek_setuju);
        txtName = (EditText)findViewById(R.id.nama);
        txtUsername = (EditText)findViewById(R.id.nama_pengguna);
        txtPassword1 = (EditText)findViewById(R.id.sandi1);
        txtPassword2 = (EditText)findViewById(R.id.sandi2);
    }
    public void onRegister(View view){
        final String name = txtName.getText().toString().trim();
        final String username = txtUsername.getText().toString().trim();
        final String password = txtPassword1.getText().toString().trim();
        final String password2 = txtPassword2.getText().toString().trim();

        if(name.length() == 0){
            txtName.setError("Kosong");
            txtName.setFocusable(true);
        }else if(username.length() == 0){
            txtUsername.setError("Kosong");
            txtUsername.setFocusable(true);
        }else if(txtUsername.length() < 6){
            txtUsername.setError("Min 6 Karakter");
            txtUsername.setFocusable(true);
        }else if(txtPassword1.length() == 0){
            txtPassword1.setError("Kosong");
            txtPassword1.setFocusable(true);
        }else if(txtPassword1.length() < 6){
            txtPassword1.setError("Min 6 Karakter");
            txtPassword1.setFocusable(true);
        }else if(password.equals(password2)){
            if(!setuju.isChecked()){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Belum menyetujui kebijakan kami.");
                alertDialogBuilder.setPositiveButton("Mengerti",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                // Tampilkan alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else{
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    RegisterMasuk();
                }else{
                    Intent i = new Intent(Register.this, Koneksi.class);
                    startActivity(i);
                }
            }
        }else{
            txtPassword2.setError("Tidak sesuai");
            txtPassword2.setFocusable(true);
        }
    }

    private void RegisterMasuk() {
        final String name_str = txtName.getText().toString().trim();
        final String name = name_str.replaceAll(" ","_").toLowerCase();
        final String username_str = txtUsername.getText().toString().trim();
        final String username = username_str.replaceAll(" ","_").toLowerCase();
        final String password = txtPassword1.getText().toString().trim();

        pDialog = new ProgressDialog(Register.this);
        pDialog.setMessage("Mengirim data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                // Panggil method tampil data
                CekHasil();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?register=1&name="+name+"&username="+username+"&password="+password);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void CekHasil() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> dataUser = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String hasil = jo.getString(DataLogin.TAG_HASIL_REEGISTER);

                HashMap<String,String> userData = new HashMap<>();
                userData.put(DataLogin.TAG_HASIL_REEGISTER,hasil);
                dataUser.add(userData);

                pDialog.hide();
                if(hasil.equals("success")){
                    Toast.makeText(Register.this, "Berhasil mendaftar.", Toast.LENGTH_LONG).show();
                    mengambilDatkembali();
                }else if(hasil.equals("ada")){
                    Toast.makeText(Register.this, "Nama pengguna telah digunakan.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Register.this, "Terjadi kesalahan, silakan coba lagi.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mengambilDatkembali() {
        pDialog = new ProgressDialog(Register.this);
        pDialog.setMessage("Mengalihkan...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final String usernama = txtUsername.getText().toString().trim();
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                // Panggil method tampil data
                SimpanData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?data=1&username="+usernama);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void SimpanData() {
        String username = txtUsername.getText().toString().trim();
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> dataUser = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String name = jo.getString(DataLogin.NAME_SHARED_PREF);
                String password = jo.getString(DataLogin.PASSWORD_SHARED_PREF);
                String createdTime = jo.getString(DataLogin.CT_SHARED_PREF);

                String alamat = jo.getString(DataLogin.ALAMAT_SHARED_PREF);
                String telpon = jo.getString(DataLogin.TELPON_SHARED_PREF);
                String facebook = jo.getString(DataLogin.FACEBOOK_SHARED_PREF);
                String twitter = jo.getString(DataLogin.TWITTER_SHARED_PREF);
                String email = jo.getString(DataLogin.EMAIL_SHARED_PREF);
                String status = jo.getString(DataLogin.STATUS_SHARED_PREF);

                HashMap<String,String> userData = new HashMap<>();
                userData.put(DataLogin.NAME_SHARED_PREF,name);
                userData.put(DataLogin.PASSWORD_SHARED_PREF,password);
                userData.put(DataLogin.CT_SHARED_PREF,createdTime);
                userData.put(DataLogin.ALAMAT_SHARED_PREF,alamat);
                userData.put(DataLogin.TELPON_SHARED_PREF,telpon);
                userData.put(DataLogin.FACEBOOK_SHARED_PREF,facebook);
                userData.put(DataLogin.TWITTER_SHARED_PREF,twitter);
                userData.put(DataLogin.EMAIL_SHARED_PREF,email);
                userData.put(DataLogin.STATUS_SHARED_PREF,status);

                dataUser.add(userData);

                SharedPreferences sharedPreferences = Register.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);

                editor.putString(DataLogin.PASSWORD_SHARED_PREF, password);
                editor.putString(DataLogin.NAME_SHARED_PREF, name);
                editor.putString(DataLogin.CT_SHARED_PREF, createdTime);

                editor.putString(DataLogin.ALAMAT_SHARED_PREF, alamat);
                editor.putString(DataLogin.TELPON_SHARED_PREF, telpon);
                editor.putString(DataLogin.FACEBOOK_SHARED_PREF, facebook);
                editor.putString(DataLogin.TWITTER_SHARED_PREF, twitter);
                editor.putString(DataLogin.EMAIL_SHARED_PREF, email);
                editor.putString(DataLogin.STATUS_SHARED_PREF,status);
                editor.putString(DataLogin.USERNAME_SHARED_PREF, username);
                //Simpan Nilai ke Variabel editor
                editor.commit();
            }
            //Starting Class yang dipanggil
            Intent intent = new Intent(Register.this, Home.class);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Register.this,Login.class);
        startActivity(intent);

        Register.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

}
