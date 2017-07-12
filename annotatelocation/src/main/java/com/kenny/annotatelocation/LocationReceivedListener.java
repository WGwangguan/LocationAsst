package com.kenny.annotatelocation;

/**
 * Created by Kenny on 2017/7/3 16:54.
 * Desc：
 */

public interface LocationReceivedListener<T> {
    void onLocationReceived(T location);
}
