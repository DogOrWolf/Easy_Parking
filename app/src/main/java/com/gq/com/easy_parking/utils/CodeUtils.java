package com.gq.com.easy_parking.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hasee on 2017/4/25.
 */
public class CodeUtils {
    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
