package com.waditra.app;

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
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.twotoasters.jazzylistview.JazzyListView;
import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListProduk extends AppCompatActivity {
    private JazzyListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listproduk);
        getJSON();
        // Inisialiasi ListView
        listView = (JazzyListView)findViewById(R.id.listProduk);
        list = (JazzyListView)findViewById(R.id.listProduk);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String IDP = ((TextView)view.findViewById(R.id.txtIDSPJ)).getText().toString();

                Intent intent = new Intent(ListProduk.this, DetailProduk.class);
                intent.putExtra("id_produk", IDP);
                startActivity(intent); */
            }
        });

        cari = (EditText)findViewById(R.id.cariProduk);
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
        //cari = (EditText)findViewById(R.id.cariProduk);
        final String subjek = cari.getText().toString().trim();
        loading = (ProgressBar)findViewById(R.id.progressBar3);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?cariProduk="+subjek);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void TampilData(){
        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> activity_listproduk = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String idP = jo.getString(DataLogin.TAG_ID_PRODUK);
                String namaP = jo.getString(DataLogin.TAG_NAMA_PRODUK);
                String jenisP = jo.getString(DataLogin.TAG_JENIS_PRODUK);
                String foto = jo.getString(DataLogin.TAG_URL_FOTO_PRODUK);

                HashMap<String,String> produk = new HashMap<>();
                produk.put(DataLogin.TAG_ID_PRODUK,idP);
                produk.put(DataLogin.TAG_NAMA_PRODUK,namaP);
                produk.put(DataLogin.TAG_JENIS_PRODUK,jenisP);
                produk.put(DataLogin.TAG_URL_FOTO_PRODUK,foto);
                activity_listproduk.add(produk);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        /*ListAdapter adapter = new SimpleAdapter(
                ListProduk.this, activity_listproduk, R.layout.list_dataproduk,
                new String[]{DataLogin.TAG_NAMA_PRODUK,DataLogin.TAG_JENIS_PRODUK,DataLogin.TAG_ID_PRODUK,DataLogin.TAG_URL_FOTO_PRODUK},
                new int[]{R.id.txtNamaProduk,R.id.txtJenisProduk,R.id.txtIDproduk,R.id.webView});*/
        SimpleAdapter adapter = new SimpleAdapter(
                ListProduk.this, activity_listproduk, R.layout.list_dataproduk,
                new String[]{DataLogin.TAG_NAMA_PRODUK,DataLogin.TAG_JENIS_PRODUK,DataLogin.TAG_ID_PRODUK},
                new int[]{R.id.txtNamaProduk,R.id.txtJenisProduk,R.id.txtIDproduk});
        /*adapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view.getId() == R.id.webView){
                    WebView web = (WebView)view;
                    String url = (String)data;
                    web.loadUrl("http://csmd-shop.16mb.com/webservice/android/foto.php?test="+url);
                }
                return false;
            }
        }); */
        //ListAdapter adapter = adapter1;
        // Tampilkan dalam bentuk ListView
        listView.setAdapter(adapter);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.listitem_up),0.2f);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }

    // Methode ambil data JSON yang kita definisikan dalam bentuk AsyncTask
    public void getJSON(){
        loading = (ProgressBar)findViewById(R.id.progressBar3);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?listproduk=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void onBack(View view){
        finish();
    }
}
