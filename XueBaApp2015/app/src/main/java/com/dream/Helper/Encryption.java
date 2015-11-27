package com.dream.Helper;

import java.security.MessageDigest;

/**
 * Created by 夏目 on 2015/10/31.
 */
public class Encryption {
    private final static int SALT_LENGTH = 8;

    private static String encrypt(String input, String salt)
    {

        try {
            byte[] btInput = (salt+input).getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            return salt + hexDigest(md);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static String Encrypt(String input)
    {
        String salt = RandomString.GetRandomString(SALT_LENGTH);
        return encrypt(input, salt);
    }

    public static boolean Validate(String input, String correct)
    {
        return encrypt(input, correct.substring(0, SALT_LENGTH)).equals(correct);
    }

    public static String hexDigest(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}