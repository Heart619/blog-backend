package com.example.blog2.utils;

import org.springframework.util.StringUtils;

/**
 * @author mxp
 * @date 2023/1/28 18:19
 */
public class DefaultImgUtils {

    private static final String[] DEFAULT_IMG = new String[]{
            "default/other0.jpg",
            "default/other1.jpg",
            "default/other2.jpg",
            "default/other3.jpg",
            "default/other4.jpg",
            "default/other5.jpg",
    };

    private static final String DEFAULT_AVATAR = "default/avatar.png";

    public static String getDefaultBackImg() {
        return DEFAULT_IMG[(int) (Math.random() * DEFAULT_IMG.length)];
    }

    public static String getDefaultAvatarImg() {
        return DEFAULT_AVATAR;
    }

    public static boolean isDefaultBackImg(String img) {
        for (String s : DEFAULT_IMG) {
            if (s.equals(img)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDefaultAvatarImg(String img) {
        return !StringUtils.isEmpty(img) && DEFAULT_AVATAR.equals(img);
    }
}
