package com.dream.User;

import com.dream.Entity.User;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class GlobalVar {
    public static User user = null; // 全局唯一的用户，登陆后将保留用户信息
    public static boolean isSigned = false; // 是否已经登录过

}
