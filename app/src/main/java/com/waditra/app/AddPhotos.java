package com.waditra.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.waditra.app.Alat.BounceInterpolator;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

public class AddPhotos extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private SimpleDraweeView userphoto;
    private EditText namaPhotos;
    private ImageView imgGambar;
    private ImageView sett;
    private TextView namaFoto;
    private ProgressDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_addphotos);
        userphoto = (SimpleDraweeView)findViewById(R.id.imgPhotos);
        namaPhotos = (EditText)findViewById(R.id.namaPhotos);
        imgGambar = (ImageView)findViewById(R.id.imgGambar);
        namaFoto = (TextView)findViewById(R.id.namaFoto);
        sett = (ImageView)findViewById(R.id.btnSet);
        sett.setEnabled(false);

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                final SimpleDraweeView img = new SimpleDraweeView(getApplicationContext());
                alertDialog.setView(img);
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });

        setPhoto();
    }

    public void setPhoto() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String foto_user = sharedPreferences.getString(DataLogin.FOTO_USER, null);

        if(foto_user != null){
            Uri fotoUser = Uri.parse(foto_user);
            userphoto.setImageURI(fotoUser);
        }
    }


    public void onBack2(View view){
        Intent i = new Intent(AddPhotos.this,Profil.class);
        startActivity(i);
    }

    public void browsePhotos(View view){
        ImageView browse = (ImageView)findViewById(R.id.btnBrowse);
        final Animation myAdmin = AnimationUtils.loadAnimation(AddPhotos.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        browse.startAnimation(myAdmin);

        //Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i,RESULT_LOAD_IMAGE);

        int a = getWindowManager().getDefaultDisplay().getWidth();

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        /*i.putExtra("crop","true");
        i.putExtra("aspectX",1);
        i.putExtra("aspectY",1);
        i.putExtra("outputX",a);
        i.putExtra("outputY",a);
        i.putExtra("return-data",true); */
        startActivityForResult(i,RESULT_LOAD_IMAGE);
    }

    public void Cancle(View view){
        ImageView cancle = (ImageView) findViewById(R.id.btnCencel);
        final Animation myAdmin = AnimationUtils.loadAnimation(AddPhotos.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        cancle.startAnimation(myAdmin);

        userphoto.setImageURI(null);
        imgGambar.setImageURI(null);
    }
    public void Set(View view){
        ImageView set = (ImageView)findViewById(R.id.btnSet);
        final Animation myAdmin = AnimationUtils.loadAnimation(AddPhotos.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);
        set.startAnimation(myAdmin);

        Dialog = new ProgressDialog(this);
        Dialog.setMessage("Uploading...");
        Dialog.setCancelable(false);
        Dialog.show();

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                prosesPhoto();
            }
        }.start();
    }

    public void prosesPhoto() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(DataLogin.USERNAME_SHARED_PREF, "Not Available");
        namaPhotos.setText(user);

        char[] chars1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        for (int i = 0; i < 50; i++){
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        String random_string = sb1.toString();
        namaFoto.setText(random_string);

        Bitmap image = ((BitmapDrawable)imgGambar.getDrawable()).getBitmap();
        new uploadImage(image,namaFoto.getText().toString(),namaPhotos.getText().toString()).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            /*Bundle extras = data.getExtras();
            Bitmap image = extras.getParcelable("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),image,"title",null);

            Uri.parse(path);
            String gambar = Uri.parse(path).toString(); */
            //Uri selectedImage = Uri.parse(gambar);

            Uri selectedImage = data.getData();

            userphoto.setImageURI(selectedImage);
            imgGambar.setImageURI(selectedImage);

            sett.setEnabled(true);

            String img = selectedImage.toString();
            SharedPreferences sharedPreferences = AddPhotos.this.getSharedPreferences(DataLogin.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(DataLogin.LOGGEDIN_SHARED_PREF, true);
            editor.putString(DataLogin.FOTO_USER, img);
            editor.commit();
        }
    }

    private class uploadImage extends AsyncTask<Void,Void,Void>{
        Bitmap image;
        String name;
        String username;

        public uploadImage(Bitmap image,String name,String username){
            this.image = image;
            this.name = name;
            this.username = username;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            //String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image",encodedImage));
            dataToSend.add(new BasicNameValuePair("name",name));
            dataToSend.add(new BasicNameValuePair("username",username));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(DataLogin.URL+"?UploadPhotos=1");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Dialog.hide();
            Dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Image Uploaded",Toast.LENGTH_SHORT).show();
            //new DownloadImage(namaFoto.getText().toString()).execute();
        }
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    private class DownloadImage extends AsyncTask<Void,Void,Bitmap>{
        String name;

        public DownloadImage(String name){
            this.name = name;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url = DataLogin.URL_PHOTOS_USERS+name+".JPG";

            try{
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000 * 30);

                return BitmapFactory.decodeStream((InputStream)connection.getContent(),null,null);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                //userphoto.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddPhotos.this, Profil.class);
        startActivity(i);
    }


}
