package com.dream.Question;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.Entity.Question;
import com.dream.XueBaApp2015.R;

import java.util.ArrayList;
import java.util.List;

public class BestQuestionList extends Fragment{
    private RecyclerView mQuestionListView;
    private List<Question> mQuestionList;
    private ProgressDialog m_Dialog;
    private int VIEW_COUNT = 10;// 每页显示个数
    private int index = 0;// 用于显示页号的索引
    private TextView loadPrevBtn;
    private TextView loadNextBtn;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionlist, container, false);
        loadPrevBtn=(TextView)rootView.findViewById(R.id.load_prev_page_textview);
        loadNextBtn=(TextView)rootView.findViewById(R.id.load_next_page_textview);
        mQuestionListView = (RecyclerView) rootView.findViewById(R.id.fragment_questionlist_list);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        mQuestionList=new ArrayList<Question>();
        mQuestionListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setListener();
        m_Dialog = ProgressDialog.show(getActivity(), "请等待...", "正在载入热门问题...",
                true);
        Thread mThread = new Thread(runnable);
        mThread.start();
    }

    private void setListener() {
        loadPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                init();
            }
        });
        loadNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                init();
            }
        });

    }

    private void checkBtn(){
        if(index<=0){
            loadPrevBtn.setVisibility(View.GONE);
            loadNextBtn.setVisibility(View.VISIBLE);
        } else if(mQuestionList==null||mQuestionList.size()<VIEW_COUNT){
            loadPrevBtn.setVisibility(View.VISIBLE);
            loadNextBtn.setVisibility(View.GONE);
        }else{
            loadPrevBtn.setVisibility(View.VISIBLE);
            loadNextBtn.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() { // 这里是对请求结果的处理
        @Override
        public void handleMessage(Message msg) { // 收到信息
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean success = data.getBoolean("success");

            if (success) {
                setListAdapter();
            } else {
                Toast.makeText(getActivity(), "服务器异常！无法加载问题列表", Toast.LENGTH_SHORT)
                        .show();
            }
            checkBtn();
            m_Dialog.dismiss(); // 最后让带进度条的对话框消失
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mQuestionList = QuestionsNetUtil.getBestSolutions(0, index + 1,
                    1);
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            if (mQuestionList != null) {
                data.putBoolean("success", true);
            } else {//如果本来就没数据怎么办
                data.putBoolean("success", false);
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    private void setListAdapter() {
        QuestionItemAdapter mAdapter=new QuestionItemAdapter(mQuestionList,getActivity());
        Log.e("index=",index+"");
        mQuestionListView.setAdapter(mAdapter);
    }
}
