package cn.ambermoe.nine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ASUS on 2017-04-16.
 */

public class StreamTools {
    public Bitmap getImage(String uri) throws IOException {
        Bitmap bitmap = null;
        URL url=new URL(uri);
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(4000);
        connection.setRequestMethod("GET");
        int code=connection.getResponseCode();
        if(code==200){
            InputStream inputStream=connection.getInputStream();
            //建立一个内存流，用于缓存数据
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte[] buf=new byte[1024];
            int length;
            //当数据读取完毕，退出循环。只要判断读取的数据的长度如果==-1，说明数据已经读完
            length=inputStream.read(buf);
            while(length>-1){
                baos.write(buf, 0, length);
                length=inputStream.read(buf);
            }
            byte[] imgArray = baos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(imgArray, 0, imgArray.length);  //生成位图

            inputStream.close();
            baos.close();
            baos.flush();
        }
        return bitmap;
    }
}
