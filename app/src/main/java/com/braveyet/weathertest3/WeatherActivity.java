package com.braveyet.weathertest3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.braveyet.weathertest3.Service.AutoUpdateService;
import com.braveyet.weathertest3.gson.Forecast;
import com.braveyet.weathertest3.gson.Weather;
import com.braveyet.weathertest3.unity.HttpUtil;
import com.braveyet.weathertest3.unity.Utility;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public TextView titleCity;
    public TextView titleUpdate;
    public Button navButton;
    public TextView nowTemp;
    public TextView nowInfo;
    public LinearLayout weatherforecast;
    public TextView pm25;
    public TextView aqi;
    public TextView comfort;
    public TextView carwash;
    public TextView sport;
    public ScrollView weatherLayout;
    public String weatherId;
    public ImageView bingPic;
    public SwipeRefreshLayout swipeLayout;
    public DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdate = (TextView) findViewById(R.id.title_update);
        nowTemp = (TextView) findViewById(R.id.now_temp);
        nowInfo = (TextView) findViewById(R.id.now_weatherinfo);
        pm25 = (TextView) findViewById(R.id.pm25);
        aqi = (TextView) findViewById(R.id.aqi);
        comfort = (TextView) findViewById(R.id.comfort);
        carwash = (TextView) findViewById(R.id.carwash);
        sport = (TextView) findViewById(R.id.sport);
        weatherLayout = (ScrollView) findViewById(R.id.weather_Layout);
        weatherforecast = (LinearLayout) findViewById(R.id.forecast_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        bingPic = (ImageView) findViewById(R.id.bing_pic);
        swipeLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        drawerLayout= (DrawerLayout) findViewById(R.id.nav);
        navButton= (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String reponseText = preferences.getString("weather", null);
        if (reponseText != null) {
            Weather weather = Utility.handleWeather(reponseText);
            weatherId = weather.basic.weatherId;
            showWeather(weather);
        } else {
            weatherLayout.setVisibility(View.GONE);
            weatherId = getIntent().getStringExtra("weatherId");
            requestWeather(weatherId);
        }
        String address = preferences.getString("bing_pic", null);
        if (address != null) {
            Glide.with(this).load(address).into(bingPic);

        } else {
            requestBingPic();
        }


    }

    private void requestBingPic() {
        String url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttp3(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "加载图片失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicText = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPicText);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPicText).into(bingPic);
                    }
                });

            }
        });
    }

    public  void requestWeather(String mweatherId) {
        String address = "http://guolin.tech/api/weather?cityid=" + mweatherId + "&key=19dbb264703e45ff96e4373c0a67268b";
        HttpUtil.sendOkHttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this, "获取天气数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();

                final Weather weather = Utility.handleWeather(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        if (weather != null && weather.status.equals("ok")) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            weatherId = weather.basic.weatherId;
                            showWeather(weather);
                            Intent intent=new Intent(WeatherActivity.this, AutoUpdateService.class);
                            startService(intent);

                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气数据失败", Toast.LENGTH_SHORT).show();
                        }


                    }

                });

            }
        });
    }

    private void showWeather(Weather weather) {
        titleCity.setText(weather.basic.city);
        titleUpdate.setText(weather.basic.update.updateTime );
        nowTemp.setText(weather.now.temperature+ "℃");
        nowInfo.setText(weather.now.more.weatherInfo);
        weatherforecast.removeAllViews();
        for (Forecast f : weather.forecastList) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, weatherforecast, false);
            TextView weatherInfo = (TextView) view.findViewById(R.id.forecast_info);
            TextView weatherTime = (TextView) view.findViewById(R.id.forecast_time);
            TextView weatherMin = (TextView) view.findViewById(R.id.forecast_min);
            TextView weatherMax = (TextView) view.findViewById(R.id.forecast_max);
            weatherInfo.setText(f.more.weatherInfo);
            weatherTime.setText(f.date);
            weatherMin.setText(f.temperature.min+ "℃");
            weatherMax.setText(f.temperature.max+ "℃");
            weatherforecast.addView(view);
        }
        if(weather.aqi!=null){
            pm25.setText(weather.aqi.city.pm25);
            aqi.setText(weather.aqi.city.aqi);
        }

        comfort.setText("舒适指数:" + weather.suggestion.comfort.info);
        carwash.setText("洗车指数:" + weather.suggestion.carWash.info);
        sport.setText("运动指数:" + weather.suggestion.sport.info);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
