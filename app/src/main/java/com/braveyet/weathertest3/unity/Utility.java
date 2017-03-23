package com.braveyet.weathertest3.unity;

import com.braveyet.weathertest3.db.City;
import com.braveyet.weathertest3.db.County;
import com.braveyet.weathertest3.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Utility {
    public static boolean handleProvince(String responseText){
        try {
            JSONArray jsonArray=new JSONArray(responseText);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Province province=new Province();
                province.setProvinceCode(jsonObject.getString("id"));
                province.setProvinceName(jsonObject.getString("name"));
                province.save();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean handleCity(String responseText,String provinceCode){
        try {
            JSONArray jsonArray=new JSONArray(responseText);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                City city=new City();
                city.setCityCode(jsonObject.getString("id"));
                city.setCityName(jsonObject.getString("name"));
                city.setProvinceCode(provinceCode);
                city.save();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  false;
    }
    public static boolean handleCounty(String responseText,String cityCode){
        try {
            JSONArray jsonArray=new JSONArray(responseText);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                County county=new County();
                county.setCountyCode(jsonObject.getString("id"));
                county.setCountyName(jsonObject.getString("name"));
                county.setWeatherId(jsonObject.getString("weather_id"));
                county.setCityCode(cityCode);
                county.save();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  false;
    }
}
