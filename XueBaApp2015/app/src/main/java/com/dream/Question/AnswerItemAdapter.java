package com.dream.Question;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.Entity.Answer;
import com.dream.Entity.Question;
import com.dream.User.GlobalVar;
import com.dream.User.UserNetUtil;
import com.dream.XueBaApp2015.R;

import java.util.ArrayList;
import java.util.List;


public class AnswerItemAdapter extends RecyclerView.Adapter<AnswerItemAdapter.ViewHolder> {
    private Question oneQuestion;
    private QuestionDetails questionDetails;
    private int position;
    private TextView i_a_Best;
    private TextView i_a_Approve;
    private List<Boolean> answer_isVoted;

    public static final int DELETE_VOTE = 0;
    public static final int GIVE_VOTE = 1;
    public static final int SEARCH_VOTE = 2;

    public AnswerItemAdapter(QuestionDetails questionDetails,Question question,List<Boolean> answer_isVoted) {
        super();
        this.questionDetails=questionDetails;
        oneQuestion = question;
        this.answer_isVoted=answer_isVoted;
    }

    public interface OnAnsItemBestClickListener {
        void onIsBestBtnClick(View view, int position);
    }

    public interface OnAnsItemEditClickListener {
        void onEditBtnClick(View view, int position);
    }

    public interface OnAnsItemDeleteClickListener {
        void onDeleteBtnClick(View view, int position);
    }

    public interface OnAnsItemApproveClickListener {
        void onApproveBtnClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(questionDetails).inflate(R.layout.item_answer, parent, false);
        return new ViewHolder(view, new OnAnsItemBestClickListener() {
            @Override
            public void onIsBestBtnClick(View view, int position) {
                AnswerItemAdapter.this.position=position;
                AnswerItemAdapter.this.i_a_Best=(TextView)view;

                if (!oneQuestion.Solved && oneQuestion.Poste.getUid() == GlobalVar.user.getUid()) {
                    new Thread(isBestRunnable).start();
                }
            }
        }, new OnAnsItemEditClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                AnswerItemAdapter.this.position=position;
                Intent intent = new Intent(questionDetails, ModifyContent.class);
                intent.putExtra("id", oneQuestion.Answers.get(position).Id);
                intent.putExtra("isQuestion", false);
                intent.putExtra("content", ((TextView) view).getText());
                questionDetails.startActivity(intent);
            }
        }, new OnAnsItemDeleteClickListener() {
            @Override
            public void onDeleteBtnClick(View view,int position) {
                AnswerItemAdapter.this.position=position;
                AlertDialog dialog = new AlertDialog.Builder(questionDetails)
                        .setTitle("提示")
                        .setMessage("您确定要删除答案？删除答案将扣掉您已经得到的10积分！")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new Thread(deleteAnswerRunnable).start();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .create();
                dialog.show();
            }
        }, new OnAnsItemApproveClickListener() {
            @Override
            public void onApproveBtnClick(View view,int position) {
                AnswerItemAdapter.this.position=position;
                AnswerItemAdapter.this.i_a_Approve=(TextView)view;
                Log.e("Votes",oneQuestion.Answers.get(position).Votes+"");

                if (GlobalVar.isSigned) {
                    if (answer_isVoted.get(position)) {//已经赞过
                        new Thread(deleteVoteRunnable).start();
                    } else {
                        new Thread(giveVoteRunnable).start();
                    }
                } else {
                    Toast.makeText(questionDetails, "您还没有登录！不能点赞！", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    Runnable isBestRunnable=new Runnable() {
        @Override
        public void run() {
            boolean success = QuestionsNetUtil.SolveQuestion(oneQuestion.Id, oneQuestion.Answers.get(position).Id);
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            data.putBoolean("success", success);
            msg.setData(data);
            isBestHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler isBestHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean success = data.getBoolean("success"); // 获得发送的数据
            if (success) {
                i_a_Best.setClickable(false);
                i_a_Best.setText("最佳答案");
                // 调用回调
                questionDetails.init();
                Toast.makeText(questionDetails, "恭喜！该问题已解决！", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(questionDetails, "操作失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    Runnable deleteAnswerRunnable=new Runnable() {
        @Override
        public void run() {
            boolean success = UserNetUtil.AnswerDeleteAnswer(oneQuestion.Answers.get(position).Id, GlobalVar.user.getUid());
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            data.putBoolean("success", success);
            msg.setData(data);
            deleteAnswerHandler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler deleteAnswerHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean success = data.getBoolean("success"); // 获得发送的数据
            if (!success) {
                Toast.makeText(questionDetails, "服务器异常！", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(
                        questionDetails,
                        "删除成功，已扣除您10积分！当前积分为："
                                + GlobalVar.user.getDownloadCredit(),
                        Toast.LENGTH_SHORT).show();
                // 调用回调
                questionDetails.init();
            }
        }
    };
    Runnable deleteVoteRunnable=new Runnable() {
        @Override
        public void run() {
            boolean success = UserNetUtil.GiveVote(oneQuestion.Answers.get(position).Id, GlobalVar.user.getUid(), DELETE_VOTE);
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            data.putBoolean("success", success);
            msg.setData(data);
            deleteVoteHandler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler deleteVoteHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean success = data.getBoolean("success"); // 获得发送的数据
            if (success) {
                oneQuestion.Answers.get(position).Votes--;
                Log.e("Votes", oneQuestion.Answers.get(position).Votes + "");
                i_a_Approve.setText("点个赞");
                answer_isVoted.set(position, false);
                notifyDataSetChanged();
            }
        }
    };
    Runnable giveVoteRunnable=new Runnable() {
        @Override
        public void run() {
            boolean success = UserNetUtil.GiveVote(oneQuestion.Answers.get(position).Id, GlobalVar.user.getUid(), GIVE_VOTE);
            Message msg = new Message(); // 向Handler发送消息
            Bundle data = new Bundle(); // data的载体
            data.putBoolean("success", success);
            msg.setData(data);
            giveVoteHandler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler giveVoteHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean success = data.getBoolean("success"); // 获得发送的数据
            if (success) {
                oneQuestion.Answers.get(position).Votes++;
                i_a_Approve.setText("取消点赞");
                answer_isVoted.set(position, true);
                Log.e("approve text", i_a_Approve.getText().toString());
                notifyDataSetChanged();
            }
        }
    };
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ////
        Answer oneAnswer = oneQuestion.Answers.get(position);
        holder.bind(oneAnswer,position);


    }

    @Override
    public int getItemCount() {
        return oneQuestion.Answers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView i_a_content;
        private TextView i_a_author;
        private TextView i_a_date;
        private TextView i_a_isBest;
        private TextView i_a_edit;
        private TextView i_a_delete;
        private TextView i_a_approve;
        private TextView i_a_vote;

        private OnAnsItemBestClickListener mOnAnsItemBestClickListener;
        private OnAnsItemEditClickListener mOnAnsItemEditClickListener;
        private OnAnsItemDeleteClickListener mOnAnsItemDeleteClickListener;
        private OnAnsItemApproveClickListener mOnAnsItemApproveClickListener;

        private boolean isVoted;

        public ViewHolder(View view,
                          OnAnsItemBestClickListener myOnAnsItemBestClickListener,
                          OnAnsItemEditClickListener myOnAnsItemEditClickListener,
                          OnAnsItemDeleteClickListener myOnAnsItemDeleteClickListener,
                          OnAnsItemApproveClickListener myOnAnsItemApproveClickListener) {
            super(view);

            i_a_content = (TextView) view.findViewById(R.id.item_answer_content);
            i_a_author = (TextView) view.findViewById(R.id.item_answer_author);
            i_a_date = (TextView) view.findViewById(R.id.item_answer_date);
            i_a_isBest = (TextView) view.findViewById(R.id.item_answer_isBest);
            i_a_edit = (TextView) view.findViewById(R.id.item_answer_edit);
            i_a_delete = (TextView) view.findViewById(R.id.item_answer_delete);
            i_a_approve = (TextView) view.findViewById(R.id.item_answer_approve);
            i_a_vote = (TextView) view.findViewById(R.id.item_answer_vote);
            mOnAnsItemBestClickListener = myOnAnsItemBestClickListener;
            mOnAnsItemEditClickListener = myOnAnsItemEditClickListener;
            mOnAnsItemDeleteClickListener = myOnAnsItemDeleteClickListener;
            mOnAnsItemApproveClickListener = myOnAnsItemApproveClickListener;

            //***---------------------***//
            i_a_isBest.setOnClickListener(this);
            i_a_edit.setOnClickListener(this);
            i_a_delete.setOnClickListener(this);
            i_a_approve.setOnClickListener(this);


        }

        public void bind(Answer answer,int position) {
            i_a_content.setText(answer.Content);
            if (answer.Poster != null) {
                i_a_author.setText("作者:" + answer.Poster.getNick());
            } else {
                i_a_author.setText("作者:" + "匿名");
            }
            i_a_date.setText("日期:" + answer.PostDateTime);
            i_a_vote.setText(String.valueOf(answer.Votes));
            boolean flag = oneQuestion.Solved//已经解决
                    || GlobalVar.user == null//未登录
                    || answer.Poster.getUid() != GlobalVar.user
                    .getUid();//回答用户不为当前用户
            if (flag) {//为true时不能修改和删除
                i_a_edit.setVisibility(View.GONE);
                i_a_delete.setVisibility(View.GONE);
            } else {
                i_a_edit.setVisibility(View.VISIBLE);
                i_a_delete.setVisibility(View.VISIBLE);
            }
            if (GlobalVar.isSigned && !oneQuestion.Solved && oneQuestion.Poste.getUid() == GlobalVar.user.getUid()) {
                i_a_isBest.setVisibility(View.VISIBLE);
                i_a_isBest.setText("选择此回答为最佳答案");
            } else if (oneQuestion.Solved && answer.IsBestAns) {
                i_a_isBest.setVisibility(View.VISIBLE);
                i_a_isBest.setText("最佳答案");
                i_a_isBest.setClickable(false);
            } else {
                i_a_isBest.setVisibility(View.GONE);
            }
            if (GlobalVar.isSigned){
                isVoted = answer_isVoted.get(position);
                Log.e("position", position+"");
                Log.e("isvoted",isVoted?"点过了":"没点");
            } else {
                isVoted=false;
            }


            if (isVoted) {//为true时表示当前用户已经赞过此答案
                i_a_approve.setText("取消点赞");
            } else {
                i_a_approve.setText("点个赞");
            }
            i_a_isBest.setTag(answer);
            i_a_edit.setTag(answer);
            i_a_delete.setTag(answer);
            i_a_approve.setTag(answer);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_answer_isBest:
                    mOnAnsItemBestClickListener.onIsBestBtnClick(i_a_isBest, getLayoutPosition());
                    break;
                case R.id.item_answer_edit:
                    mOnAnsItemEditClickListener.onEditBtnClick(i_a_content, getLayoutPosition());
                    break;
                case R.id.item_answer_delete:
                    mOnAnsItemDeleteClickListener.onDeleteBtnClick(i_a_delete, getLayoutPosition());
                    break;
                case R.id.item_answer_approve:
                    mOnAnsItemApproveClickListener.onApproveBtnClick(i_a_approve, getLayoutPosition());
                    break;
            }
        }
    }
}





