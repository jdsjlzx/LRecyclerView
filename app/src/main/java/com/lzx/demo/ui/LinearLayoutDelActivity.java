package com.lzx.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzx.demo.R;

public class LinearLayoutDelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_del);
        LinearLayout llContent = (LinearLayout) findViewById(R.id.llContent);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LinearLayoutDelActivity.this, "内容区域被点击", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LinearLayoutDelActivity.this, "删除按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });


        LinearLayout llContent2 = (LinearLayout) findViewById(R.id.llContent);
        llContent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LinearLayoutDelActivity.this, "第二个内容区域被点击", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnDelete2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LinearLayoutDelActivity.this, "第二个删除按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
