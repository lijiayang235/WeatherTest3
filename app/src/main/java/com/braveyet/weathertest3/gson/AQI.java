package com.braveyet.weathertest3.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/3/24.
 */

public class AQI {

    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }

}
