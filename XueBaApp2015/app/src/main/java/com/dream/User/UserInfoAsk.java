package com.dream.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dream.Entity.Question;
import com.dream.Question.QuestionDetails;
import com.dream.XueBaApp2015.R;

import java.util.ArrayList;
import java.util.List;

public class UserInfoAsk extends AppCompatActivity{
    private List<Question> mQuestions;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_ask);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("我提出的问题");
        actionBar.setDisplayHomeAsUpEnabled(true);
        mQuestions = UserNetUtil.SelectQidByUid(GlobalVar.user.getUid());
        ListView askList = (ListView) findViewById(R.id.activity_user_info_ask_list);
        askList.setAdapter(new ArrayAdapter<String>(UserInfoAsk.this, android.R.layout.simple_list_item_1, getData()));
        askList.setDividerHeight(1);
        askList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int qid = mQuestions.get(position).Id;
                Intent intent = new Intent(UserInfoAsk.this, QuestionDetails.class);
                intent.putExtra("id",qid);
                startActivity(intent);
            }
        });
    }


    private String[] getData(){
        String[] titles = new String[]{};
        if (mQuestions!=null){
            titles = new String[mQuestions.size()];
            for (int i = 0; i < mQuestions.size(); i++) {
                titles[i]=mQuestions.get(i).Title;
            }
        }
        return titles;
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
