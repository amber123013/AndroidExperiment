package cn.ambermoe.nine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<String>();
    public String BASE_URI = "http://m.weather.com.cn/img/";
    public String END_URI;
    private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.weather_text);
        list.add("晴");
        list.add("多云");
        list.add("阴");
        list.add("阵雨");
        list.add("雷阵雨");
        list.add("雷阵雨伴有冰雹");
        list.add("雨夹雪");
        list.add("小雨");
        final Spinner spinner = (Spinner)findViewById(R.id.weather_list);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String str = ((TextView)spinner.getAdapter().getItem(position)).getText().toString();
                  textView.setText(list.get(position));
                END_URI = "b" + position + ".gif";
                new Thread(runnable).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imageView = (ImageView) findViewById(R.id.img);
    }
    //子线程 获取网络图片
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Message msg = new Message();
                StreamTools streamTools = new StreamTools();
                Bitmap bitmap = streamTools.getImage(BASE_URI+END_URI);
                msg.obj = bitmap;
                handler.sendMessage(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    /**
     * 消息处理器，负责消息的处理
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
           // Color.FromArgb(0, Color.Black.ToArgb());
            imageView.setImageBitmap(bitmap);
        }
    };
}
