package com.waditra.app.Alat;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by defuj on 30/08/2016.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized AppController getmInstance(){
        return mInstance;
    }
    public RequestQueue getmRequestQueue(){
        if(mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public ImageLoader getmImageLoader(){
        getmRequestQueue();
        if(mImageLoader==null){
            mImageLoader = new ImageLoader(this.mRequestQueue,new BitmapCache());
        }
        return this.mImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag((TextUtils.isEmpty(tag) ? TAG :tag));
        getmRequestQueue().add(request);
    }
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getmRequestQueue().add(request);
    }
    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
