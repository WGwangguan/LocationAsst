package com.kenny.annotatelocation;


public abstract class GGLocationClient<T> {
    abstract void startLocation();

    abstract void stopLocation();

    abstract int requestLocation();

    abstract void updateOption(T option);

    abstract void destroyClient();

    abstract boolean isStarted();

}
