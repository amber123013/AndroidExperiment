package cn.ambermoe.androidexperiment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class eduActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu);

        ListView listEdu = (ListView) findViewById(R.id.list_edu);
        listEdu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textEdu = (TextView) view;
                String strEdu = textEdu.getText().toString();
                Intent intent = getIntent();
                intent.putExtra("edu",strEdu);

                eduActivity.this.setResult(1,intent);
                eduActivity.this.finish();;
            }
        });
    }
}
