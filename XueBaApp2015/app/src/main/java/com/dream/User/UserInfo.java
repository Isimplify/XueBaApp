package com.dream.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dream.XueBaApp2015.R;

public class UserInfo extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("个人信息");
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView detailTV = (TextView)findViewById(R.id.activity_user_info_detail);
        TextView askTV = (TextView)findViewById(R.id.activity_user_info_myAsk);
        TextView answerTV = (TextView)findViewById(R.id.activity_user_info_myAnswer);
        TextView fileTV = (TextView)findViewById(R.id.activity_user_info_myFile);
        detailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this, UserInfoDetail.class));
            }
        });
        askTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this,UserInfoAsk.class));
            }
        });
        answerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this,UserInfoAnswer.class));
            }
        });
        fileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this,UserInfoFile.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
