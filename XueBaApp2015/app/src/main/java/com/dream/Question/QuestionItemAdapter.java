package com.dream.Question;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dream.Entity.Question;
import com.dream.XueBaApp2015.R;

import java.util.List;

/**
 * Created by 夏目 on 2015/11/12.
 */
public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.ViewHolder>{

    private List<Question> mQuestionList;
    private FragmentActivity activity;

    public QuestionItemAdapter(List<Question> questionList,FragmentActivity mainActivity){
        super();
        mQuestionList=questionList;
        activity=mainActivity;
    }

    public interface MyItemClickListener{
        void OnMyItemClick(View view,int position);
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question,parent,false);
        return new ViewHolder(view, new MyItemClickListener() {
            @Override
            public void OnMyItemClick(View view, int position) {
                Intent intent = new Intent(parent.getContext(), QuestionDetails.class);
                intent.putExtra("id", mQuestionList.get(position).Id);
                parent.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mQuestionList.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView i_q_view;
        private TextView i_q_replies;
        private TextView i_q_title;
        private TextView i_q_detail;
        private TextView i_q_tags;
        private TextView i_q_author;
        private QuestionItemAdapter.MyItemClickListener mListener;

        public ViewHolder(View rootView,QuestionItemAdapter.MyItemClickListener listener){
            super(rootView);
            i_q_author=(TextView)rootView.findViewById(R.id.item_question_author);
            i_q_detail=(TextView)rootView.findViewById(R.id.item_question_detail);
            i_q_replies=(TextView)rootView.findViewById(R.id.item_question_replies);
            i_q_tags=(TextView)rootView.findViewById(R.id.item_question_tags);
            i_q_title=(TextView)rootView.findViewById(R.id.item_question_title);
            i_q_view=(TextView)rootView.findViewById(R.id.item_question_view);
            mListener=listener;
            rootView.setOnClickListener(this);
        }

        public void bind(Question question) {
            if(question!=null){
                if(question.Poste!=null){
                    i_q_author.setText(question.Poste.getNick());}
                else
                    i_q_author.setText("N/A");
                String str=question.Content;
//                str=str.substring(0, str.length() > 200 ? 200 : str.length())
//                        + "...";
                i_q_detail.setText(str);
                i_q_replies.setText(Integer.toString(question.Replies));
                i_q_view.setText(Integer.toString(question.Views));
                if (question.Title.isEmpty())
                    i_q_title.setText("无标题");
                else
                    i_q_title.setText(question.Title);
                if(!question.Tags.isEmpty()){
                    i_q_tags.setText(question.Tags.toString()
                            .replace("[", "").replace("]", "").replace("\n", ""));
                } else {
                    i_q_tags.setText("未分类");
                }
            } else {
                i_q_author.setText("N/A");
                i_q_detail.setText("N/A");
                i_q_replies.setText("0");
                i_q_title.setText("N/A");
                i_q_view.setText("0");
                i_q_tags.setText("N/A");
            }
        }

        @Override
        public void onClick(View v) {
            mListener.OnMyItemClick(v,getLayoutPosition());
        }
    }
}
