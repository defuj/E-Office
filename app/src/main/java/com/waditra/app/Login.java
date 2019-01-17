package com.waditra.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.waditra.app.Alat.DatabaseHelper;
import com.waditra.app.Alat.RequestHandler;
import com.waditra.app.HomeDrawer.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    DatabaseHelper MyDb;

    private ProgressDialog pDialog;
    private Button btnRegister;
    private EditText inputUsername, inputPassword;
    private boolean loggedIn = false;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        /*Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/
        setContentView(R.layout.activity_login);
        //MyDb = new DatabaseHelper(this);

        inputUsername = (EditText)findViewById(R.id.editTextUsername);
        inputPassword = (EditText)findViewById(R.id.editTextPassword);

        btnRegister = (Button)findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

                Login.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn=sharedPreferences.getBoolean(DataLogin.LOGGEDIN_SHARED_PREF, false);

        if (loggedIn){
            // Class Yang akan muncul jika Login Sukses
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
        }
    }

    public void onLogin(View view){
        if(inputUsername.getText().toString().length() > 0){
            if(inputPassword.getText().toString().length() > 0){
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    LoginMasuk();
                }else{
                    Intent i = new Intent(Login.this, Koneksi.class);
                    startActivity(i);
                }
            }else{
                inputPassword.setError("Belum diisi");
                inputPassword.setFocusable(true);
            }
        }else{
            inputUsername.setError("Belum diisi");
            inputUsername.setFocusable(true);
        }
    }

    private void LoginMasuk() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Mohon tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        // Ubah ketipe String
        final String username=inputUsername.getText().toString().trim();
        final String password=inputPassword.getText().toString().trim();

        //Buatkan Request Dalam bentuk String
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataLogin.URL+"?login=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Jika Respon server sukses
                        if(response.equalsIgnoreCase(DataLogin.LOGIN_SUCCESS)){
                            //Buatkan sebuah shared preference
                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Buatkan Sebuah variabel Editor Untuk penyimpanan Nilai shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Tambahkan Nilai ke Editor
                            editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(DataLogin.USERNAME_SHARED_PREF, username);

                            //Simpan Nilai ke Variabel editor
                            editor.commit();

                            setData();
                        }else{
                            //Jika Respon Dari Server tidak Sukses
                            //Tampilkan Pesan Errorrr dengan Toast,,
                            pDialog.hide();
                            Toast.makeText(Login.this, "Periksa kembali data", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Tambahkan Parameter username dan password untuk Request
                params.put(DataLogin.KEY_USERNAME, username);
                params.put(DataLogin.KEY_PASSWORD, password);

                //Kembalikan Nilai parameter
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setData() {
        getJSON();
    }
    public void TampilData(){
        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
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
                String foto = jo.getString(DataLogin.FOTO_ADDRESS);

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
                userData.put(DataLogin.FOTO_ADDRESS,foto);

                dataUser.add(userData);

                SharedPreferences sharedPreferences = Login.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
                editor.putString(DataLogin.FOTO_ADDRESS,foto);
                //Simpan Nilai ke Variabel editor
                editor.commit();
            }
            //Starting Class yang dipanggil
            prosesDownloadPhoto();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void prosesDownloadPhoto() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String foto_user = sharedPreferences.getString(DataLogin.FOTO_ADDRESS, null);
        if(foto_user != null){
            new DownloadImage(foto_user).execute();
        }else{
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
        }
    }

    private class DownloadImage extends AsyncTask<Void,Void,Bitmap>{
        String name;

        public DownloadImage(String name){
            this.name = name;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url = DataLogin.URL_PHOTOS_USERS+name;

            try{
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000 * 30);

                return BitmapFactory.decodeStream((InputStream)connection.getContent(),null,null);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                //userphoto.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"judul",null);
                Uri.parse(path);
                String img = Uri.parse(path).toString();

                SharedPreferences sharedPreferences = Login.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
                editor.putString(DataLogin.FOTO_USER, img);
                editor.commit();

                Intent i = new Intent(Login.this, Home.class);
                startActivity(i);
            }else{
                Intent i = new Intent(Login.this, Home.class);
                startActivity(i);
            }
        }
    }

    // Methode ambil data JSON yang kita definisikan dalam bentuk AsyncTask
    public void getJSON(){
        final String userNAME=inputUsername.getText().toString().trim();
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
                TampilData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?data=1&username="+userNAME);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Ingin keluar dari aplikasi?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Balik dan tampilkan ke halaman Utama aplikasi jika logout berhasil
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
// Pilihan jika NO
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        // Tampilkan alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
