package com.waditra.app.HomeDrawer;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.waditra.app.Alat.BounceInterpolator;
import com.waditra.app.Alat.DatabaseHelper;
import com.waditra.app.Alat.RequestHandler;
import com.waditra.app.DataLogin;
import com.waditra.app.Fragment.Beranda;
import com.waditra.app.Fragment.ListKaryawan;
import com.waditra.app.ListNotifikasi;
import com.waditra.app.ListPortofolio;
import com.waditra.app.ListProduk;
import com.waditra.app.ListProjek;
import com.waditra.app.ListSPJ;
import com.waditra.app.ListUser;
import com.waditra.app.Login;
import com.waditra.app.MySingleton;
import com.waditra.app.Profil;
import com.waditra.app.R;
import com.waditra.app.Tentang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper MyDb;
    Fragment fragment;
    private TextView txtUser, txtEmail, selamatDatang;
    private ListView listView;
    // Variabel untuk format String JSON
    private String JSON_STRING;
    private ProgressDialog loading;
    private ViewFlipper flipper;

    private SimpleDraweeView byPhoto;

    private LinearLayout konek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        //navigationView.setItemBackground(null);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));

        fragment = new Beranda();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tampilFragment, fragment);
        ft.commit();

        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Peoses();
            }
        }.start();
    }

    public void pergiKeProfile(View view){
        Intent i = new Intent(Home.this, Profil.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void goProfilWithAllText(View view){
        Intent i = new Intent(Home.this, Profil.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void goProfilWithAll(View view) {
        Intent i = new Intent(Home.this, Profil.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void lihatUser(View view){
        Intent i = new Intent(Home.this, ListUser.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    private void Peoses() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String aran = sharedPreferences.getString(DataLogin.USERNAME_SHARED_PREF, "Not Available");
        String pengguna = sharedPreferences.getString(DataLogin.NAME_SHARED_PREF, "Not Available");
        String userstatus = sharedPreferences.getString(DataLogin.STATUS_SHARED_PREF,"Not Available");
        String foto_user = sharedPreferences.getString(DataLogin.FOTO_USER, null);

        if(foto_user != null){
            Uri fotoUser = Uri.parse(foto_user);
            byPhoto = (SimpleDraweeView)findViewById(R.id.imgFotoProfil);
            byPhoto.setImageURI(fotoUser);
        }

        setTitle("Hai, "+pengguna);

        /*if(userstatus.equals("1")){
            ImageView SEEuser = (ImageView)findViewById(R.id.lihatUser);
            SEEuser.setVisibility(View.VISIBLE);
        }else if(userstatus.equals("3")){
            fragment = new Beranda_user();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.tampilFragment, fragment);
            ft.commit();
        } */

        //selamatDatang = (TextView) findViewById(R.id.selamatDatang);
        //selamatDatang.setText("Selamat Datang " + aran);
        txtUser = (TextView) findViewById(R.id.txtNamaUser);
        txtEmail = (TextView) findViewById(R.id.txtEmailUser);
        txtEmail.setText(aran + "@domain.com");
        txtUser.setText(pengguna);
        
        token();
    }

    public void token() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DataLogin.URL+"?KodeToken=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Home.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("fcm_token",token);

                return params;
            }
        };
        MySingleton.getmIntance(Home.this).addToRequestque(stringRequest);

        cekKonektivitas();
    }

    public void cekKonektivitas() {
        konek = (LinearLayout)findViewById(R.id.konektivitas);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    konek.setVisibility(View.GONE);
                }else{
                    konek.setVisibility(View.VISIBLE);
                }
            }
        }.start();

        buatBar();
    }

    public void getJSON() {
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                // Panggil method tampil data
                ShowNotif();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(DataLogin.URL+"?jumlahnotif=1");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void ShowNotif() {
        TextView txtJumlahNotif = (TextView)findViewById(R.id.txtJumlahNotif);
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> jumlahnotif = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(DataLogin.TAG_JSON_ARRAY);
            // FOR untuk ambil data
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                // TAG_ID dan TAG_NAME adalah variabel yang ada di Class Config.java,
                String jumlah = jo.getString(DataLogin.TAG_JUMLAH_NOTIF);

                HashMap<String,String> notif = new HashMap<>();
                notif.put(DataLogin.TAG_JUMLAH_NOTIF,jumlah);
                jumlahnotif.add(notif);

                txtJumlahNotif.setText(jumlah+" Notif");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buatBar() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(DataLogin.USERNAME_SHARED_PREF, "Not Available");

        Intent intent = new Intent(this,Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setContentTitle("E-OFFICE WRC");
        notificationBuilder.setContentText("Masuk sebagai "+user);
        notificationBuilder.setSmallIcon(R.drawable.applogo);

        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());

        getJSON();
    }


    public void notif_clik(View v) {
        ImageButton btn = (ImageButton) findViewById(R.id.btnShowNotif);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        btn.startAnimation(myAdmin);

        Intent i = new Intent(Home.this, ListNotifikasi.class);
        startActivity(i);

        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void menu_satu(View v) {
        Button button1 = (Button) findViewById(R.id.menu1);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        button1.startAnimation(myAdmin);

        Intent i = new Intent(Home.this, ListKaryawan.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

    public void menu_dua(View v) {
        Button button1 = (Button) findViewById(R.id.menu2);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        button1.startAnimation(myAdmin);

        Intent i = new Intent(Home.this, ListSPJ.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

    public void menu_tiga(View v) {
        Button button1 = (Button) findViewById(R.id.menu3);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        button1.startAnimation(myAdmin);

        Intent intent = new Intent(Home.this, ListPortofolio.class);
        startActivity(intent);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void menu_empat(View v) {
        Button button1 = (Button) findViewById(R.id.menu4);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        button1.startAnimation(myAdmin);

        Intent i = new Intent(Home.this, ListProjek.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void menu_lima(View v) {
        Button button1 = (Button) findViewById(R.id.menu5);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        button1.startAnimation(myAdmin);

        Intent intent = new Intent(Home.this, ListProduk.class);
        startActivity(intent);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

    public void menu_enam(View v) {
        Button button1 = (Button) findViewById(R.id.menu6);
        final Animation myAdmin = AnimationUtils.loadAnimation(Home.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        button1.startAnimation(myAdmin);

        Intent i = new Intent(Home.this, Tentang.class);
        startActivity(i);
        Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmpt yBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragments;
        /*if (id == R.id.nav_Beranda) {

            fragments = new Beranda();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.tampilFragment, fragments);
            ft.commit();

        } else */if (id == R.id.nav_goweb) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://waditra.com"));
            startActivity(i);
        } else if (id == R.id.nav_keluar) {
            // Munculkan alert dialog apabila user ingin keluar aplikasi
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Ingin keluar dari akun?");
            alertDialogBuilder.setPositiveButton("Ya",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // Getting out
                            SharedPreferences preferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            // put nilai false untuk login
                            editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, false);

                            // put nilai untuk username
                            editor.putString(DataLogin.USERNAME_SHARED_PREF, "");
                            editor.putString(DataLogin.FOTO_USER,null);
                            editor.putString(DataLogin.FOTO_ADDRESS,"foto");

                            //Simpan ke haredpreferences

                            editor.commit();

                            ProgressDialog pDialog;
                            pDialog = new ProgressDialog(Home.this);
                            pDialog.setMessage("Keluar...");
                            pDialog.setIndeterminate(false);
                            pDialog.setCancelable(false);
                            pDialog.show();

                            new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    // Balik dan tampilkan ke halaman Utama aplikasi jika logout berhasil
                                    Intent intent = new Intent(Home.this, Login.class);
                                    startActivity(intent);
                                }
                            }.start();
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
        }else if(id == R.id.nav_pegawai){
            Intent i = new Intent(Home.this, ListKaryawan.class);
            startActivity(i);
            Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }else if(id == R.id.nav_spj){
            Intent i = new Intent(Home.this, ListSPJ.class);
            startActivity(i);
            Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }else if(id == R.id.nav_portofolio){
            Intent i = new Intent(Home.this, ListPortofolio.class);
            startActivity(i);
            Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }else if(id == R.id.nav_projek){
            Intent i = new Intent(Home.this, ListProjek.class);
            startActivity(i);
            Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }else if(id == R.id.nav_produk){
            Intent i = new Intent(Home.this, ListProduk.class);
            startActivity(i);
            Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }else if(id == R.id.nav_tentang){
            Intent i = new Intent(Home.this, Tentang.class);
            startActivity(i);
            Home.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCall(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:62225201501"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public void onEmail(View view){
        /*final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(intent); */

        Intent myIntent = new Intent(Intent.ACTION_SEND);

        PackageManager pm = getPackageManager();
        Intent tempIntent = new Intent(Intent.ACTION_SEND);
        tempIntent.setType("*/*");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(tempIntent, 0);
        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            if (ri.activityInfo.packageName.contains("android.gm")) {
                myIntent.setComponent(new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));
                myIntent.setAction(Intent.ACTION_SEND);
                myIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rasyidvadrian16@gmail.com"});
                myIntent.setType("message/rfc822");
                myIntent.putExtra(Intent.EXTRA_TEXT, "");
                myIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                myIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("uri://your/uri/string"));
            }
        }
        startActivity(myIntent);
    }
}
