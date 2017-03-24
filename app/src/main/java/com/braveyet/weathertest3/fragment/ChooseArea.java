package com.braveyet.weathertest3.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.braveyet.weathertest3.R;
import com.braveyet.weathertest3.db.City;
import com.braveyet.weathertest3.db.County;
import com.braveyet.weathertest3.db.Province;
import com.braveyet.weathertest3.unity.HttpUtil;
import com.braveyet.weathertest3.unity.Utility;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ChooseArea extends Fragment {
    public static int LEVEL_PROVINCE=0;
    public static int LEVEL_CITY=1;
    public static int LEVEL_COUNTY=2;
    public Button backButton;
    public TextView titleText;
    public ListView listView;
    public ArrayAdapter<String>adapter;
    public List<String>datalist=new ArrayList<>();
    public List<Province>provinceList;
    public List<City>cityList;
    public List<County>countyList;
    public Province selectProvince;
    public City selectCity;
    public County selectCounty;
    public  int currentLevel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.choose_area,container);
        backButton= (Button) view.findViewById(R.id.back_button);
        titleText= (TextView) view.findViewById(R.id.title_text);
        listView= (ListView) view.findViewById(R.id.listview);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCity();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               if(currentLevel==LEVEL_PROVINCE) {
                   selectProvince=provinceList.get(position);
                   queryCity();
               }else if(currentLevel==LEVEL_CITY){
                   selectCity=cityList.get(position);
                   queryCounty();
               }else if(currentLevel==LEVEL_COUNTY){
                   selectCounty=countyList.get(position);

               }
            }
        });
        queryProvince();
    }

    private void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList=DataSupport.findAll(Province.class);
        datalist.clear();

        if(provinceList.size()>0){

            for(Province p:provinceList){
                ;datalist.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else {

            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendOkHttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(),"获取城市失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText=response.body().string();

                boolean access=false;
                if(type.equals("province")){

                    access= Utility.handleProvince(responseText);

                }else if(type.equals("city")){
                    access=Utility.handleCity(responseText,selectProvince.getProvinceCode());
                }else if(type.equals("county")){
                    access=Utility.handleCounty(responseText,selectCity.getCityCode());
                }

                if(access){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(type.equals("province")){
                                Log.d("myTest","queryfromServ-province-runonUi");
                                queryProvince();
                            }else if(type.equals("city")){
                                queryCity();
                            }else if(type.equals("county")){
                                queryCounty();
                            }
                        }
                    });

                }
            }
        });
    }

    private void queryCity() {
        titleText.setText(selectProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceCode=?",selectProvince.getProvinceCode()).find(City.class);
        datalist.clear();
        if(cityList.size()>0){

            for(City city:cityList){
                ;datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            String address="http://guolin.tech/api/china/"+selectProvince.getProvinceCode();
            queryFromServer(address,"city");
        }
    }
    private void queryCounty() {
        titleText.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityCode=?",selectCity.getCityCode()).find(County.class);
        datalist.clear();
        if(countyList.size()>0){

            for(County county:countyList){
                ;datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else {
            String address="http://guolin.tech/api/china/"+selectProvince.getProvinceCode()+"/"+selectCity.getCityCode();
            queryFromServer(address,"county");
        }
    }

}
