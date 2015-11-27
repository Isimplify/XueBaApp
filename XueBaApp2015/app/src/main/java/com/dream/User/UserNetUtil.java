package com.dream.User;

import android.util.Log;

import com.dream.Entity.Question;
import com.dream.Entity.User;
import com.dream.Question.QuestionsNetUtil;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class UserNetUtil {
    private static final String URL_HOST = "http://10.2.26.62:80//myapp";

    private static final String USER_LOGIN_URL = URL_HOST + "/UserLogin?";
    private static final String USER_Register_URL = URL_HOST + "/Hello?";
    private static final String User_ModifyInformation_URL = URL_HOST
            + "/UserModifyInformation?";
    private static final String User_ModifyPassword_URL = URL_HOST
            + "/UserMofifyPassword?";
    private static final String User_GetUserById_URL = URL_HOST
            + "/UserGetUserById?";
    private static final String Answer_GiveAnswer_URL = URL_HOST
            + "/AnswerGiveAnswer?";
    private static final String Question_Ask_URL = URL_HOST + "/QuestionAsk?";
    private static final String Vote_GiveVote_URL = URL_HOST + "/VoteGiveVote?";
    private static final String SelectQidByUid_URL = URL_HOST
            + "/SelectQidByUid?";
    private static final String SelectAnqidByUid_URL = URL_HOST
            + "/SelectAnqidByUid?";
    private static final String UserAddCredit_URL = URL_HOST
            + "/UserAddCredit?";
    private static final String AnswerDeleteAnswer_URL = URL_HOST
            + "/AnswerDeleteAnswer?";
    private static final String QuestionMofifyByuidqid_URL = URL_HOST
            + "/QuestionMofifyByuidqid?";
    private static final String AnswerUpdateAnswer_URL = URL_HOST
            + "/AnswerUpdateAnswer?";

    public static int SERVLET_ERROR = -404;

    public static int Register(String email, String nick, String password) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = USER_Register_URL + "eml=" + email + "&nick="
                    + nick + "&pwd=" + password;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int Login(String email, String password) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = USER_LOGIN_URL + "eml=" + email + "&pwd="
                    + password;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

            if (result > 0) { // 设置一下User,此时result是uid
                GlobalVar.user = GetUserById(result); // 设置全局变量
                GlobalVar.isSigned = true; // 设置已登陆
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static User GetUserById(int uid) {
        User user = null;
        try {
            // a,b,c是参数
            String urlString = User_GetUserById_URL + "uid="
                    + Integer.toString(uid);
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            int _uid = Integer.valueOf(din.readUTF());
            String _email = din.readUTF();
            String _nick = din.readUTF();
            String _password = "";
            String _realName = din.readUTF();
            String _description = din.readUTF();
            int _downloadCredit = Integer.valueOf(din.readUTF());
            int _activated = Integer.valueOf(din.readUTF());
            Date _forgotTime, _createdAt;
            try {
                _forgotTime = new Date(din.readUTF());
            } catch (Exception e) {
                // TODO: handle exception
                _forgotTime = null;
            }
            try {
                _createdAt = new Date(din.readUTF());
            } catch (Exception e) {
                // TODO: handle exception
                _createdAt = null;
            }
            in.close();

            user = new User(_uid, _email, _nick, _password, _realName,
                    _description, _downloadCredit, _activated, _forgotTime,
                    _createdAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static int ModifyInformation(int uid, String email, String nick,
                                        String realname, String description) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = User_ModifyInformation_URL + "eml=" + email
                    + "&uid=" + uid + "&nick=" + nick + "&realname="
                    + URLEncoder.encode(realname, "utf-8") + "&description="
                    + URLEncoder.encode(description, "utf-8");
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int ModifyPassword(int uid, String oldPass, String newPass) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = User_ModifyPassword_URL + "uid=" + uid
                    + "&oldPass=" + oldPass + "&newPass=" + newPass;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int GiveAnswer(int qid, int uid, String content) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = Answer_GiveAnswer_URL + "qid=" + qid + "&uid="
                    + uid + "&content=" + URLEncoder.encode(content, "utf-8");
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

            // 增加 10分
            int score = UserAddCredit(uid, 10);
            if (score == -2) {
                result = SERVLET_ERROR;
            } else {
                GlobalVar.user.setDownloadCredit(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int Ask(int uid, String title, String content, String tag) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = Question_Ask_URL + "uid=" + uid + "&title="
                    + URLEncoder.encode(title, "utf-8") + "&content="
                    + URLEncoder.encode(content, "utf-8") + "&tag="
                    + URLEncoder.encode(tag, "utf-8");
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

            // 增加 5分
            int score = UserAddCredit(uid, 5);
            if (score == -2) {
                result = SERVLET_ERROR;
            } else {
                GlobalVar.user.setDownloadCredit(score);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean GiveVote(int aid, int uid, int up) {
        boolean result = false;
        try {
            // a,b,c是参数
            String urlString = Vote_GiveVote_URL + "aid=" + aid + "&uid=" + uid
                    + "&ups=" + up;
            URL url = new URL(urlString);

            // 这个流是重要的
            InputStream in = url.openStream();
            DataInputStream din = new DataInputStream(in);
            result = Boolean.valueOf(din.readUTF());
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (up==2)
            Log.e("赞没赞过",result?"是":"否");
        return result;
    }

    public static int AnswerUpdateAnswer(int uid, int aid, String content) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = AnswerUpdateAnswer_URL + "uid=" + uid + "&aid="
                    + aid + "&content=" + URLEncoder.encode(content, "utf-8");
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int QuestionMofifyByuidqid(int uid, int qid, String content) {
        int result = SERVLET_ERROR;
        try {
            // a,b,c是参数
            String urlString = QuestionMofifyByuidqid_URL + "uid=" + uid
                    + "&qid=" + qid + "&content="
                    + URLEncoder.encode(content, "utf-8");
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Integer.parseInt(din.readUTF());
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean AnswerDeleteAnswer(int aid, int uid) {
        boolean result = false;
        try {
            // a,b,c是参数
            String urlString = AnswerDeleteAnswer_URL + "aid=" + aid + "&uid="
                    + uid;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            // 这个流是重要的
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);
            result = Boolean.valueOf(din.readUTF());
            in.close();

            // 扣除10分
            int score = UserAddCredit(uid, -10);
            if (score == -2) {
                result = false;
            } else {
                GlobalVar.user.setDownloadCredit(score);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Question> SelectQidByUid(int uid) {
        List<Question> list = new ArrayList<Question>();
        try {
            // a,b,c是参数
            String urlString = SelectQidByUid_URL + "uid=" + uid;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            int size = Integer.parseInt(din.readUTF());
            for (int i = 0; i < size; i++) {
                int qid = Integer.valueOf(din.readUTF());
                Question question = QuestionsNetUtil.getQuestionById(qid);
                list.add(question);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;

    }

    public static List<Question> SelectAnqidByUid(int uid) {
        List<Question> list = new ArrayList<Question>();
        try {
            // a,b,c是参数
            String urlString = SelectAnqidByUid_URL + "uid=" + uid;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)

            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            int size = Integer.parseInt(din.readUTF());
            Log.e("我的问题size",size+"");
            for (int i = 0; i < size; i++) {
                int qid = Integer.valueOf(din.readUTF());
                Question question = QuestionsNetUtil.getQuestionById(qid);
                list.add(question);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static int UserAddCredit(int uid, int score) {
        int result = -2;
        try {
            // a,b,c是参数
            String urlString = UserAddCredit_URL + "uid=" + uid + "&score="
                    + score;
            URL url = new URL(urlString);
            // HttpURLConnection conn = (HttpURLConnection)
            InputStream in = url.openStream();

            DataInputStream din = new DataInputStream(in);

            result = Integer.parseInt(din.readUTF());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
