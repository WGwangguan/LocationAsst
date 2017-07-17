[![](https://jitpack.io/v/WGwangguan/LocationAsst.svg)](https://jitpack.io/#WGwangguan/LocationAsst)

通过注解的方式，更方便的使用定位服务。

### 特点
1. 支持注解方式获取定位信息。
2. 支持百度定位及高德定位。


### 简单例子
```java
@LocationAsst(value = MapType.TYPE_BDMap)//指定定位类型，默认百度定位
public class MainActivity extends AppCompatActivity {
    @LocationClient
    BDLocationClient bdClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LAMainActivity.bind(this, this);//绑定定位服务

        bdClient.updateOption(myBDOption());//如不设置Option则使用默认的Option

        findViewById(R.id.btn).setOnClickListener(v -> {
            if (bdClient.isStarted()) {
                bdClient.stopLocation();//停止定位
            } else {
                bdClient.startLocation();//启动定位
            }
        });

    }
    
    //接收定位回调
    @onLocationReceived
    void onReceived(BDLocation location) {
        Log.i("ddd", "onReceived: " + location.getAddrStr());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LAMainActivity.unBind();//在不需要定位服务的时候解绑定位服务
    }
    
}
```



## 使用方式
### 配置根目录的build.gradle 
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
### 配置app module的build.gradle 
```
dependencies {
    compile 'com.github.WGwangguan.LocationAsst:annotatelocation:v1.0.0'
    compile 'com.github.WGwangguan.LocationAsst:annotations:v1.0.0'
    annotationProcessor 'com.github.WGwangguan.LocationAsst:processor:v1.0.0'
}
```
### 配置AndroidManifest.xml
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.xxx.xxx">

    <!-- 这个权限用于进行网络定位-->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <!-- 这个权限用于访问GPS定位-->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位-->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!-- 获取运营商信息，用于支持提供运营商信息相关的接口-->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- 这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位-->
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <!-- 用于读取手机当前的状态-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!-- 写入扩展存储，向扩展卡写入数据，用于写入离线定位数据-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <!-- 访问网络，网络定位需要上网-->
    <uses-permission android:name="android.permission.INTERNET" />
    <!-- SD卡读取权限，用户写入离线定位数据-->
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!--百度定位服务-->
        <meta-data
            android:name="com.baidu.lbsapi.API_KEY"
            android:value="dfaKCMwZ3AWFYsOTwQfEoBG5TmjozgaA" />

        <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote" />

        <!--高德定位服务-->
        <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="6980330d930a3b6f2037d5a71884ef1c" />

        <service android:name="com.amap.api.location.APSService" />

    </application>

</manifest>
```
根据需要配置百度/高德定位服务。

### 其它说明
6.0以上请做好权限处理。

## License

    Copyright 2017 WGwangguan, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

