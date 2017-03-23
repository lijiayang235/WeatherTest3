package com.braveyet.weathertest3.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/3/23.
 */

public class County extends DataSupport {
    public String countyCode;
    public String countyName;
    public String cityCode;
    public String weatherId;

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getCountyName() {
        return countyName;
    }
}
