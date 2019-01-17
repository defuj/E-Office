package com.waditra.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class Notifikasi extends AppCompatActivity {

    private String judul;
    private String isi,waktuu;

    private TextView JUDUL,ISI,waktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        JUDUL = (TextView)findViewById(R.id.txtJudulNotif);
        ISI = (TextView)findViewById(R.id.txtIsiNotif);
        waktu = (TextView)findViewById(R.id.txtwaktu);

        tampilkanData();
    }

    private void tampilkanData() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dateString = sdf.format(date);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            judul = extras.getString("judul");
            isi = extras.getString("isi");
            waktuu = extras.getString("waktu");

            JUDUL.setText(judul);
            ISI.setText(isi);
            waktu.setText(waktuu);
        }
    }

    public void onBack2(View view){
        finish();
    }
}
