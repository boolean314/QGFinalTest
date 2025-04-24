package com.marknote.android.viewPager.json;

import java.util.List;

public class Weather {
    private String city;
    private String date;
    private String week;
    private String wea;
    private String wea_img;
    private String tem;
    private String tem1;
    private String tem2;

    public String getTem1() {
        return tem1;
    }


    public String getTem2() {
        return tem2;
    }


    private List<HourlyWeather> hours;

    public List<HourlyWeather> getHours() {
        return hours;
    }

    //内部类
    public static class HourlyWeather {
        private String hours;
        private String wea_img;
        private String tem;

        public String getHours() {
            return hours;
        }


        public String getWeaImg() {
            return wea_img;
        }


        public String getTem() {
            return tem;
        }


    }

    //
    public String getCity() {
        return city;
    }


    public String getDate() {
        return date;
    }


    public String getWeek() {
        return week;
    }


    public String getWea() {
        return wea;
    }


    public String getWeaImg() {
        return wea_img;
    }

    public String getTem() {
        return tem;
    }

}
