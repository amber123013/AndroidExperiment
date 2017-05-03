package cn.ambermoe.weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ambermoe.weather.utils.Weather;

/**
 * Created by ASUS on 2017-05-02.
 * 适配器 使用viewholder 为 recycleview提供数据
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder>{

    //今天
    private static final int VIEW_TYPE_TODAY = 0;
    //之后
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    /**
     * 所有需显示的天气信息
     */
    private Weather[] mWeatherData;


    public WeatherAdapter() {

    }
    /**
     *实现 viewholder
     *保存textview的引用 之后赋值时从这里拿
     * 节省 使用finviewbyid的开销
     */
    public class WeatherAdapterViewHolder extends RecyclerView.ViewHolder{
        //列表项中 视图
         ImageView iconView;
         TextView dateView;
         TextView descriptionView;
         TextView highTempView;
         TextView lowTempView;
         TextView locationView;
        public WeatherAdapterViewHolder(View view) {
            super(view);
            //获取
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);

            if(view.findViewById(R.id.location) != null) {
                locationView = (TextView) view.findViewById(R.id.location);
            }
        }
    }

    /**
     * 创建ViewHolder 直到足够 滚动使用
     * @param parent 包含创建的view
     * @param viewType  支持不同类型的视图（也就是说每个列表项可以是不同的视图）
     * @return  一个WeatherAdapterViewHolder
     */
    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId;
        switch (viewType) {
            //第一项
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_today;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new WeatherAdapterViewHolder(view);
    }

    /**
     * 绑定recycleview每一项的position位置显示的内容
     * @param holder 显示的项
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(WeatherAdapterViewHolder holder, int position) {
        Weather weather = mWeatherData[position];
        //绑定改项位置显示的内容
        holder.dateView.setText(weather.getDate());
        holder.descriptionView.setText(weather.getDescription());
        holder.highTempView.setText(String.format("%1.0f°",weather.getMax()));
        holder.lowTempView.setText(String.format("%1.0f°",weather.getMin()));
        holder.iconView.setImageResource(weather.getImgResourceId());
        if(position == 0) {
            holder.locationView.setText(weather.getLocation());
        }
    }

    /**
     * 根据所在的项数选择不同的视图
     * 第一个视图（本日）用更大的视图显示
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    /**
     * 总共的项数
     * @return 项数
     */
    @Override
    public int getItemCount() {
        if(mWeatherData == null)
            return 0;
        return mWeatherData.length;
    }
    //设置适配器使用的数据
    public void setWeatherData(Weather[] data) {
        this.mWeatherData = data;
        //通知适配器内容发生了变化
        notifyDataSetChanged();
    }
}
