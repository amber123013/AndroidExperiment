package cn.ambermoe.androidexperiment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ASUS on 2017-03-17.
 */

/**
 * 继承ArrayAdapter 并重写getView方法
 * 为ListView生成自定义的listitem （contact_list_item）
 * 并对listitem中的两个ImageButton设置监听
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 检查是否已经有可以重用的列表项视图（称为 convertView），
        // 否则，如果 convertView 为 null，则 inflate 一个新列表项布局。
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_list_item, parent, false);
        }
        //为当前列表项提供信息的Contact 对象
        final Contact currentContact =  getItem(position);

        //姓
        TextView textSurname = (TextView) listItemView.findViewById(R.id.textView_surname);
        textSurname.setText(currentContact.getName().substring(0,1));

        /**
         * /为姓氏背景圆圈设置一个的背景颜色。
         * 从 TextView 获取背景，该背景是一个 GradientDrawable。
         * 颜色资源 ID 仅指向我们定义的资源，而非颜色的 值
         *调用 ContextCompat getColor() 以将颜色资源 ID 转换为 实际整数颜色值
         */
        GradientDrawable surNameCircle = (GradientDrawable) textSurname.getBackground();
        //设置背景圈中颜色
        surNameCircle.setColor(ContextCompat.getColor(getContext(), currentContact.getSurnameColor()));
        //名字
        TextView textName = (TextView) listItemView.findViewById(R.id.textView_name);
        textName.setText(currentContact.getName());
        //职业信息
        TextView textMessage = (TextView) listItemView.findViewById(R.id.textView_msg);
        textMessage.setText(currentContact.getMessage());
        //发送信息的ImageButton
        ImageButton msgButton = (ImageButton) listItemView.findViewById(R.id.imageButton_msg);
        /**
         * 设置监听，当点击发送信息按钮时
         * 返回并跳转至发送信息页面
         * 将返回的电话号码放入 电话号码框中
         */
        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得电话号码
                String strPhoneNumber = currentContact.getPhoneNumber();
                //将ContactAdapter初始化时传入的（Activity）
                // context 强制在转换成 ContactActivity
                Activity contactActivity = ((ContactActivity)getContext());
                Intent intent = contactActivity.getIntent();
                intent.putExtra("phoneNumber",strPhoneNumber);
                //返回码设置为2
                contactActivity.setResult(2,intent);
                contactActivity.finish();
            }
        });
        //拨打电话的ImageBUtton
        ImageButton phoneButton = (ImageButton) listItemView.findViewById(R.id.imageButton_phone);
        /**
         * 设置拨号ImageButton监听
         * 当点击图片按钮时，直接拨打该Contact中的电话号码
         */
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity contactActivity = ((ContactActivity)getContext());
                //直接拨打电话
                Uri uri = Uri.parse("tel:" + currentContact.getPhoneNumber());
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if(PackageManager.PERMISSION_GRANTED == 0)
                    contactActivity.startActivity(intent);
            }
        });
        return listItemView;
    }
}
