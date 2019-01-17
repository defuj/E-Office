package com.waditra.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailSPJ extends AppCompatActivity {
    private ScrollView DataDetailSPJ;
    private ProgressBar prosesiSPJ;
    private String ID;
    private String JSON_STRING;
    private TextView NAMASPJ, NAMA_KARYAWAN,TUJUANSPJ,WAKTUMULAISPJ,WAKTUSELESAISPJ,DESKRIPSISPJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        setContentView(R.layout.activity_detailspj);

        tampilkanDetailSPJ();
    }

    public void tampilkanDetailSPJ() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            ID = extras.getString("id_spj");

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
                    String s = rh.sendGetRequest(DataLogin.URL+"?detailSPJ="+ID);
                    return s;
                }
            }
            GetJSON gj = new GetJSON();
            gj.execute();
        }
    }

    public void TampilData() {
        NAMASPJ = (TextView)findViewById(R.id.txtNamaSPJ);
        NAMA_KARYAWAN = (TextView)findViewById(R.id.txtNamaPegawaiSPJ);
        TUJUANSPJ = (TextView)findViewById(R.id.txtTujuanSPJ);
        WAKTUMULAISPJ = (TextView)findViewById(R.id.txtWaktuMulaiSPJ);
        WAKTUSELESAISPJ = (TextView)findViewById(R.id.txtWaktuSelesaiSPJ);
        DESKRIPSISPJ = (TextView)findViewById(R.id.txtDeskripsiPekerjaan);

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
                String namaSPJ = jo.getString(DataLogin.TAG_NAMA_SPJ);
                String namKaryawanSPJ = jo.getString(DataLogin.TAG_NAMA_KARYAWAN);
                String tujuanSPJ = jo.getString(DataLogin.TAG_TUJUAN);
                String waktuMULAISPJ = jo.getString(DataLogin.TAG_TGL_MULAI);
                String waktuSELESAISPJ = jo.getString(DataLogin.TAG_TGL_SELESAI);
                String deskripsiPEKERJAAN = jo.getString(DataLogin.TAG_KEGIATAN_PEKERJAAN);

                HashMap<String,String> user = new HashMap<>();
                user.put(DataLogin.TAG_NAMA_SPJ,namaSPJ);
                user.put(DataLogin.TAG_NAMA_KARYAWAN,namKaryawanSPJ);
                user.put(DataLogin.TAG_TUJUAN,tujuanSPJ);
                user.put(DataLogin.TAG_TGL_MULAI,waktuMULAISPJ);
                user.put(DataLogin.TAG_TGL_SELESAI,waktuSELESAISPJ);
                user.put(DataLogin.TAG_KEGIATAN_PEKERJAAN,deskripsiPEKERJAAN);
                listkaryawan.add(user);

                NAMASPJ.setText(namaSPJ);
                NAMA_KARYAWAN.setText(namKaryawanSPJ);
                TUJUANSPJ.setText(tujuanSPJ);
                WAKTUMULAISPJ.setText(waktuMULAISPJ);
                WAKTUSELESAISPJ.setText(waktuSELESAISPJ);
                DESKRIPSISPJ.setText(deskripsiPEKERJAAN);

                DataDetailSPJ = (ScrollView)findViewById(R.id.DataDetailSPJ);
                prosesiSPJ = (ProgressBar)findViewById(R.id.prosesiSPJ);

                prosesiSPJ.setVisibility(View.GONE);
                DataDetailSPJ.setVisibility(View.VISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBack2(View view){
        finish();
    }
}
