package com.dream.Entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class Question {
    public Question() {
        Answers = new ArrayList<Answer>();
        // Tags = new ArrayList<Tag>();
        Tags = new ArrayList<String>();
    }

    public String getAbstract() {
        if (Content.length() >= 60)
            return Content.substring(0, 60) + "....";
        else
            return Content;
    }

    public int Id;
    public int FieldId;
    public User Poste;
    public String Title;
    public String Content;
    public Timestamp PostDateTime;
    public Timestamp LastPostDateTime;
    public int Views;
    public int Replies;
    public boolean Solved;

    public List<Answer> Answers;
    // public List<Tag> Tags;
    // 为了简单，我将这里改为了String，如果学弟学妹们想做的更好，可以改回原来的形式
    public List<String> Tags;
}