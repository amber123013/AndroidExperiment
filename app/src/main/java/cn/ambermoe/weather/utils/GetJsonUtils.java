package cn.ambermoe.weather.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ambermoe.weather.R;

/**
 * Created by ASUS on 2017-05-02.
 * 将json字符串解析为 Weather
 */

public class GetJsonUtils {

    public static Weather[] getWeatherDataFromJson(Context context,String json) throws JSONException {
        //存放天气数据
        Weather[] array;
        //构建json对象
        JSONObject weatherJson = new JSONObject(json);

        /* json字符创是否有错误代码
        *错误时返回此代码{"cod":401, "message": "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."}
        */
        if (weatherJson.has("cod")) {
            int errorCode = weatherJson.getInt("cod");

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* 404*/
                    return null;
                default:
                    /* 其他错误代码*/
                    return null;
            }
        }

        //json对象中获取天气列表
        JSONArray weatherArray = weatherJson.getJSONArray("list");
        String location;
        array = new Weather[weatherArray.length()];
        //城市名 + 国家名
        location = weatherJson.getJSONObject("city").getString("name")+" · "+ weatherJson.getJSONObject("city").getString("country");
        for (int i = 0; i < weatherArray.length(); i++) {
            /** list 每一项的结构
             * {
                 "dt":1493697600,
                 "temp":{
                     "day":18.99,
                     "min":18.99,
                     "max":18.99,
                     "night":18.99,
                     "eve":18.99,
                     "morn":18.99
                 },
                 "pressure":969.3,
                 "humidity":96,
                 "weather":[
                     {
                         "id":801,
                         "main":"Clouds",
                         "description":"晴，少云",
                         "icon":"02n"
                     }
                 ],
                 "speed":0.88,
                 "deg":95,
                 "clouds":24
             }
             */
            String date;
            long dateTimeMillis;
            double max;
            double min;
            int weatherId;

            String description;

            JSONObject weather = weatherArray.getJSONObject(i);

            //获取时间戳  得到的是秒数转换为毫秒
            dateTimeMillis = weather.getLong("dt") * 1000;

            //装换为日期
            date = fromTimeMillisGetDate(new Date(dateTimeMillis));
            //date = String.valueOf(dateTimeMillis);

            JSONObject weatherObject =
                    weather.getJSONArray("weather").getJSONObject(0);
            //天气id
            weatherId = weatherObject.getInt("id");
            //图片资源id
            weatherId = getArtResourceIdForWeatherCondition(weatherId);
            description = weatherObject.getString("description");
            //记录温度情况的jsonobject
            JSONObject temperatureObject = weather.getJSONObject("temp");
            max = temperatureObject.getDouble("max");
            min = temperatureObject.getDouble("min");

            Weather temp = new Weather();
            temp.setMin(min);
            temp.setMax(max);
            temp.setDescription(description);
            temp.setDate(date);
            temp.setImgResourceId(weatherId);
            temp.setLocation(location);
            array[i] = temp;
        }

        return array;
    }

    private static String fromTimeMillisGetDate(Date time) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLdd日");
        return dateFormat.format(time);
    }
    //根据json中的 weather对象中的id 获得要显示的图片
    public static int getArtResourceIdForWeatherCondition(int weatherId) {

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.art_storm;
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.art_storm;
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.art_clear;
        }

        return R.drawable.art_storm;
    }
}
