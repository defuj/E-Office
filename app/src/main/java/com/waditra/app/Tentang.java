package com.waditra.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tentang extends AppCompatActivity {
    private Button sdm,tentang,visi;
    private TextView judul;
    private LinearLayout a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        sdm = (Button)findViewById(R.id.sdm);
        tentang = (Button)findViewById(R.id.tentang);
        visi = (Button)findViewById(R.id.visi);

        judul = (TextView)findViewById(R.id.BagianJudul);

        a = (LinearLayout)findViewById(R.id.ContentTentang);
        b = (LinearLayout)findViewById(R.id.ContentVisi);
        c = (LinearLayout)findViewById(R.id.ContentSDM);
    }

    public void onBack3(View view){
        finish();
    }

    public void visi(View view){
        a.setVisibility(View.GONE);
        c.setVisibility(View.GONE);

        b.setVisibility(View.VISIBLE);

        judul.setText("Visi & Misi");
        visi.setBackgroundColor(Color.parseColor("#FFDE430B"));

        tentang.setBackgroundColor(Color.parseColor("#f66631"));
        sdm.setBackgroundColor(Color.parseColor("#f66631"));
    }
    public void tentang(View view){
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);

        a.setVisibility(View.VISIBLE);

        judul.setText("Tentang Kami");
        tentang.setBackgroundColor(Color.parseColor("#FFDE430B"));

        visi.setBackgroundColor(Color.parseColor("#f66631"));
        sdm.setBackgroundColor(Color.parseColor("#f66631"));
    }
    public void sdm(View view){
        a.setVisibility(View.GONE);
        b.setVisibility(View.GONE);

        c.setVisibility(View.VISIBLE);

        judul.setText("Keunggulan");
        sdm.setBackgroundColor(Color.parseColor("#FFDE430B"));

        visi.setBackgroundColor(Color.parseColor("#f66631"));
        tentang.setBackgroundColor(Color.parseColor("#f66631"));
    }
}
