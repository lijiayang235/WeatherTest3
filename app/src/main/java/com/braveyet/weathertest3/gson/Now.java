package com.braveyet.weathertest3.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/3/24.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String weatherInfo;
    }
}
