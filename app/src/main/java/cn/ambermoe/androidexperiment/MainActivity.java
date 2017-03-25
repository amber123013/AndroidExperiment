package cn.ambermoe.androidexperiment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.attr.targetSdkVersion;

public class MainActivity extends AppCompatActivity {
    int pri;
    //被点击按钮引用
    Button clickedButton;
    //计算器显示框
    EditText etInput;

    TextView lb_title;
    boolean[] flags=new boolean[]{false,false,false};//初始复选情况
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lb_title = (TextView) findViewById(R.id.lb_title);
        //拨号按钮监听
        Button button_dial = (Button) findViewById(R.id.button_dial);
        button_dial.setOnClickListener(new View.OnClickListener() {
            //回调函数
            @Override
            public void onClick(View v) {
                lb_title.setText("拨号");
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
                        String callPhoneNumber = ((EditText)findViewById(R.id.call_phone_number)).getText().toString();
                        Uri uri = Uri.parse("tel:" + callPhoneNumber);
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
                    }
                });
            }
        });
        //主页面短信按钮
        Button button_msg = (Button) findViewById(R.id.button_msg);
        button_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutMsg();

                Button msg = (Button) findViewById(R.id.msg);
                msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获得号码
                        String phoneNumber = ((EditText)findViewById(R.id.msg_phone_number)).getText().toString();

                        EditText msg = (EditText)findViewById(R.id.msg_phone_content);
                        //获得短信内容
                        String phoneContent = msg.getText().toString();
                        //获取短信管理器
                        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                        //拆分短信内容
                        List<String> msgContents = smsManager.divideMessage(phoneContent);
                        for(String text: msgContents) {
                            smsManager.sendTextMessage(phoneNumber,null,text,null,null);
                        }

                        Toast.makeText(MainActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                        //清空短信框
                        msg.setText("");

                    }
                });
            }
        });

        //联系人按钮
        Button button_contact = (Button) findViewById(R.id.button_contact);
        button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,ContactActivity.class);
                //请求码2
                startActivityForResult(intent,2);
            }
        });
        //个人中心
        Button button_personal_center = (Button) findViewById(R.id.button_personal_center);
        button_personal_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lb_title.setText("个人中心");
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
                frameLayout.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.personalcenter_layout,frameLayout);
                //登录按钮
                ImageButton buttonLogin = (ImageButton) findViewById(R.id.button_login);
                buttonLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lb_title.setText("登录");
                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
                        frameLayout.removeAllViews();
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        inflater.inflate(R.layout.login_layout,frameLayout);
                    }
                });
                //注册按钮
                ImageButton buttonRegister = (ImageButton) findViewById(R.id.button_register);
                buttonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lb_title.setText("注册");
                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
                        frameLayout.removeAllViews();
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        inflater.inflate(R.layout.register_layout,frameLayout);
                        //获取学历按钮
                        Button buttonEducation = (Button) findViewById(R.id.getEducation);
                        buttonEducation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,eduActivity.class);
                                //request
                                startActivityForResult(intent,1);
                            }
                        });
                        //性别
                        TextView radioText = (TextView) findViewById(R.id.radio_text);
                        radioText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("性别");
                                final String[] items = new String[]{"男","女"};
                                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       switch (which) {
                                           case 0:
                                               ((RadioButton)findViewById(R.id.sex_man)).setChecked(true);
                                               break;
                                           case 1:
                                               ((RadioButton)findViewById(R.id.sex_woman)).setChecked(true);
                                               break;
                                       }
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                            }
                        });
                        RadioGroup sexGroup = (RadioGroup) findViewById(R.id.sex_group);
                        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton rbSex = (RadioButton) findViewById(checkedId);
                                String strSex = rbSex.getText().toString();
                                Log.v("MainActivity-sex",strSex);
                                Toast.makeText(MainActivity.this,strSex,Toast.LENGTH_SHORT).show();
                            }
                        });
                        //爱好
                        TextView checkBoxText = (TextView)findViewById(R.id.checkbox_text);

                        checkBoxText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

                                //设置对话框的标题
                                builder.setTitle("复选框对话框");
                                final String[] items = new String[]{"游泳","舞蹈","唱歌"};
                                builder.setMultiChoiceItems(items, flags, new DialogInterface.OnMultiChoiceClickListener(){
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        flags[which]=isChecked;
                                    }
                                });
                                //添加一个确定按钮
                                builder.setPositiveButton(" 确 定 ", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) {
                                        //设置主界面选中
                                        if(flags[0])
                                                ((CheckBox) findViewById(R.id.cb_swim)).setChecked(true);
                                        else
                                            ((CheckBox) findViewById(R.id.cb_swim)).setChecked(false);
                                        if(flags[1])
                                            ((CheckBox) findViewById(R.id.cb_dance)).setChecked(true);
                                        else
                                            ((CheckBox) findViewById(R.id.cb_dance)).setChecked(false);
                                        if(flags[2])
                                            ((CheckBox) findViewById(R.id.cb_sing)).setChecked(true);
                                        else
                                            ((CheckBox) findViewById(R.id.cb_sing)).setChecked(false);
                                    }
                                });
                                //创建一个复选框对话
                                builder.create().show();
                            }
                        });

                        //专业选择
                        ExpandableListView listView = (ExpandableListView) findViewById(R.id.list);
                        MyExpandableListAdapter adapter = new MyExpandableListAdapter(MainActivity.this,listView);
                        listView.setAdapter(adapter);
                        //免费注册按钮
                        Button button = (Button) findViewById(R.id.register_button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                findViewById(R.id.register_content).setVisibility(View.GONE);
                                View progress  = findViewById(R.id.progress_box);
                                progress.setVisibility(View.VISIBLE);
                                // 创建进度对话框
                                final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                                pd.setMax(100);
                                // 设置对话框的标题
                                pd.setTitle("注册中");
                                // 设置对话框 显示的内容
                                pd.setMessage("注册完成百分比");
                                // 设置对话框不能用“取消”按钮关闭
                                pd.setCancelable(false);
                                // 设置对话框的进度条风格
                                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                // 设置对话框的进度条是否显示进度(false是显示
                                pd.setIndeterminate(false);
                                pd.show();
                                Thread thread = new Thread(){
                                    int count=0;
                                    public void run(){

                                        while(count<100){
                                            pd.setProgress(count++);
                                            try {
                                                sleep(20);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        pd.dismiss();
                                    }
                                };
                                thread.start();
                        }
                        });
                    }
                });
            }
        });
    }

    /**
     * 切换到短信页面
     */
    private void checkoutMsg() {
        lb_title.setText("短信");
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
        //清除FrameLayout中的视图
        frameLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //flate 出内容视图 并添加到FramLayout中
        inflater.inflate(R.layout.msg_layout, frameLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == 1){
            String edu = data.getStringExtra("edu");
            ((EditText)findViewById(R.id.eduEditText)).setText(edu);
        }
        //ContactActivity的返回
        if(requestCode == 2 && resultCode == 2) {
            //切换到短信页面
            checkoutMsg();
            //获取电话号码，并放入电话号码框
            String phoneNumber = data.getStringExtra("phoneNumber");
            ((EditText)findViewById(R.id.msg_phone_number)).setText(phoneNumber);
        }
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
