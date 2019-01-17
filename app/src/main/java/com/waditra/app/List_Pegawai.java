package com.waditra.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.waditra.app.Alat.Adapter;
import com.waditra.app.Alat.AppController;
import com.waditra.app.Alat.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class List_Pegawai extends AppCompatActivity {
    private ProgressDialog Dialog;
    private ListView listView;
    private List<Item> array = new ArrayList<Item>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpegawai);

        listView = (ListView)findViewById(R.id.listView5);
        adapter = new Adapter(this, array);
        listView.setAdapter(adapter);

        Dialog = new ProgressDialog(this);
        Dialog.setMessage("Loading...");
        Dialog.show();

        loadData();
    }
    public void loadData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DataLogin.URL+"?list=1", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hideDialog();

                for(int i = 0; i<response.length();i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        Item item = new Item();
                        item.setNamaK(obj.getString("nama"));
                        item.setJabatan(obj.getString("jabatan"));
                        item.setIdK(obj.getString("id"));
                        //item.setImage(obj.getString("image"));

                        array.add(item);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getmInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void hideDialog(){
        Dialog.hide();
        //if(Dialog !=null){
          //  Dialog.hide();
            //Dialog=null;
        //}
    }
    public void onBack(View view){
        finish();
    }
}
