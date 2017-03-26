package com.braveyet.weathertest3.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/3/24.
 */

public class Suggestion {
    public Sport sport;
    public class Sport{

        @SerializedName("txt")
        public String info;
    }
    @SerializedName("comf")
    public Comfort comfort;
    public class Comfort{
        @SerializedName("txt")
        public String info;
    }
    @SerializedName("cw")
    public CarWash carWash;
    public class CarWash{
        @SerializedName("txt")
        public String info;
    }
}
