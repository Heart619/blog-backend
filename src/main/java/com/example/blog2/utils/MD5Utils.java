package com.example.blog2.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/4/1 10:45
 */
public class MD5Utils {
    public static String code(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if(i<0)
                    i+=256;
                if (i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
