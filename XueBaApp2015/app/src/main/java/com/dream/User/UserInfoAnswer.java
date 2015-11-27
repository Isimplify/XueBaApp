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

public class UserInfoAnswer extends AppCompatActivity{
    private List<Question> mAnsweredQuestions;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setTitle("我回答的问题");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ListView answerList = (ListView) findViewById(R.id.activity_user_info_answer_list);
        mAnsweredQuestions = UserNetUtil.SelectAnqidByUid(GlobalVar.user.getUid());
        if (mAnsweredQuestions!=null){
            answerList.setAdapter(new ArrayAdapter<String>(UserInfoAnswer.this,android.R.layout.simple_list_item_1,getData()));
        }

        answerList.setDividerHeight(1);
        answerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int qid = mAnsweredQuestions.get(position).Id;
                Intent intent = new Intent(UserInfoAnswer.this, QuestionDetails.class);
                intent.putExtra("id",qid);
                startActivity(intent);
            }
        });
    }



    private String[] getData(){
        String[] titles = new String[]{};
        if (mAnsweredQuestions!=null){
            titles = new String[mAnsweredQuestions.size()];
            for (int i = 0; i < mAnsweredQuestions.size(); i++) {
                titles[i]=mAnsweredQuestions.get(i).Title;
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
