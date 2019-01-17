package com.waditra.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.waditra.app.HomeDrawer.Home;

public class Profil extends AppCompatActivity {
    private TextView txtUser, txtEmail;
    private TextView Alamat, Telpon,Email;
    private SimpleDraweeView FOTO_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_profil);
        FOTO_USER = (SimpleDraweeView)findViewById(R.id.fotoProfile);

        Peoses();
    }

    public void onBack2(View view){
        Intent i = new Intent(Profil.this, Home.class);
        startActivity(i);
    }

    public void gantiPhoto(View view){
        //Toast.makeText(Profil.this, "Modul sedang diperbaiki", Toast.LENGTH_LONG).show();
        Intent i = new Intent(Profil.this, AddPhotos.class);
        startActivity(i);
        Profil.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    private void Peoses() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String foto_user = sharedPreferences.getString(DataLogin.FOTO_USER, null);

        if(foto_user != null){
            Uri fotoUser = Uri.parse(foto_user);
            FOTO_USER.setImageURI(fotoUser);
        }

        peoses2();
    }

    public void peoses2() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String aran = sharedPreferences.getString(DataLogin.USERNAME_SHARED_PREF,"Not Available");
        String pengguna = sharedPreferences.getString(DataLogin.NAME_SHARED_PREF,"Not Available");
        String address = sharedPreferences.getString(DataLogin.ALAMAT_SHARED_PREF,"Not Available");
        String email = sharedPreferences.getString(DataLogin.EMAIL_SHARED_PREF,"Not Available");

        Button status = (Button)findViewById(R.id.status);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            status.setText("Online");
        }else{
            status.setText("Offline");
        }

        Alamat = (TextView)findViewById(R.id.txtAlamat);
        Email = (TextView)findViewById(R.id.txtEmail) ;

        txtUser = (TextView)findViewById(R.id.txtNamaProfil);
        txtEmail = (TextView)findViewById(R.id.txtEmailProfil);

        Alamat.setText(address);

        txtEmail.setText(aran+"@domain.com");
        txtUser.setText(pengguna);
        Email.setText(email);

        proses();
    }

    private void proses() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString(DataLogin.TELPON_SHARED_PREF,"Not Available");
        Telpon = (TextView)findViewById(R.id.txtTelpon);
        Telpon.setText(phone);
    }

    public void onBack(View view){
        Intent i = new Intent(Profil.this, Home.class);
        startActivity(i);
    }

    public void editProfile(View view){
        Intent i = new Intent(Profil.this, EditProfile.class);
        startActivity(i);
    }
    public void goTwitter(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String tw = sharedPreferences.getString(DataLogin.TWITTER_SHARED_PREF,"Not Available");
        if(tw.equals("Not Available")){
            Toast.makeText(Profil.this, "Anda belum mengatur akun anda", Toast.LENGTH_LONG).show();
        }else{
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.twitter.com/"+tw));
            startActivity(i);
        }
    }
    public void goFacebook(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String fb = sharedPreferences.getString(DataLogin.FACEBOOK_SHARED_PREF,"Not Available");
        if(fb.equals("Not Available")){
            Toast.makeText(Profil.this, "Anda belum mengatur akun anda", Toast.LENGTH_LONG).show();
        }else{
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/"+fb));
            startActivity(i);
        }
    }
    public void goPlus(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(DataLogin.EMAIL_SHARED_PREF,"Not Available");
        if(email.equals("Not Available")){
            Toast.makeText(Profil.this, "Anda belum mengatur email anda", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Profil.this,email , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Profil.this, Home.class);
        startActivity(i);
    }
}
