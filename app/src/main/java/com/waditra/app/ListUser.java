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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListUser extends AppCompatActivity {
    private ListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listuser);

        getJSON();

        listView = (ListView)findViewById(R.id.listPengguna);
        list = (ListView)findViewById(R.id.listPengguna);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((TextView)view.findViewById(R.id.txtListUsername)).getText().toString();

                Intent intent = new Intent(ListUser.this, DetailUser.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        cari = (EditText)findViewById(R.id.cariUser);
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
        cari = (EditText)findViewById(R.id.cariUser);
        final String subjek = cari.getText().toString().trim();
        loading = (ProgressBar)findViewById(R.id.progressListUser);
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.setVisibility(View.GONE);
                super.onPostExecute(s);
                JSON_STRING = s;
                // Panggil method tampil data
                if(subjek.equals(null)){
                    getJSON();
                }else{
                    TampilData();
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?cariUSER="+subjek);
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
        ArrayList<HashMap<String,String>> activity_listuser = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String nama = jo.getString(DataLogin.TAG_NAMA_USER);
                String username = jo.getString(DataLogin.TAG_USERNAME_USER);
                String ct = jo.getString(DataLogin.TAG_CT_USER);

                HashMap<String,String> user = new HashMap<>();
                user.put(DataLogin.TAG_NAMA_USER,nama);
                user.put(DataLogin.TAG_USERNAME_USER,username);
                user.put(DataLogin.TAG_CT_USER,ct);
                activity_listuser.add(user);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        ListAdapter adapter = new SimpleAdapter(
                ListUser.this, activity_listuser, R.layout.listdata_user,
                new String[]{DataLogin.TAG_NAMA_USER,DataLogin.TAG_USERNAME_USER,DataLogin.TAG_CT_USER},
                new int[]{R.id.txtListNamaUser,R.id.txtListUsername,R.id.txtStartRegiter});
        // Tampilkan dalam bentuk ListView
        listView.setAdapter(adapter);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.listitem_up),0.5f);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }

    // Methode ambil data JSON yang kita definisikan dalam bentuk AsyncTask
    public void getJSON(){
        loading = (ProgressBar)findViewById(R.id.progressListUser);
        class GetJSON extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.setVisibility(View.GONE);
                super.onPostExecute(s);
                JSON_STRING = s;
                // Panggil method tampil data
                TampilData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?listUser=1");
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
