package com.waditra.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfilKaryawan extends AppCompatActivity {
    private ScrollView ContentDetailKaryawan;
    private String ID;
    private EditText txtNama;
    private String JSON_STRING;
    private LinearLayout progress,content;
    private TextView NAMA, KEDUDUKAN, MASUK, TTL;
    private WebView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilkaryawan);

        tampilData1();
    }

    public void tampilData1(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            ID = extras.getString("id");
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
                    String s = rh.sendGetRequest(DataLogin.URL+"?id="+ID);
                    return s;
                }
            }
            GetJSON gj = new GetJSON();
            gj.execute();
        }
    }

    public void TampilData(){
        NAMA = (TextView) findViewById(R.id.txtNamaDetalKaryawan);
        KEDUDUKAN = (TextView) findViewById(R.id.txtKedudukanDetail);
        TTL = (TextView) findViewById(R.id.txtTTLDetail);
        MASUK = (TextView) findViewById(R.id.txtWaktuMasuk);

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
                String jabatan = jo.getString(DataLogin.TAG_JABATAN);
                String nama = jo.getString(DataLogin.TAG_NAMA);
                String tempatLahir = jo.getString(DataLogin.TAG_TEMPAT_LAHIR);
                String tanggalLahir = jo.getString(DataLogin.TAG_TANGGAL_LAHIR);
                String CT = jo.getString(DataLogin.TAG_CREATED_TIME);

                HashMap<String,String> karyawan = new HashMap<>();
                karyawan.put(DataLogin.TAG_JABATAN,jabatan);
                karyawan.put(DataLogin.TAG_NAMA,nama);
                karyawan.put(DataLogin.TAG_TEMPAT_LAHIR, tempatLahir);
                karyawan.put(DataLogin.TAG_TANGGAL_LAHIR, tanggalLahir);
                karyawan.put(DataLogin.TAG_CREATED_TIME, CT);
                listkaryawan.add(karyawan);

                NAMA.setText(nama);
                KEDUDUKAN.setText(jabatan);
                TTL.setText(tempatLahir+", "+tanggalLahir);
                MASUK.setText(CT);

                Bundle extras = getIntent().getExtras();

                foto = (WebView)findViewById(R.id.fotoProfilKaryawan);
                foto.loadUrl("http://csmd-shop.16mb.com/webservice/android/foto.php?pegawai="+extras.getString("id"));

                progress = (LinearLayout)findViewById(R.id.untukProgress);
                ContentDetailKaryawan = (ScrollView)findViewById(R.id.ContentDetailKaryawan);
                progress.setVisibility(View.GONE);
                ContentDetailKaryawan.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onBack2(View view){
        finish();
    }
}
