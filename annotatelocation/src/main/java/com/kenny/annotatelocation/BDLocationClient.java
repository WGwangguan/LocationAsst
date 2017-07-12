package com.kenny.annotatelocation;


import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

public class BDLocationClient extends GGLocationClient<LocationClientOption> implements BDLocationListener {

    private LocationReceivedListener<BDLocation> locationReceivedListener;
    private LocationClient client;

    public BDLocationClient(Context context, LocationClientOption option,
                            LocationReceivedListener<BDLocation> listener) {
        client = new LocationClient(context.getApplicationContext());
        client.setLocOption(option);
        client.registerLocationListener(this);
        locationReceivedListener = listener;
    }

    @Override
    public void startLocation() {
        client.start();
    }

    @Override
    public void stopLocation() {
        client.stop();
    }

    @Override
    public int requestLocation() {
        return client.requestLocation();
    }

    @Override
    public void updateOption(LocationClientOption option) {
        client.setLocOption(option);
    }

    @Override
    public void destroyClient() {
        client.stop();
        client.unRegisterLocationListener(this);
        client = null;
        locationReceivedListener = null;
        Log.i("GDapLocationClient", "client had destroyed");
    }

    @Override
    public boolean isStarted() {
        return client.isStarted();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        locationReceivedListener.onLocationReceived(bdLocation);
//        resolveLocation(bdLocation);
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    private void resolveLocation(BDLocation bdLocation) {
        //获取定位结果
        StringBuffer sb = new StringBuffer(256);

        sb.append("time : ");
        sb.append(bdLocation.getTime());    //获取定位时间

        sb.append("\nerror code : ");
        sb.append(bdLocation.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(bdLocation.getLatitude());    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(bdLocation.getLongitude());    //获取经度信息

        sb.append("\nradius : ");
        sb.append(bdLocation.getRadius());    //获取定位精准度

        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(bdLocation.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(bdLocation.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(bdLocation.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(bdLocation.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(bdLocation.getAddrStr());    //获取地址信息

            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(bdLocation.getAddrStr());    //获取地址信息

            sb.append("\noperationers : ");
            sb.append(bdLocation.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

        } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        sb.append("\nlocationdescribe : ");
        sb.append(bdLocation.getLocationDescribe());    //位置语义化信息

        List<Poi> list = bdLocation.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        Log.i("BaiduLocationApiDem", sb.toString());
    }
}
