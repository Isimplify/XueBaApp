package com.dream.Helper;

import java.util.Random;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class RandomString {
    private static Random rand = new Random();

    private static char getRandomChar()
    {//此函数利用ascii码随机生成一个a-z或0-9或A-Z字符
        int ret = rand.nextInt(122);
        while (ret < 48 || (ret > 57 && ret < 65) || (ret > 90 && ret < 97))
        {
            ret = rand.nextInt(122);
        }
        return (char)ret;
    }

    public static String GetRandomString(int length)
    {//此函数利用getRandomChar()函数生成长度为length的只含字母数字的字符串
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i)
        {
            builder.append(getRandomChar());
        }
        return builder.toString();
    }
}