package com.dream.Question;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class AddAnswer extends AppCompatActivity{
    private EditText mEditText;
    private int qid;
    private String content;
    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("回答问题");

        mEditText=(EditText)findViewById(R.id.activity_add_answer_edittext);
        Intent intent = getIntent();
        qid = intent.getIntExtra("qid",0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        menuItem.setTitle("提交答案");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_sample:
                if(qid!=0)
                    handOnAnswer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handOnAnswer(){
        content=mEditText.getText().toString();
        if (content.isEmpty()){
            Toast.makeText(AddAnswer.this,"答案不能为空！",Toast.LENGTH_SHORT).show();
        } else {
            mDialog=ProgressDialog.show(this,"请等待...","正在提交答案...",true);
            new Thread(commitRunnable).start();
        }
    }

    Runnable commitRunnable=new Runnable() {
        @Override
        public void run() {
            int cid = UserNetUtil.GiveAnswer(qid, GlobalVar.user.getUid(),content);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("cid",cid);
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
            int cid = data.getInt("cid");
            if (cid == UserNetUtil.SERVLET_ERROR){
                Toast.makeText(AddAnswer.this,"服务器异常！",Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else {
                Toast.makeText(AddAnswer.this,"回答成功，奖励您10积分！您当前的积分为"+GlobalVar.user.getDownloadCredit(),Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                finish();
            }
        }
    };
}
