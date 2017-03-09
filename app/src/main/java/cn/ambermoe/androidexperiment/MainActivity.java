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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
                lb_title.setText("短信");
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
                //清除FrameLayout中的视图
                frameLayout.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //flate 出内容视图 并添加到FramLayout中
                inflater.inflate(R.layout.msg_layout, frameLayout);


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
        //计算按钮
        Button button_calc = (Button) findViewById(R.id.button_calc);
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lb_title.setText("计算器");
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content);
                //清除FrameLayout中的视图
                frameLayout.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //flate 出内容视图 并添加到FramLayout中
                inflater.inflate(R.layout.calc_layout, frameLayout);
                //计算结果框
                etInput = (EditText)findViewById(R.id.et_input);
                etInput.setText("");

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
                        CheckBox cbSwim = (CheckBox) findViewById(R.id.cb_swim);
                        cbSwim.setOnCheckedChangeListener(new CheckBoxListenner());
                        CheckBox cbDance = (CheckBox) findViewById(R.id.cb_dance);
                        cbDance.setOnCheckedChangeListener(new CheckBoxListenner());
                        CheckBox cbSing = (CheckBox) findViewById(R.id.cb_sing);
                        cbSing.setOnCheckedChangeListener(new CheckBoxListenner());
                        //专业选择
                        Spinner spMajor = (Spinner) findViewById(R.id.sp_major);
                        spMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView textMajor = (TextView) view;
                                String strMajor = textMajor.getText().toString();
                                Log.v("MainActivity",strMajor);
                                Toast.makeText(MainActivity.this,strMajor,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
            }
        });
    }
    private class CheckBoxListenner implements android.widget.CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.cb_swim:
                    if (isChecked) {
                        Log.v("MainActivity", buttonView.getText().toString());
                        Toast.makeText(MainActivity.this, buttonView.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                        break;
                case R.id.cb_dance:
                    if(isChecked) {
                        Log.v("MainActivity", buttonView.getText().toString());
                        Toast.makeText(MainActivity.this, buttonView.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                        break;
                case R.id.cb_sing:
                    if(isChecked) {
                        Log.v("MainActivity", buttonView.getText().toString());
                        Toast.makeText(MainActivity.this, buttonView.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                        break;
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == 1){
            String edu = data.getStringExtra("edu");
            ((EditText)findViewById(R.id.eduEditText)).setText(edu);
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
    //计算器功能实现
    public void setEtInputValue(View v) {
        Button clickedButton = (Button)v;
        char btValue = ((Button) v).getText().toString().trim().charAt(0);
        //计算框中的值
        String tempValue = etInput.getText().toString();
        //框中的最后一个字符
        char lastWord = ' ';
        if(!"".equals(tempValue))
            lastWord = tempValue.charAt(tempValue.length() - 1);
        switch (btValue) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '0':
                etInput.setText(etInput.getText().toString() + btValue);
                break;
            case '+':
            case '-':
            case '*':
            case '/':
                //运算符之前不能是运算符和空字符
                if(lastWord != '+' && lastWord !='-' && lastWord !='*' && lastWord != '/' && tempValue != "") {
                    etInput.setText(etInput.getText().toString() + btValue);
                } else {
                    Toast.makeText(MainActivity.this,"请正确输入计算式",Toast.LENGTH_SHORT).show();
                }
                break;
            case '=':
                //=号之前不能是运算符
                if(lastWord !='+' && lastWord !='-' && lastWord !='*' && lastWord != '/') {
                    etInput.setText(Expression.toValue(Expression.toPostfix(etInput.getText().toString())) + "");
                } else{
                    Toast.makeText(MainActivity.this,"你确定输入完全了吗？",Toast.LENGTH_SHORT).show();
                }
                break;
            case '.':
                //小数点前不能是空 运算符 和它自身
                if(!"".equals(tempValue) && (lastWord != '+' && lastWord !='-' && lastWord !='*' && lastWord != '/' && lastWord !='.'))
                    etInput.setText(etInput.getText().toString() + btValue);
                else
                    Toast.makeText(MainActivity.this,"是不是哪里输错了呢？",Toast.LENGTH_SHORT).show();
                break;
            case 'C':
                etInput.setText("");
                break;
            case '←':
                //删除最后一个字符，如果删除之后最后为 ‘.’ 则删除它
                tempValue = etInput.getText().toString();
                etInput.setText(tempValue.substring(0,tempValue.length() -1));
                tempValue = etInput.getText().toString();
                if(!"".equals(tempValue) && tempValue.charAt(tempValue.length() -1) == '.')
                    etInput.setText(tempValue.substring(0,tempValue.length() -1));
                break;
        }
    }

}
