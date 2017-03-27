package com.braveyet.weathertest3.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.util.Log;

import com.braveyet.weathertest3.gson.Weather;
import com.braveyet.weathertest3.unity.HttpUtil;
import com.braveyet.weathertest3.unity.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myTest","onStartCommand");
        updateBingPic();
        updateWeather();
        AlarmManager alarm= (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        alarm.cancel(pi);
        long hours=8*60*60*1000;

        long triggertime= SystemClock.elapsedRealtime()+hours;
        alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggertime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        final String reponsetext=prefs.getString("weather",null);
        if(reponsetext!=null){
            Weather weather= Utility.handleWeather(reponsetext);
            String weatherId=weather.basic.weatherId;
            String address="http://guolin.tech/api/weather?cityid="+weatherId+"&key=19dbb264703e45ff96e4373c0a67268b";
            HttpUtil.sendOkHttp3(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String reponseText=response.body().string();
                    SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("weather",reponseText);
                    editor.apply();
                }
            });
        }
    }

    private void updateBingPic() {
        String address="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPicText=response.body().string();
                SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPicText);
                editor.apply();
            }
        });
    }
}
