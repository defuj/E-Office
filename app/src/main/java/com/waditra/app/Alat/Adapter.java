package com.waditra.app.Alat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.waditra.app.R;

import java.util.List;

/**
 * Created by defuj on 30/08/2016.
 */
public class Adapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> items;
    ImageLoader imageLoader = AppController.getmInstance().getmImageLoader();

    public Adapter(Activity activity, List<Item> items){
        this.activity = activity;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_datapegawai, null);
        }

        TextView nama = (TextView)convertView.findViewById(R.id.namaPegawai);
        TextView jabatan = (TextView)convertView.findViewById(R.id.jabatanPegawai);
        TextView id = (TextView)convertView.findViewById(R.id.idPegawai);

        Item item = items.get(position);

        nama.setText(item.getNamaK());

        jabatan.setText(item.getJabatan());

        id.setText(item.getIdK());

        /*if(imageLoader==null){
            imageLoader=AppController.getmInstance().getmImageLoader();
            //NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.fotoKaryawan);
            TextView nama = (TextView)convertView.findViewById(R.id.namaPegawai);
            TextView jabatan = (TextView)convertView.findViewById(R.id.jabatanPegawai);
            TextView id = (TextView)convertView.findViewById(R.id.idPegawai);

            Item item = items.get(position);

            //imageView.setImageUrl(item.getImage(), imageLoader);

            nama.setText(item.getNamaK());

            jabatan.setText(item.getJabatan());

            id.setText(item.getIdK());
        } */

        return convertView;
    }
}
