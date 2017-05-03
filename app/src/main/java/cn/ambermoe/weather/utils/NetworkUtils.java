package cn.ambermoe.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ASUS on 2017-05-02.
 * 从网络中获取json字符串
 */

public class NetworkUtils {
    //存储的设置项
    public static SharedPreferences sharedPrefs;
    public static String URL_CONTENT = "http://api.openweathermap.org/data/2.5/forecast/daily?&appid=2e8553a3515fb053ade32facfc132266&cnt=16&units=metric&lang=zh_cn";
    //获取请求的url
    public static URL getUrl(Context context) throws MalformedURLException {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //获取设置 中的位置
        String location = sharedPrefs.getString("location","sanming");
        //将参数添加入uri
        Uri weatherQueryUri = Uri.parse(URL_CONTENT).buildUpon()
                .appendQueryParameter("q", location)
                .build();
        //返回一个url
        return new URL(weatherQueryUri.toString());
    }

    //根据url获取请求的json数据
    public static String getJSONFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestMethod("GET");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String json = null;
            if (hasInput) {
                json = scanner.next();
            }
            scanner.close();
            return json;
        } finally {
            urlConnection.disconnect();
        }
    }
}
