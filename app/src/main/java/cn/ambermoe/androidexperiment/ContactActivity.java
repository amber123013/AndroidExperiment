package cn.ambermoe.androidexperiment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    //适配器引用
    private ContactAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ListView contactListView = (ListView) findViewById(R.id.contact_list);
        //实例化适配器
        mAdapter = new ContactAdapter(this,getContactList());
        //设置为ListView的适配器
        contactListView.setAdapter(mAdapter);

    }

    private List<Contact> getContactList() {
        List<Contact>  contactList = new ArrayList<>();
        contactList.add(new Contact("平陵","行政审批部门 文案科","18065853729",R.color.surname2));
        contactList.add(new Contact("凌泽旭","行政部 部员","18065853729",R.color.surname3));
        contactList.add(new Contact("福建爱康","办公室部门 美工组","18065853729",R.color.surname1));
        contactList.add(new Contact("平徐颖","行政审批部门 信息科","18065853729",R.color.surname4));
        contactList.add(new Contact("于二图","办公室部门 美工组","18065853729",R.color.surname2));
        contactList.add(new Contact("阚宝佳","人事部","18065853729",R.color.surname1));
        contactList.add(new Contact("碑林","办公室部门 前台","18065853729",R.color.surname3));
        contactList.add(new Contact("凌涛","美工组","18065853729",R.color.surname5));
        contactList.add(new Contact("傅玉斌","自由职业","18065853729",R.color.surname1));
        contactList.add(new Contact("潘佳运","办公室部门 美工组","18065853729",R.color.surname4));
        contactList.add(new Contact("徐琳","行政审批部门 信息科","18065853729",R.color.surname2));
        contactList.add(new Contact("洪家肯","行政审批部门 文案科","18065853729",R.color.surname3));
        contactList.add(new Contact("乌尔林","自由职业","18065853729",R.color.surname5));
        return contactList;
    }
}
