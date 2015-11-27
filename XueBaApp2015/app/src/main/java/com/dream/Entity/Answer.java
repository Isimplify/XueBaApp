package com.dream.Entity;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class Answer
{
    public Answer()
    {
    }
    public int Id;
    public int FieldId;
    public int TopicId;
    public int Layer;
    public User Poster;
    public String Title;
    public String Content;
    public String PostDateTime;
    public int Views;
    public boolean IsBestAns;
    public int Votes;

}
