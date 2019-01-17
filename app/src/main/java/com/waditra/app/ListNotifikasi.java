package com.waditra.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.twotoasters.jazzylistview.JazzyListView;
import com.waditra.app.Alat.BounceInterpolator;
import com.waditra.app.Alat.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListNotifikasi extends AppCompatActivity {
    // Definisikan ListView
    private JazzyListView listView, list;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listnotifikasi);

        getJSON();

        // Inisialiasi ListView
        listView = (JazzyListView)findViewById(R.id.listNotifikasi);
        list = (JazzyListView)findViewById(R.id.listNotifikasi);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String isi = ((TextView)view.findViewById(R.id.txtNotifIsi)).getText().toString();
                String judul = ((TextView)view.findViewById(R.id.txtNotifJudul)).getText().toString();
                String waktu = ((TextView)view.findViewById(R.id.txtNotifWaktu)).getText().toString();

                Intent intent = new Intent(ListNotifikasi.this, Notifikasi.class);
                intent.putExtra("judul", judul);
                intent.putExtra("isi", isi);
                intent.putExtra("waktu", waktu);
                startActivity(intent);
            }
        });
    }

    public void getJSON() {
        loading = (ProgressBar)findViewById(R.id.progressBar2);
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
                String s = rh.sendGetRequest(DataLogin.URL+"?notifikasi=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void TampilData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> listNotikasi = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String judul = jo.getString(DataLogin.TAG_JUDUL_NOTIF);
                String isi = jo.getString(DataLogin.TAG_ISI_NOTIF);
                String waktu = jo.getString(DataLogin.TAG_WAKTU_NOTIF);

                HashMap<String,String> notifikasi = new HashMap<>();
                notifikasi.put(DataLogin.TAG_JUDUL_NOTIF,judul);
                notifikasi.put(DataLogin.TAG_ISI_NOTIF,isi);
                notifikasi.put(DataLogin.TAG_WAKTU_NOTIF,waktu);
                listNotikasi.add(notifikasi);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tampilkan datanya dalam Layout Lihat Data
        ListAdapter adapter = new SimpleAdapter(
                ListNotifikasi.this, listNotikasi, R.layout.list_datanotifikasi,
                new String[]{DataLogin.TAG_JUDUL_NOTIF,DataLogin.TAG_ISI_NOTIF,DataLogin.TAG_WAKTU_NOTIF},
                new int[]{R.id.txtNotifJudul,R.id.txtNotifIsi,R.id.txtNotifWaktu});
        // Tampilkan dalam bentuk ListView
        listView.setAdapter(adapter);

        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.listitem_up),0.2f);
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();
    }

    public void onBack4(View view){
        finish();
    }

    public void onDelete(View view){
        ImageButton btn = (ImageButton) findViewById(R.id.imageButton16);
        final Animation myAdmin = AnimationUtils.loadAnimation(ListNotifikasi.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        btn.startAnimation(myAdmin);

        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListNotifikasi.this);
                alertDialogBuilder.setTitle("Notifikasi");
                //alertDialogBuilder.setMessage("Terimakasih "+ pengguna +" anda telah menggunakan aplikasi kami.");// Pilihan jika NO
                alertDialogBuilder.setMessage("Belum ada pemberitahuan apapun.");
                alertDialogBuilder.setNegativeButton("Tutup",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                // Tampilkan alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }.start();
    }
}
