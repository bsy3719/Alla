package com.fave.android.alla.util;

/**
 * Created by seungyeop on 2017-12-12.
 */

public class MethodUtil {

    //메일 앞부분 자르기
    public static String getNickName(String mail){
        int idx = mail.indexOf("@");
        String nickName = mail.substring(0,idx);
        return nickName;
    }
}
