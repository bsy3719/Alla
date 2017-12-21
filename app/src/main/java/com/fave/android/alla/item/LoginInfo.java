package com.fave.android.alla.item;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by seungyeop on 2017-11-21.
 */

public class LoginInfo {

    private volatile static LoginInfo loginInfoInstance;

    private int userNo;
    private String id;
    private String nickName;
    private String imageURL;
    private String intro;
    private Date birth;
    private int gender;
    private boolean loginFlag = false;

    private LoginInfo(){
    }

    public static LoginInfo getInstance(){
        if (loginInfoInstance == null){
            synchronized (LoginInfo.class){
                if (loginInfoInstance == null){
                    loginInfoInstance = new LoginInfo();
                }
            }
        }
        return loginInfoInstance;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
}
