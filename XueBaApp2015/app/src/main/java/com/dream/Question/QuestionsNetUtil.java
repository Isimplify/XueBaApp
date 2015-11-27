package com.dream.Question;

import android.util.Log;

import com.dream.Entity.Answer;
import com.dream.Entity.Question;
import com.dream.User.UserNetUtil;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class QuestionsNetUtil {
    private static final String URL_HOST = "http://10.2.26.62:80//Soga";

    private static final String QMgetUnanseredQuestion_URL = URL_HOST
            + "/QMgetUnanseredQuestion?";
    private static final String QMgetBestSolutions_URL = URL_HOST
            + "/QMgetBestSolutions?";
    // private static final String QMgetQuestionsByTag_URL = URL_HOST
    // + "/QMgetQuestionsByTag?";
    private static final String QMgetQuestionById_URL = URL_HOST
            + "/QMgetQuestionById?";
    private static final String QMAddViews_URL = URL_HOST + "/QMAddViews?";
    private static final String QMSolveQuestion_URL = URL_HOST
            + "/QMSolveQuestion?";

    public static int SERVLET_ERROR = -404;

    public static boolean SolveQuestion(int qid, int aid) {
        boolean result = false;
        try {
            // a,b,c是参数
            String urlString = QMSolveQuestion_URL + "qid=" + qid + "&aid="
                    + aid;

            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Boolean.valueOf(din.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean addViews(int qid) {
        boolean result = false;
        try {
            // a,b,c是参数
            String urlString = QMAddViews_URL + "qid=" + qid;

            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Boolean.valueOf(din.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Question> getUnanseredQuestion(int tagId, int pageNum,
                                                      int queryType) {
        List<Question> list = new ArrayList<Question>();
        try {
            // a,b,c是参数
            String urlString = QMgetUnanseredQuestion_URL + "tagId=" + tagId
                    + "&pageNum=" + pageNum + "&queryType=" + queryType;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            int size = Integer.parseInt(din.readUTF());
            Log.e("getUnanseredQuestion",size+"");
            for (int i = 0; i < size; i++) {
                Question question = new Question();
                question.Id = Integer.parseInt(din.readUTF()); // 问题ID，UI中并不需要
                // question.FieldId = Integer.parseInt(din.readUTF());
                question.Poste = UserNetUtil.GetUserById(Integer.parseInt(din
                        .readUTF()));
                question.Title = din.readUTF(); // 获得标题
                question.Content = din.readUTF(); // 获得内容
                question.Views = Integer.parseInt(din.readUTF()); // 获得浏览
                question.Replies = Integer.parseInt(din.readUTF()); // 获得回答数
                question.Solved = Boolean.valueOf(din.readUTF());

                int tagSize = Integer.parseInt(din.readUTF());
                for (int j = 0; j < tagSize; j++) {
                    question.Tags.add(din.readUTF().trim());
                }

                list.add(question);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static List<Question> getBestSolutions(int tagId, int pageNum,
                                                  int queryType) {
        List<Question> list = new ArrayList<Question>();
        try {
            // a,b,c是参数
            String urlString = QMgetBestSolutions_URL + "tagId=" + tagId
                    + "&pageNum=" + pageNum + "&queryType=" + queryType;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            int size = Integer.parseInt(din.readUTF());
            Log.e("BestQuestion",size+"");
            for (int i = 0; i < size; i++) {
                Question question = new Question();
                question.Id = Integer.parseInt(din.readUTF()); // 问题ID，UI中并不需要
                // question.FieldId = Integer.parseInt(din.readUTF());
                question.Poste = UserNetUtil.GetUserById(Integer.parseInt(din
                        .readUTF()));
                question.Title = din.readUTF(); // 获得标题
                question.Content = din.readUTF(); // 获得内容
                question.Views = Integer.parseInt(din.readUTF()); // 获得浏览
                question.Replies = Integer.parseInt(din.readUTF()); // 获得回答数
                question.Solved = Boolean.valueOf(din.readUTF());

                int tagSize = Integer.parseInt(din.readUTF());
                for (int j = 0; j < tagSize; j++) {
                    question.Tags.add(din.readUTF().trim());
                }

                list.add(question);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }



    public static Question getQuestionById(int id) {
        Question question = new Question();
        try {
            // a,b,c是参数
            String urlString = QMgetQuestionById_URL + "qid=" + id;

            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            question.Id = Integer.valueOf(din.readUTF());
            question.Poste = UserNetUtil.GetUserById(Integer.valueOf(din
                    .readUTF()));
            question.Title = din.readUTF();
            question.Content = din.readUTF();
            question.Views = Integer.valueOf(din.readUTF());
            question.Replies = Integer.valueOf(din.readUTF());
            question.Solved = Boolean.valueOf(din.readUTF());

            int size = Integer.parseInt(din.readUTF());
            for (int i = 0; i < size; i++) {
                Answer answer = new Answer();
                answer.Id = Integer.valueOf(din.readUTF());
                answer.Poster = UserNetUtil.GetUserById(Integer.valueOf(din
                        .readUTF()));
                answer.Title = din.readUTF(); // 获得标题
                answer.Content = din.readUTF(); // 获得内容
                answer.Views = Integer.valueOf(din.readUTF());
                answer.Votes = Integer.valueOf(din.readUTF());
                answer.PostDateTime = din.readUTF();
                answer.IsBestAns = Boolean.valueOf(din.readUTF());
                question.Answers.add(answer);
            }

            int tagSize = Integer.parseInt(din.readUTF());
            for (int j = 0; j < tagSize; j++) {
                question.Tags.add(din.readUTF().trim());
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return question;
    }
}

