package com.dream.User;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dream.XueBaApp2015.R;

public class UserInfoDetail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_detail);
        TextView emailTV = (TextView) findViewById(R.id.activity_user_info_detail_email);
        TextView nickTV = (TextView) findViewById(R.id.activity_user_info_detail_nick);
        TextView nameTV = (TextView) findViewById(R.id.activity_user_info_detail_name);
        TextView signTV = (TextView) findViewById(R.id.activity_user_info_detail_sign);
        TextView creditTV = (TextView) findViewById(R.id.activity_user_info_detail_credit);
        TextView rankTV = (TextView) findViewById(R.id.activity_user_info_detail_rank);
        TextView titleTV = (TextView) findViewById(R.id.activity_user_info_detail_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("详细资料");
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (GlobalVar.user!=null) {
            emailTV.setText(GlobalVar.user.getEmail());
            nickTV.setText(GlobalVar.user.getNick());
            if (GlobalVar.user.getRealName().isEmpty()){
                nameTV.setText("");
            } else {
                nameTV.setText(GlobalVar.user.getRealName());
            }
            if (GlobalVar.user.getDescription().isEmpty()){
                signTV.setText("");
            } else {
                signTV.setText(GlobalVar.user.getDescription());
            }
            creditTV.setText(String.valueOf(GlobalVar.user.getDownloadCredit()));
            rankTV.setText(String.valueOf(GlobalVar.user.getRank()));
            titleTV.setText(GlobalVar.user.getTitle());
        }
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
