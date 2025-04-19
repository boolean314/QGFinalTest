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

    public void setTem1(String tem1) {
        this.tem1 = tem1;
    }

    public String getTem2() {
        return tem2;
    }

    public void setTem2(String tem2) {
        this.tem2 = tem2;
    }

    private List<HourlyWeather> hours; // 这里应该是一个列表
    public List<HourlyWeather> getHours() {
        return hours;
    }

    public void setHours(List<HourlyWeather> hours) {
        this.hours = hours;
    }
    // getter和setter方法


    //内部类
    public static class HourlyWeather {
        //小时
        private String hours;
        //天气图片
        private String wea_img;
        //温度
        private String tem;

        //获取小时
        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getWeaImg() {
            return wea_img;
        }

        public void setWeaImg(String wea_img) {
            this.wea_img = wea_img;
        }

        public String getTem() {
            return tem;
        }

        public void setTem(String tem) {
            this.tem = tem;
        }
    }
    //
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWea() {
        return wea;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public String getWeaImg() {
        return wea_img;
    }

    public void setWeaImg(String wea_img) {
        this.wea_img = wea_img;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }


}
