package com.waditra.app.Proses;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by defuj on 11/08/2016.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    public static ProgressDialog pDialog;
    AlertDialog alertDialog;
    public BackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://csmd-shop.16mb.com/webservice/login.php";
        String register_url = "http://csmd-shop.16mb.com/webservice/daftar.php";

        if(type.equals("login")){
            try{
                String username = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine())!= null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(type.equals("register")){
            try{
                String name = params[1];
                String username = params[2];
                String password = params[3];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"
                        +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine())!= null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Logging...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        //alertDialog.show();
        if(result.equals("success")){
            pDialog.cancel();
            Toast.makeText(context.getApplicationContext(), "Sukses", Toast.LENGTH_LONG).show();
        }else if(result.equals("was registered")){
            pDialog.cancel();
            Toast.makeText(context.getApplicationContext(), "Nama Pengguna Telah Terdaftar", Toast.LENGTH_LONG).show();
        }else if(result.equals("failed")){
            pDialog.cancel();
            Toast.makeText(context.getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
