package com.braveyet.weathertest3.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/3/24.
 */

public class Basic {
    public String city;
    @SerializedName("id")
  public   String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
