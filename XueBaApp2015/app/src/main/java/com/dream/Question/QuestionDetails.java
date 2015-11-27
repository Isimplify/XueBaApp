package com.dream.Question;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.Entity.Question;
import com.dream.User.GlobalVar;
import com.dream.User.UserNetUtil;
import com.dream.XueBaApp2015.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionDetails extends AppCompatActivity {
    private int qid;
    private RecyclerView mRecyclerView;
    private ImageButton mButton2;
    private Question mQuestion;
    private ProgressDialog m_Dialog;
    public static final int SEARCH_VOTE = 2;

    // 该线性表用来记录用户对每个问题的点赞情况
    private List<Boolean> answers_isVoted = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("问题详情");
        mButton2 = (ImageButton) findViewById(R.id.activity_question_detail_editButton);
    }

    public void init(){
        qid = getIntent().getIntExtra("id", 0);
        new Thread(new Runnable() {
            public void run() {
                QuestionsNetUtil.addViews(qid);
            }
        }).start();
        m_Dialog = ProgressDialog.show(this, "请等待...", "正在获取数据...", true);
        Thread mThread = new Thread(runnable);
        mThread.start();
        setListener();
    }

    private void setListener(){

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(QuestionDetails.this,
                        ModifyContent.class);
                intent.putExtra("id", qid);
                intent.putExtra("isQuestion", true);
                intent.putExtra("content", mQuestion.Content);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_Dialog.dismiss();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() { // 这里是对请求结果的处理
        @Override
        public void handleMessage(Message msg) { // 收到信息
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean success = data.getBoolean("success");
            if (success) {
                TextView mTitle = (TextView) findViewById(R.id.activity_question_detail_title);
                mTitle.setText(mQuestion.Title);
                TextView mContent = (TextView) findViewById(R.id.activity_question_detail_details);
                mContent.setText(mQuestion.Content);
                TextView mInfo = (TextView) findViewById(R.id.activity_question_detail_author);
                if (mQuestion.Poste != null) {
                    mInfo.setText("作者：" + mQuestion.Poste.getNick());
                } else {
                    mInfo.setText("作者：" + "匿名");
                }
                if (mQuestion.Solved) {
                    ImageView mImageView = (ImageView) findViewById(R.id.activity_question_detail_solvedPic);
                    mImageView.setVisibility(View.VISIBLE);
                }
                if (mQuestion.Solved || GlobalVar.user == null
                        || GlobalVar.user.getUid() != mQuestion.Poste.getUid()) {

                    mButton2.setVisibility(View.INVISIBLE);

                    mButton2.setEnabled(false);
                }

                mRecyclerView = (RecyclerView) findViewById(R.id.activity_question_detail_RView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(QuestionDetails.this);
                mRecyclerView.setLayoutManager(layoutManager);

                /**--------------------------------***/

                setListAdapter();
            }
            m_Dialog.dismiss(); // 最后让带进度条的对话框消失
        }

    };



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            mQuestion = QuestionsNetUtil.getQuestionById(qid);

            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            if (mQuestion != null) {
                data.putBoolean("success", true);

                answers_isVoted = new ArrayList<Boolean>();
                if (GlobalVar.isSigned) {
                    for (int i = 0; i < mQuestion.Answers.size(); i++) {
                        answers_isVoted.add(UserNetUtil.GiveVote(
                                mQuestion.Answers.get(i).Id,
                                GlobalVar.user.getUid(),
                                SEARCH_VOTE));
                    }
                } else {
                    for (int i = 0; i < mQuestion.Answers.size(); i++) {
                        answers_isVoted.add(false);
                    }
                }
            } else {
                data.putBoolean("success", false);
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_sample);
        if(mQuestion.Solved)
            menuItem.setTitle("");
        else
            menuItem.setTitle("回答");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // 实现返回效果
                return true;
            case R.id.menu_sample:
                if (!GlobalVar.isSigned) {
                    Toast.makeText(getApplicationContext(), "你还没有登录！请登录后再回答",
                            Toast.LENGTH_SHORT).show();

                } else {
                    if (qid != 0 && mQuestion != null) {
                        //mQuestion这东西一开始不是null么？
                        addAnswer();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListAdapter() {
        mRecyclerView.setAdapter(new AnswerItemAdapter(this,mQuestion,answers_isVoted));
    }



    private void addAnswer() {
        Intent intent = new Intent(QuestionDetails.this, AddAnswer.class);
        intent.putExtra("qid", qid);
        startActivity(intent);
    }

    // 回调函数
    public void callBack() {
        // 由于是SingleTask，所以要先finish掉才能实现刷新
        finish();
        Intent intent = new Intent(QuestionDetails.this, QuestionDetails.class);
        intent.putExtra("id", qid);
        startActivity(intent);
//        onStart();

    }
}
