package com.kenny.annotatelocationsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.example.annotation.LocationAsst;
import com.example.annotation.LocationClient;
import com.example.annotation.onLocationReceived;
import com.kenny.annotatelocation.BDLocationClient;

@LocationAsst
public class MainActivity extends AppCompatActivity {

    @LocationClient
    public BDLocationClient bdClient;

    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LAMainActivity.bind(this, this);

        bdClient.updateOption(myBDOption());

        presenter = new Presenter(this);
        findViewById(R.id.btn).setOnClickListener(v -> {
            if (bdClient.isStarted()) {
                presenter.start();
                bdClient.stopLocation();
            } else {
                bdClient.startLocation();
                presenter.stop();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        LAMainActivity.unBind();
    }

    @onLocationReceived
    void onReceived(BDLocation location) {
        Log.i("ddd", "onReceived: " + location.getAddrStr());
    }

    @onLocationReceived
    void onHAHA(BDLocation location) {
        Log.i("ddd", "onHAHA: " + location.getTime());
    }

    private LocationClientOption myBDOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        return option;
    }

}
