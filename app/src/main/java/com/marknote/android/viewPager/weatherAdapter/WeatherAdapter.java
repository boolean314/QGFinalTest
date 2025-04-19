package com.marknote.android.viewPager.weatherAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.marknote.android.R;
import com.marknote.android.viewPager.fragment.WeatherFragment;
import com.marknote.android.viewPager.json.Weather;

import org.w3c.dom.Text;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<Weather.HourlyWeather> mHourlyList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hours;
        ImageView wea_img;
        TextView tem;

        public ViewHolder(View view) {
            super(view);
            hours = view.findViewById(R.id.item_hours);
            wea_img = view.findViewById(R.id.item_wea_img);
            tem = view.findViewById(R.id.item_tem);

        }
    }


    public WeatherAdapter(List<Weather.HourlyWeather> hourlyList) {
        mHourlyList = hourlyList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_weather_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Weather.HourlyWeather hourlyWeather = mHourlyList.get(position);
        holder.hours.setText(hourlyWeather.getHours());
        holder.tem.setText(hourlyWeather.getTem() + "℃");
        String image = hourlyWeather.getWeaImg();
        if (image.equals("yun")) {
            holder.wea_img.setImageResource(R.drawable.yun);
        } else if (image.equals("lei")) {
            holder.wea_img.setImageResource(R.drawable.lei);
        } else if (image.equals("qing")) {
            holder.wea_img.setImageResource(R.drawable.qing);
        } else if (image.equals("shachen")) {
            holder.wea_img.setImageResource(R.drawable.shachen);
        } else if (image.equals("wu")) {
            holder.wea_img.setImageResource(R.drawable.wu);
        } else if (image.equals("xue")) {
            holder.wea_img.setImageResource(R.drawable.xue);
        } else if (image.equals("yin")) {
            holder.wea_img.setImageResource(R.drawable.yin);
        } else if (image.equals("yu")) {
            holder.wea_img.setImageResource(R.drawable.yu);
        } else if (image.equals("bingbao")) {
            holder.wea_img.setImageResource(R.drawable.bingbao);
        }


    }


    @Override
    public int getItemCount() {
        return mHourlyList.size();
    }


}
