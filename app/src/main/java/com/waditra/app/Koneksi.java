package com.waditra.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Koneksi extends AppCompatActivity {
    private ProgressBar cek;
    private TextView txtCek;
    private ImageButton imgCek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_koneksi);
        cekKoneksiJaringan();
    }

    public void toCek(View view){
        cekKoneksiJaringan();
    }

    private void cekKoneksiJaringan() {
        cek = (ProgressBar)findViewById(R.id.progresCeking);
        txtCek = (TextView)findViewById(R.id.txtCek);
        imgCek = (ImageButton)findViewById(R.id.btnCek);

        imgCek.setVisibility(View.GONE);
        cek.setVisibility(View.VISIBLE);
        txtCek.setText("Mengkoneksikan");

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    finish();
                }else{
                    noInternet();
                }
            }
        }.start();
    }

    private void noInternet() {
        cek = (ProgressBar)findViewById(R.id.progresCeking);
        txtCek = (TextView)findViewById(R.id.txtCek);
        imgCek = (ImageButton)findViewById(R.id.btnCek);

        imgCek.setVisibility(View.VISIBLE);
        cek.setVisibility(View.GONE);
        txtCek.setText("Tidak ada koneksi");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Ingin keluar dari aplikasi?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Balik dan tampilkan ke halaman Utama aplikasi jika logout berhasil
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
// Pilihan jika NO
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        // Tampilkan alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
