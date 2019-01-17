package com.waditra.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.twotoasters.jazzylistview.JazzyListView;
import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListSPJ extends AppCompatActivity {
    private JazzyListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listspj);

        getJSON();
        // Inisialiasi ListView
        listView = (JazzyListView)findViewById(R.id.listView2);
        list = (JazzyListView)findViewById(R.id.listView2);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String IDSPJ = ((TextView)view.findViewById(R.id.txtIDSPJ)).getText().toString();

                Intent intent = new Intent(ListSPJ.this, DetailSPJ.class);
                intent.putExtra("id_spj", IDSPJ);
                startActivity(intent);
            }
        });

        cari = (EditText)findViewById(R.id.txtCariSpj);
        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pencarian();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void pencarian(){
        //cari = (EditText)findViewById(R.id.txtCariSpj);
        final String subjek = cari.getText().toString().trim();
        loading = (ProgressBar)findViewById(R.id.progressListSpj);
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.setVisibility(View.GONE);
                JSON_STRING = s;
                if(subjek.equals(null)){
                    getJSON();
                }else{
                    TampilData();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?cariSPJ="+subjek);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    // Buat Methode untuk ambil data dari Server
    public void TampilData(){
        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> activity_listspj = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String idSPJ = jo.getString(DataLogin.TAG_ID_SPJ);
                String namaSPJ = jo.getString(DataLogin.TAG_NAMA_SPJ);
                String tujuan = jo.getString(DataLogin.TAG_TUJUAN);
                String tgl_mulai = jo.getString(DataLogin.TAG_TGL_MULAI);
                String tgl_selesai = jo.getString(DataLogin.TAG_TGL_SELESAI);
                String nama_karyawan = jo.getString(DataLogin.TAG_NAMA_KARYAWAN);

                HashMap<String,String> spj = new HashMap<>();
                spj.put(DataLogin.TAG_ID_SPJ,idSPJ);
                spj.put(DataLogin.TAG_NAMA_SPJ,namaSPJ);
                spj.put(DataLogin.TAG_TUJUAN,tujuan);
                spj.put(DataLogin.TAG_TGL_MULAI,tgl_mulai);
                spj.put(DataLogin.TAG_TGL_SELESAI,tgl_selesai);
                spj.put(DataLogin.TAG_NAMA_KARYAWAN,nama_karyawan);
                activity_listspj.add(spj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        ListAdapter adapter = new SimpleAdapter(
                ListSPJ.this, activity_listspj, R.layout.listdata_spj,
                new String[]{DataLogin.TAG_NAMA_SPJ,DataLogin.TAG_TUJUAN,DataLogin.TAG_ID_SPJ,DataLogin.TAG_TGL_MULAI,DataLogin.TAG_TGL_SELESAI,DataLogin.TAG_NAMA_KARYAWAN},
                new int[]{R.id.txtNamaSpj,R.id.txtTujuanSpj,R.id.txtIDSPJ,R.id.txtTglMulai,R.id.txtTglSelesai,R.id.txtNamaKaryawan});
        // Tampilkan dalam bentuk ListView
        listView.setAdapter(adapter);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.listitem_up),0.2f);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }

    // Methode ambil data JSON yang kita definisikan dalam bentuk AsyncTask
    public void getJSON(){
        loading = (ProgressBar)findViewById(R.id.progressListSpj);
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.setVisibility(View.GONE);
                JSON_STRING = s;
                // Panggil method tampil data
                TampilData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?spj=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void onBack4(View view){
        finish();
    }
}
