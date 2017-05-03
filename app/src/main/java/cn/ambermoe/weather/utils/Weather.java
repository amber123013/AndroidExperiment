package cn.ambermoe.weather.utils;

/**
 * Created by ASUS on 2017-05-02.
 * 实体类 用于存放一天的天气数据
 */

public class Weather {
    //最低温度
    private double min;
    //最高温度
    private double max;
    //时间
    private String date;
    //天气情况
    private String description;
    //显示的图片资源id

    private int imgResourceId;
    //位置
    private String location;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

}
