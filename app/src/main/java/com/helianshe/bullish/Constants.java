package com.helianshe.bullish;

public class Constants {
    /**
     * 底导id,固定值
     */
    public static  String TAB_HOME= "1";
    public static  String TAB_WITHDRAW= "2";
    public static  String TAB_MINE= "3";
    public static  String TAB_WELFARE= "4";

    /**
     * 底导index
     */
    public   static int TAB_MAIN_INDEX = 0;
    public   static int TAB_WITHDRAW_INDEX = 1;
    public   static int TAB_WELFARE_INDEX = 3;
    public   static int TAB_MY_INDEX = 2;

    private static final String H5_HOST_DEV = "https://restart-qa.qttfe.com";
    private static final String H5_HOST_PRE = "https://static-h5.haozan123.cn";
    /**
     * 首页
     */
    private static  String DEV_HOME_URL= H5_HOST_DEV+"/main/index.html";
    private static  String PRE_HOME_URL= H5_HOST_PRE+"/main/index.html";
    public static  String HOME_URL= AppUtils.isApkInDebug(AppApplication.getInstance())?DEV_HOME_URL:PRE_HOME_URL;


    /**
     * 提现页
     */
    private static  String DEV_WITHDRAW_URL= "https://withdrawgoodpraise-qa.qttfe.com/index.html";
    private static  String PRE_WITHDRAW_URL= "https://goodpraisewithdraw.1sapp.com/index.html";
    public static  String WITHDRAW_URL= AppUtils.isApkInDebug(AppApplication.getInstance())?DEV_WITHDRAW_URL:PRE_WITHDRAW_URL;

    /**
     /**
     * 福利页
     */
    private static  String DEV_WELFARE_URL= H5_HOST_DEV+"/goldTask/index.html";
    private static  String PRE_WELFARE_URL= H5_HOST_PRE+"/goldTask/index.html";
    public static  String WELFARE_URL= AppUtils.isApkInDebug(AppApplication.getInstance())?DEV_WELFARE_URL:PRE_WELFARE_URL;

    /**
     * 我的
     */
    private static  String DEV_MINE_URL= H5_HOST_DEV+"/goldTask/index.html";
    private static  String PRE_MINE_URL= H5_HOST_PRE+"/goldTask/index.html";
    public static  String MINE_URL= AppUtils.isApkInDebug(AppApplication.getInstance())?DEV_WELFARE_URL:PRE_WELFARE_URL;
    /**
     /**
     * 商品详情
     */
    private static  String DEV_DETAIL_URL= H5_HOST_DEV+"/goodDetail/index.html?id=";
    private static  String PRE_DETAIL_URL= H5_HOST_PRE+"/goodDetail/index.html?id=";
    public static  String DETAIL_URL= AppUtils.isApkInDebug(AppApplication.getInstance())?DEV_DETAIL_URL:PRE_DETAIL_URL;


}
