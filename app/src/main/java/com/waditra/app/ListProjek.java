package com.waditra.app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ListProjek extends AppCompatActivity {
    private JazzyListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;
    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listprojek);
        getJSON();
        // Inisialiasi ListView
        listView = (JazzyListView)findViewById(R.id.listView4);
        list = (JazzyListView)findViewById(R.id.listView4);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void getJSON() {
        loading = (ProgressBar)findViewById(R.id.progressBar5);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?listprojek=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void TampilData() {
        // Data dalam bentuk Array kemudian akan kita ubah menjadi JSON Object
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> activity_listprojek = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String nama = jo.getString(DataLogin.TAG_NAMA_PROJEK);
                String jenis = jo.getString(DataLogin.TAG_JENIS_PROJEK);
                String waktu = jo.getString(DataLogin.TAG_WAKTU_PROJEK);
                String progress = jo.getString(DataLogin.TAG_PROGRESS_PROJEK);

                HashMap<String,String> projek = new HashMap<>();
                projek.put(DataLogin.TAG_NAMA_PROJEK,nama);
                projek.put(DataLogin.TAG_JENIS_PROJEK,jenis);
                projek.put(DataLogin.TAG_WAKTU_PROJEK,waktu);
                projek.put(DataLogin.TAG_PROGRESS_PROJEK,progress);
                activity_listprojek.add(projek);
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
                ListProjek.this, activity_listprojek, R.layout.list_dataprojek,
                new String[]{DataLogin.TAG_NAMA_PROJEK,DataLogin.TAG_WAKTU_PROJEK,DataLogin.TAG_JENIS_PROJEK,DataLogin.TAG_PROGRESS_PROJEK},
                new int[]{R.id.namaProjek,R.id.waktuProjek,R.id.jenisProjek,R.id.projekPersen});
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

    public void onBack(View view){
        finish();
    }
}
