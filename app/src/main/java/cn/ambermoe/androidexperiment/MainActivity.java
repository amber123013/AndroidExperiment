package cn.ambermoe.androidexperiment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import static android.R.attr.targetSdkVersion;

public class MainActivity extends AppCompatActivity {
    int pri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //拨号按钮监听
        Button button_dial = (Button) findViewById(R.id.button_dial);
        button_dial.setOnClickListener(new View.OnClickListener() {
            //回调函数
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
                //清除FrameLayout中的视图
                frameLayout.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //flate 出内容视图 并添加到FramLayout中
                inflater.inflate(R.layout.dial_layout, frameLayout);


                Button dial = (Button) findViewById(R.id.dial);
                dial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("tel:" + "1875980871");
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        //Toast.makeText(MainActivity.this,"请启用允许拨打电话权限",Toast.LENGTH_SHORT).show();
                        //ContextCompat.checkSelfPermission() 用于检查权限
                        //有返回PERMISSION_GRANTED
                        //无返回PERMISSION_DENIED PermissionChecker
                        /**
                         * 在targetSdkVersion小于23(Android M)的时候
                         * ContextCompat.CheckSelfPermission 和Context.checkSelfPermission方法
                         * 都不能正常工作并且始终返0(PERMISSION_GRANTED)
                         */
                        if(targetSdkVersion >= Build.VERSION_CODES.M) {
                           pri =  ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
                            Toast.makeText(MainActivity.this, ">=23", Toast.LENGTH_SHORT).show();
                        } else {
                            //targetSdkVersion < 23 时调用 PermissionChecker方法
                            pri = PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
                        }

                        switch (pri) {
                            case PackageManager.PERMISSION_GRANTED:
                                startActivity(intent);
                                break;
                            case PackageManager.PERMISSION_DENIED:
                                //拨打电话权限不可用，开启选择设置对话框
                                showDialogTipUserGoToAppSettting();
                                break;
                        }
//                        if (PackageManager.PERMISSION_GRANTED != pri) {
//                            //权限可用启动 使用intent拨打电话
//
//
//                        } else {
//                            //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 321);
//                            // Toast.makeText(MainActivity.this, "请启用允许拨打电话权限", Toast.LENGTH_SHORT).show();
//
//
//
//                        }

                    }
                });
            }
        });
    }

    /**
     * 启用一个AlertDialog对话框
     * 当拨打电话权限不可用时 开启选择
     * 立即开启 --> 跳转到设置页面
     * 取消     --> 放弃操作
     */
    private void showDialogTipUserGoToAppSettting() {

        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("电话权限不可用")
                .setMessage("请在-应用设置-权限-中，允许应用程序使用拨打电话权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

}
