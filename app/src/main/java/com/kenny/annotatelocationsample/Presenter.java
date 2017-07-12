package com.kenny.annotatelocationsample;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.example.annotation.LocationAsst;
import com.example.annotation.LocationClient;
import com.example.annotation.MapType;
import com.example.annotation.onLocationReceived;
import com.kenny.annotatelocation.GDapLocationClient;

/**
 * Created by Kenny on 2017/7/10 18:20.
 * Desc:
 */
@LocationAsst(value = MapType.TYPE_AMap)
public class Presenter {
    @LocationClient
    GDapLocationClient client;

    public Presenter(Context context) {
        LAPresenter.bind(context, this);
    }

    public void start() {
        client.startLocation();
    }

    public void stop() {
        client.stopLocation();
    }

    public void destroy() {
        LAPresenter.unBind();
    }


    @onLocationReceived
    public void onReceived(AMapLocation location) {
        Log.i("ddd", "onReceived: presenter " + location.getAddress());
    }
}
