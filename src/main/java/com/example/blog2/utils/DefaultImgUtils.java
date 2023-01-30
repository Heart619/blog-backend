package com.example.blog2.utils;

import org.springframework.util.StringUtils;

/**
 * @author mxp
 * @date 2023/1/28 18:19
 */
public class DefaultImgUtils {

    private static final String DEFAULT_IMG_1 = "default/other0.jpg";

    private static final String DEFAULT_IMG_2 = "default/other1.jpg";

    private static final String DEFAULT_AVATAR = "default/avatar.png";

    private static final String IMG_COOKIE_NAME = "blog_cook_name";

    public static String getDefaultBackImg() {
        return (System.currentTimeMillis() & 1) == 0 ? DEFAULT_IMG_1 : DEFAULT_IMG_2;
    }

    public static String getDefaultAvatarImg() {
        return DEFAULT_AVATAR;
    }

    public static boolean isDefaultBackImg(String img) {
        return !StringUtils.isEmpty(img) && (DEFAULT_IMG_1.equals(img) || DEFAULT_IMG_2.equals(img));
    }

    public static boolean isDefaultAvatarImg(String img) {
        return !StringUtils.isEmpty(img) && DEFAULT_AVATAR.equals(img);
    }
}
