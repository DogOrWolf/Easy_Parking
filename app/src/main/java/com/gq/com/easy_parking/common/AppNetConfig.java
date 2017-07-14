package com.gq.com.easy_parking.common;

/**
 * Created by hasee on 2017/4/16.
 */
public class AppNetConfig {
//    public static final String IPADDRESS = "10.52.216.20";
    //    public static final String IPADDRESS = "192.168.179.1";
//    public static final String IPADDRESS = "10.52.40.208";
    public static final String IPADDRESS = "192.168.1.106";

//    public static final String BASE_URL = "http://" + IPADDRESS + ":8080/P2PInvest/";
    public static final String BASE_URL = "http://" + IPADDRESS + ":8080/";

    public static final String LOGIN = BASE_URL + "login";//登录

    public static final String INDEX = BASE_URL + "index";//访问“homeFragment”

    public static final String ALLMARKERS = BASE_URL + "marker/getAllMarkers";//获取所有停车地点

    public static final String SpotDetail = "http://192.168.1.106:8088/";//获取所有停车地点
}
