package com.marknote.android.viewPager.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.marknote.android.R;
import com.marknote.android.viewPager.json.HttpUtil;
import com.marknote.android.viewPager.json.Weather;
import com.marknote.android.viewPager.weatherAdapter.WeatherAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class WeatherFragment extends Fragment {
    private View view;
    private Weather weather;
    private String city, date, week, wea, weaImg, tem, tem1, tem2;
    private List<Weather.HourlyWeather> hourlyList;
    private TextView cityText, dateText, weekText, weaText, temText, hlText;

    private ImageView weaImgImage;


    @Override
    // 创建视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (weather == null) {
            // 使用布局文件fragment_weather.xml填充视图
            view = inflater.inflate(R.layout.fragment_weather, container, false);
            // 初始化组件
            cityText = view.findViewById(R.id.city);
            dateText = view.findViewById(R.id.date);
            weekText = view.findViewById(R.id.week);
            weaText = view.findViewById(R.id.wea);
            temText = view.findViewById(R.id.tem);
            weaImgImage = view.findViewById(R.id.wea_img);
            hlText = view.findViewById(R.id.hl);


            HttpUtil.sendOkHttpRequest("https://gfeljm.tianqiapi.com/api?unescape=1&version=v63&appid=52686657&appsecret=o6yhe8Yb", new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到服务器返回的具体内容
                    String responseData = response.body().string();

                    parseJSONWithGSON(responseData);

                    requireActivity().runOnUiThread(() -> {
                        cityText.setText(city);
                        dateText.setText(date);
                        weekText.setText(week);
                        weaText.setText(wea);
                        temText.setText(tem + "℃");
                        hlText.setText("最低" + tem2 + "℃最高" + tem1 + "℃");
                        chooseImage(weaImg);
                        //设置recyclerView
                        RecyclerView recyclerView = view.findViewById(R.id.weather_recycler_view);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        WeatherAdapter adapter = new WeatherAdapter(hourlyList);
                        recyclerView.setAdapter(adapter);
                    });


                }

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    // 在这里进行异常处理
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "获取天气信息失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
        return view;

    }


    //解析JSON数据
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        weather = gson.fromJson(jsonData, Weather.class);

        city = weather.getCity();
        date = weather.getDate();
        week = weather.getWeek();
        wea = weather.getWea();
        tem = weather.getTem();
        weaImg = weather.getWeaImg();
        tem1 = weather.getTem1();
        tem2 = weather.getTem2();
        hourlyList = weather.getHours();

    }

    //根据返回的wea_img匹配天气图片
    private void chooseImage(String weaImg) {
        if (weaImg.equals("yun")) {
            weaImgImage.setImageResource(R.drawable.yun);
        } else if (weaImg.equals("lei")) {
            weaImgImage.setImageResource(R.drawable.lei);
        } else if (weaImg.equals("qing")) {
            weaImgImage.setImageResource(R.drawable.qing);
        } else if (weaImg.equals("shachen")) {
            weaImgImage.setImageResource(R.drawable.shachen);
        } else if (weaImg.equals("wu")) {
            weaImgImage.setImageResource(R.drawable.wu);
        } else if (weaImg.equals("xue")) {
            weaImgImage.setImageResource(R.drawable.xue);
        } else if (weaImg.equals("yin")) {
            weaImgImage.setImageResource(R.drawable.yin);
        } else if (weaImg.equals("yu")) {
            weaImgImage.setImageResource(R.drawable.yu);
        } else if (weaImg.equals("bingbao")) {
            weaImgImage.setImageResource(R.drawable.bingbao);
        }
    }

}