package com.waditra.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailUser extends AppCompatActivity {
    private ScrollView DataDetailUser;
    private ProgressBar prosesi;
    private String STATUS;
    private String JSON_STRING;
    private TextView NAMA, USERNAME, status, CT, ALAMAT, TELPON, FACEBOOK, TWITTER,EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailuser);

        tampilkanDataUser();
    }

    public void tampilkanDataUser() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            STATUS = extras.getString("username");

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
                    String s = rh.sendGetRequest(DataLogin.URL+"?USERDETAIL="+STATUS);
                    return s;
                }
            }
            GetJSON gj = new GetJSON();
            gj.execute();
        }
    }

    public void TampilData(){
        NAMA = (TextView)findViewById(R.id.txtDetailNama);
        USERNAME = (TextView)findViewById(R.id.txtDetailUsername);
        status = (TextView)findViewById(R.id.txtDetailStatus);
        CT = (TextView)findViewById(R.id.txtDetailCT);
        ALAMAT = (TextView)findViewById(R.id.txtDetailAlamat);
        TELPON = (TextView)findViewById(R.id.txtDetailTelpon);
        FACEBOOK = (TextView)findViewById(R.id.txtDetailFacebook);
        TWITTER = (TextView)findViewById(R.id.txtDetailFacebook);
        EMAIL = (TextView)findViewById(R.id.txtDetailEmail);

        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> listkaryawan = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String nama = jo.getString(DataLogin.TAG_NAMA_USER);
                String username = jo.getString(DataLogin.TAG_USERNAME_USER);
                String Status = jo.getString(DataLogin.TAG_STATUS_USER);
                String ct = jo.getString(DataLogin.TAG_CT_USER);
                String alamat = jo.getString(DataLogin.TAG_ALAMAT_USER);
                String telpon = jo.getString(DataLogin.TAG_TELPON_USER);
                String facebook = jo.getString(DataLogin.TAG_FACEBOOK_USER);
                String twitter = jo.getString(DataLogin.TAG_TWITTER_USER);
                String email = jo.getString(DataLogin.TAG_EMAIL_USER);

                HashMap<String,String> user = new HashMap<>();
                user.put(DataLogin.TAG_NAMA_USER,nama);
                user.put(DataLogin.TAG_USERNAME_USER,username);
                user.put(DataLogin.TAG_STATUS_USER,Status);
                user.put(DataLogin.TAG_CT_USER,ct);
                user.put(DataLogin.TAG_ALAMAT_USER,alamat);
                user.put(DataLogin.TAG_TELPON_USER,telpon);
                user.put(DataLogin.TAG_FACEBOOK_USER,facebook);
                user.put(DataLogin.TAG_TWITTER_USER,twitter);
                user.put(DataLogin.TAG_EMAIL_USER,email);
                listkaryawan.add(user);

                NAMA.setText(nama);
                USERNAME.setText(username);
                if(Status.equals("1")){
                    status.setText("Administratos");
                }else if(Status.equals("2")){
                    status.setText("Karyawan / Pegawai");
                }else{
                    status.setText("Pengguna umum");
                }

                CT.setText(ct);
                ALAMAT.setText(alamat);
                TELPON.setText(telpon);
                FACEBOOK.setText(facebook);
                TWITTER.setText(twitter);
                EMAIL.setText(email);

                DataDetailUser = (ScrollView)findViewById(R.id.viewDetailUser);
                prosesi = (ProgressBar)findViewById(R.id.progressDetailUser);

                prosesi.setVisibility(View.GONE);
                DataDetailUser.setVisibility(View.VISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Ingin menghapus pengguna ini?");
        alertDialogBuilder.setPositiveButton("Lanjutkan",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        prosesi.setVisibility(View.VISIBLE);
                        DataDetailUser.setVisibility(View.GONE);
                        menghapusUser();
                    }
                });
// Pilihan jika NO
        alertDialogBuilder.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        // Tampilkan alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void menghapusUser() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            STATUS = extras.getString("username");

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
                    HasilHapusUser();
                }

                @Override
                protected String doInBackground(Void... params) {
                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendGetRequest(DataLogin.URL+"?DeleteUser="+STATUS);
                    return s;
                }
            }
            GetJSON gj = new GetJSON();
            gj.execute();
        }
    }

    public void HasilHapusUser() {
        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> listkaryawan = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String hasil = jo.getString(DataLogin.TAG_HASIL_HAPUS);

                HashMap<String,String> user = new HashMap<>();
                user.put(DataLogin.TAG_HASIL_HAPUS,hasil);
                listkaryawan.add(user);

                if(hasil.equals("berhasil")){
                    prosesi.setVisibility(View.GONE);
                    Toast.makeText(DetailUser.this, "Pengguna berhasil dihapus", Toast.LENGTH_LONG).show();
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Intent i = new Intent(DetailUser.this, ListUser.class);
                            startActivity(i);
                        }
                    }.start();
                }else{
                    prosesi.setVisibility(View.GONE);
                    DataDetailUser.setVisibility(View.VISIBLE);
                    Toast.makeText(DetailUser.this, "Terjadi kesalahan saat menghapus", Toast.LENGTH_LONG).show();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBack2(View view){
        finish();
    }
}
