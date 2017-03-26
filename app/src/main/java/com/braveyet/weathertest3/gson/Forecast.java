package com.braveyet.weathertest3.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/3/24.
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        public String min;
        public String max;
    }
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt_d")
        public String weatherInfo;
    }
}
