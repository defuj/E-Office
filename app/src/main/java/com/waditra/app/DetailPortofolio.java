package com.waditra.app;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailPortofolio extends AppCompatActivity {
    private ProgressBar loading;
    private ScrollView sc;
    private String nama,instansi,tahun,nilai, detail;
    private TextView TXTnama,TXTinstansi,TXTtahun,TXTnilai,TXTdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailportofolio);

        TXTnama = (TextView)findViewById(R.id.RinciNamaProyek);
        TXTinstansi = (TextView)findViewById(R.id.RinciInstansiProyek);
        TXTtahun = (TextView)findViewById(R.id.RinciTahunProyek);
        TXTnilai = (TextView)findViewById(R.id.RinciNilaiProyek);
        TXTdetail = (TextView)findViewById(R.id.RinciDetailProyek);

        loading = (ProgressBar)findViewById(R.id.progressBar7);
        sc = (ScrollView)findViewById(R.id.scrollView5);

        proses();
    }

    public void proses() {
        Bundle extras = getIntent().getExtras();
        nama = extras.getString("namaproyek");
        instansi = extras.getString("namainstansi");
        tahun = extras.getString("tahun");
        nilai = extras.getString("nilai");
        detail = extras.getString("detail");

        TXTnama.setText(nama);
        TXTinstansi.setText(instansi);
        TXTtahun.setText(tahun);
        TXTnilai.setText(nilai);
        TXTdetail.setText(detail);

        proses2();
    }

    public void proses2() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                loading.setVisibility(View.GONE);
                sc.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void onBack2(View view){
        finish();
    }
}
