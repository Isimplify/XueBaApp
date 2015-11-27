package com.dream.Question;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class ModifyContent extends AppCompatActivity{
    private EditText mDetail;
    private String detail;
    private boolean isQuestion;
    private ProgressDialog mDialog;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("修改内容");

        mDetail = (EditText)findViewById(R.id.activity_modify_content_detail);

        id = getIntent().getIntExtra("id", 0);
        isQuestion = getIntent().getBooleanExtra("isQuestion", true);
        detail = getIntent().getStringExtra("content");
        mDetail.setText(detail);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("保存修改");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_sample:
                modifyQuestion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void modifyQuestion(){
        detail = mDetail.getText().toString();
        if (detail.isEmpty()){
            Toast.makeText(ModifyContent.this,"内容不能为空！",Toast.LENGTH_SHORT).show();
        } else {
            if (isQuestion){
                mDialog = ProgressDialog.show(this,"请等待...","正在修改问题...",true);
            } else {
                mDialog = ProgressDialog.show(this,"请等待...","正在修改回答...",true);
            }
            new Thread(commitRunnable).start();
        }
    }

    Runnable commitRunnable = new Runnable() {
        @Override
        public void run() {
            int qid;
            if (isQuestion){
                qid = UserNetUtil.QuestionMofifyByuidqid(GlobalVar.user.getUid(),id,detail);
            } else {
                qid = UserNetUtil.AnswerUpdateAnswer(GlobalVar.user.getUid(),id,detail);
            }
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
            if (qid == UserNetUtil.SERVLET_ERROR){
                Toast.makeText(ModifyContent.this,"服务器异常！",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else {
                if(isQuestion){
                    Toast.makeText(ModifyContent.this,"修改问题成功！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyContent.this,"修改回答成功！",Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
                finish();
            }
        }
    };
}
