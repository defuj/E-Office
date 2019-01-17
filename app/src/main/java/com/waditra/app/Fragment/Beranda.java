package com.waditra.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.waditra.app.R;

/**
 * Created by defuj on 10/08/2016.
 */
public class Beranda extends Fragment{
    public Beranda(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStace){
        return inflater.inflate(R.layout.fragment_beranda,container,false);
    }
}
