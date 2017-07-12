package com.kenny.annotatelocation;


import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class GDapLocationClient extends GGLocationClient<AMapLocationClientOption> implements AMapLocationListener {

    private LocationReceivedListener<AMapLocation> locationReceivedListener;
    private AMapLocationClient client;

    public GDapLocationClient(Context context, AMapLocationClientOption option,
                              LocationReceivedListener<AMapLocation> listener) {
        client = new AMapLocationClient(context.getApplicationContext());
        client.setLocationOption(option);
        client.setLocationListener(this);
        locationReceivedListener = listener;
    }

    @Override
    public void startLocation() {
        client.startLocation();
    }

    @Override
    public void stopLocation() {
        client.stopLocation();
    }

    @Override
    public int requestLocation() {
        return -1;
    }

    @Override
    void updateOption(AMapLocationClientOption option) {
        client.setLocationOption(option);
    }

    @Override
    public void destroyClient() {
        client.stopLocation();
        client.unRegisterLocationListener(this);
        client.onDestroy();
        client = null;
        locationReceivedListener = null;
            Log.i("GDapLocationClient", "client had destroyed");
    }

    @Override
    public boolean isStarted() {
        return client.isStarted();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        locationReceivedListener.onLocationReceived(aMapLocation);
    }
}
