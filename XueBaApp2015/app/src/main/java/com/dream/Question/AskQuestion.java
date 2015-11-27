package com.dream.Question;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dream.User.GlobalVar;
import com.dream.User.UserNetUtil;
import com.dream.XueBaApp2015.R;

import java.util.ArrayList;
import java.util.List;

public class AskQuestion extends AppCompatActivity{
    private EditText mTitle;
    private EditText mDetail;
    private TextInputLayout mTag1;
    private TextInputLayout mTag2;
    private TextInputLayout mTag3;
    private TextInputLayout mTag4;
    private String title;
    private String detail;
    private String tag;
    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("提问");
        mTitle=(EditText)findViewById(R.id.activity_ask_question_title);
        mDetail=(EditText)findViewById(R.id.activity_ask_question_detail);
        mTag1=(TextInputLayout)findViewById(R.id.activity_ask_question_tag1);
        mTag2=(TextInputLayout)findViewById(R.id.activity_ask_question_tag2);
        mTag3=(TextInputLayout)findViewById(R.id.activity_ask_question_tag3);
        mTag4=(TextInputLayout)findViewById(R.id.activity_ask_question_tag4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("提交问题");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_sample:
                handOnQuestion();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handOnQuestion(){
        title = mTitle.getText().toString();
        detail = mDetail.getText().toString();
        List<String> tags = new ArrayList<String>();
        if (!mTag1.getEditText().getText().toString().isEmpty())
            tags.add(mTag1.getEditText().getText().toString());
        if (!mTag2.getEditText().getText().toString().isEmpty())
            tags.add(mTag2.getEditText().getText().toString());
        if (!mTag3.getEditText().getText().toString().isEmpty())
            tags.add(mTag3.getEditText().getText().toString());
        if (!mTag4.getEditText().getText().toString().isEmpty())
            tags.add(mTag4.getEditText().getText().toString());
        tag = tags.toString().replace("[","").replace("]","");
        if (title.isEmpty()){
            Toast.makeText(AskQuestion.this,"问题标题不能为空！",Toast.LENGTH_SHORT).show();
        } else if (detail.isEmpty()){
            Toast.makeText(AskQuestion.this,"问题详细描述不能为空！",Toast.LENGTH_SHORT).show();
        } else {
            mDialog = ProgressDialog.show(this,"请等待..","正在提交问题...",true);
            new Thread(commitRunnable).start();
        }
    }

    Runnable commitRunnable = new Runnable() {
        @Override
        public void run() {
            int qid = UserNetUtil.Ask(GlobalVar.user.getUid(),title,detail,tag);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("qid",qid);
            msg.setData(data);
            commitHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler commitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int qid = data.getInt("qid");
            if(qid == UserNetUtil.SERVLET_ERROR){
                Toast.makeText(AskQuestion.this,"服务器异常！",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else {
                Toast.makeText(AskQuestion.this,"提问成功，奖励您5积分！您当前的积分为"+GlobalVar.user.getDownloadCredit(),Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                finish();
            }
        }
    };
}
