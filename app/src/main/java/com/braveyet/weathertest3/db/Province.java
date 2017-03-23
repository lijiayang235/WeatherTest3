package com.braveyet.weathertest3.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Province extends DataSupport {
    public String provinceCode;
    public String provinceName;

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }
}
