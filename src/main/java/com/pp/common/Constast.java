package com.pp.common;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

/*
* 状态码
* */
public class Constast {
    public static final Integer OK = 200;
    public static final Integer ERROR = -1;
    public static final Integer USER_TYPE_SUPPER = 0;
    public static final Integer USER_TYPE_NORMAL = 1;
    public static final String TYPE_MNEU = "menu";
    public static final String TYPE_PERMISSION = "permission";
    public static final Integer AVAILABLE_TRUE = 1;
    public static final Integer AVAILABLE_FALSE = 0;
    public static final String USER_DEFULT_PWD = "123456";

    /**
     * 商品默认图片
     */
    public static final String IMAGES_DEFAULTGOODSIMG_PNG = "images/defaultgoodsimg.png";
}