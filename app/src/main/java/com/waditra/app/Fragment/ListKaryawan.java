package com.waditra.app.Fragment;

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
import com.waditra.app.DataLogin;
import com.waditra.app.HomeDrawer.Home;
import com.waditra.app.ProfilKaryawan;
import com.waditra.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by defuj on 12/08/2016.
 */
public class ListKaryawan extends AppCompatActivity {
    // Definisikan ListView
    private JazzyListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listkaryawan);

        getJSON();

        // Inisialiasi ListView
        listView = (JazzyListView)findViewById(R.id.listView);
        list = (JazzyListView)findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idd = ((TextView)view.findViewById(R.id.id)).getText().toString();

                Intent intent = new Intent(ListKaryawan.this, ProfilKaryawan.class);
                intent.putExtra("id", idd);
                startActivity(intent);
            }
        });

        cari = (EditText)findViewById(R.id.txtCariKaryawan);
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

    public void onBack4(View view){
        finish();
    }

    private void pencarian() {
        //cari = (EditText)findViewById(R.id.txtCariKaryawan);
        final String subjek = cari.getText().toString().trim();
        loading = (ProgressBar)findViewById(R.id.progresListKaryawan);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?user="+subjek);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void onBack(View view){
        Intent i = new Intent(ListKaryawan.this, Home.class);
        startActivity(i);
    }
    // Buat Methode untuk ambil data dari Server
    public void TampilData(){
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
                String id = jo.getString(DataLogin.TAG_ID);

                HashMap<String,String> karyawan = new HashMap<>();
                karyawan.put(DataLogin.TAG_JABATAN,jabatan);
                karyawan.put(DataLogin.TAG_NAMA,nama);
                karyawan.put(DataLogin.TAG_ID,id);
                listkaryawan.add(karyawan);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        ListAdapter adapter = new SimpleAdapter(
                ListKaryawan.this, listkaryawan, R.layout.listdata,
                new String[]{DataLogin.TAG_NAMA,DataLogin.TAG_JABATAN,DataLogin.TAG_ID},
                new int[]{R.id.name,R.id.jabatan,R.id.id});
        // Tampilkan dalam bentuk ListView
        listView.setAdapter(adapter);

        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.listitem_up),0.2f);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }

    // Methode ambil data JSON yang kita definisikan dalam bentuk AsyncTask
    public void getJSON(){
        loading = (ProgressBar)findViewById(R.id.progresListKaryawan);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?list=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
