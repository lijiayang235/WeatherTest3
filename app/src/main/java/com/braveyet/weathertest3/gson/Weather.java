package com.braveyet.weathertest3.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yf on 2017/3/24.
 */

public class Weather {
   public AQI aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    public String status;
    @SerializedName("daily_forecast")
    public List<Forecast>forecastList;
}
