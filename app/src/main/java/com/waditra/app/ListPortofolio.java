package com.waditra.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
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

public class ListPortofolio extends AppCompatActivity {
    // Definisikan ListView
    private JazzyListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listportofolio);
        getJSON();

        // Inisialiasi ListView
        listView = (JazzyListView)findViewById(R.id.listView3);
        list = (JazzyListView)findViewById(R.id.listView3);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String namaproyek = ((TextView)view.findViewById(R.id.namaProyek)).getText().toString();
                String namainstansi = ((TextView)view.findViewById(R.id.namaInstansi)).getText().toString();
                String tahun = ((TextView)view.findViewById(R.id.tahunProyek)).getText().toString();
                String nilai = ((TextView)view.findViewById(R.id.nilaiProyek)).getText().toString();
                String detail = ((TextView)view.findViewById(R.id.detailProyek)).getText().toString();

                Intent intent = new Intent(ListPortofolio.this, DetailPortofolio.class);
                intent.putExtra("namaproyek", namaproyek);
                intent.putExtra("namainstansi", namainstansi);
                intent.putExtra("tahun", tahun);
                intent.putExtra("nilai", nilai);
                intent.putExtra("detail", detail);
                startActivity(intent);
            }
        });
    }

    public void getJSON() {
        loading = (ProgressBar)findViewById(R.id.progressBar4);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?portofolio=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void TampilData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> listPortofolio = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String id = jo.getString(DataLogin.TAG_ID_PROYEK);
                String nama = jo.getString(DataLogin.TAG_NAMA_PROYEK);
                String tahun = jo.getString(DataLogin.TAG_TAHUN_PROYEK);
                String instansi = jo.getString(DataLogin.TAG_NAMA_INSTANSI);
                String nilai = jo.getString(DataLogin.TAG_NILAI_PROYEK);
                String detail = jo.getString(DataLogin.TAG_DETAIL_PROYEK);

                HashMap<String,String> portofolio = new HashMap<>();
                portofolio.put(DataLogin.TAG_ID_PROYEK,id);
                portofolio.put(DataLogin.TAG_NAMA_PROYEK,nama);
                portofolio.put(DataLogin.TAG_TAHUN_PROYEK,tahun);
                portofolio.put(DataLogin.TAG_NAMA_INSTANSI,instansi);
                portofolio.put(DataLogin.TAG_NILAI_PROYEK,nilai);
                portofolio.put(DataLogin.TAG_DETAIL_PRODUK,detail);
                listPortofolio.add(portofolio);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        ListAdapter adapter = new SimpleAdapter(
                ListPortofolio.this, listPortofolio, R.layout.listdata_portofolio,
                new String[]{DataLogin.TAG_NAMA_PROYEK,DataLogin.TAG_NAMA_INSTANSI,DataLogin.TAG_TAHUN_PROYEK,DataLogin.TAG_ID_PROYEK,DataLogin.TAG_NILAI_PROYEK,DataLogin.TAG_DETAIL_PROYEK},
                new int[]{R.id.namaProyek,R.id.namaInstansi,R.id.tahunProyek,R.id.idProyek,R.id.nilaiProyek,R.id.detailProyek});
        // Tampilkan dalam bentuk ListView
        listView.setAdapter(adapter);

        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.listitem_up),0.2f);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }

    public void onBack(View view){
        finish();
    }
}
