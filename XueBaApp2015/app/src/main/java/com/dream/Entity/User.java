package com.dream.Entity;

import com.dream.Helper.Encryption;

import java.util.Date;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class User {
    private int Uid;
    private String Email;
    private String Nick;
    private String RealName;
    private String Description;
    private int DownloadCredit;
    private Date ForgotTime;
    private Date CreatedAt;
    private int Activated;
    private static int rank;
    private static String title;
    private String password;

    public void setDownloadCredit(int downloadCredit) {
        DownloadCredit = downloadCredit;
    }

    public User(int uid, String email, String nick, String password,
                String realName, String description, int download, int activated,
                Date forgotTime, Date created) {
        this.Uid = uid;
        this.Email = email;
        this.Nick = nick;
        this.password = password;
        this.RealName = realName;
        this.Description = description;
        this.DownloadCredit = download;
        this.Activated = activated;
        this.ForgotTime = forgotTime;
        this.CreatedAt = created;
        User.rank = Rank();
        User.title = Title();
    }

    public int getUid() {
        return Uid;
    }

    public String getEmail() {
        return Email;
    }

    public String getNick() {
        return Nick;
    }

    public String getRealName() {
        return RealName;
    }

    public String getDescription() {
        return Description;
    }

    public int getDownloadCredit() {
        return DownloadCredit;
    }

    public Date getForgotTime() {
        return ForgotTime;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public int getActivated() {
        return Activated;
    }

    public int getRank() {
        return Rank();
    }

    public String getTitle() {
        return Title();
    }

    public Boolean CheckPassword(String password) {
        return Encryption.Validate(password, this.password);
    }

    public int Rank() {
        if (DownloadCredit < 0)
            return -1;
        if (DownloadCredit <= 5)
            return 0;
        if (DownloadCredit <= 50)
            return 1;
        if (DownloadCredit <= 150)
            return 2;
        if (DownloadCredit <= 400)
            return 3;
        if (DownloadCredit <= 1000)
            return 4;
        if (DownloadCredit <= 3000)
            return 5;
        if (DownloadCredit <= 7000)
            return 6;
        if (DownloadCredit <= 20000)
            return 7;
        if (DownloadCredit <= 50000)
            return 8;
        return 9;
    }

    public String Title() {
        if (rank == 0)
            return "书生";
        if (rank == 1)
            return "秀才";
        if (rank == 2)
            return "举人";
        if (rank == 3)
            return "探花";
        if (rank == 4)
            return "榜眼";
        if (rank == 5)
            return "状元";
        if (rank == 6)
            return "编修";
        if (rank == 7)
            return "翰林";
        if (rank == 8)
            return "大学士";
        return "文曲星";
    }
}
