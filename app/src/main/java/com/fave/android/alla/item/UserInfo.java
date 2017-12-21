package com.fave.android.alla.item;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seungyeop on 2017-12-11.
 */

public class UserInfo implements Parcelable{

    @SerializedName("USER_NO")
    private int userNo;
    @SerializedName("USER_ID")
    private String userId;
    @SerializedName("USER_NNM")
    private String userNnm;
    @SerializedName("USER_IMG")
    private String userImg;
    @SerializedName("USER_INTRO")
    private String userIntro;
    @SerializedName("USER_BR")
    private String userBr;
    @SerializedName("USER_GN")
    private int userGn;

    public UserInfo(){
    }

    protected UserInfo(Parcel in) {
        userNo = in.readInt();
        userId = in.readString();
        userNnm = in.readString();
        userImg = in.readString();
        userIntro = in.readString();
        userBr = in.readString();
        userGn = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNnm() {
        return userNnm;
    }

    public void setUserNnm(String userNnm) {
        this.userNnm = userNnm;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getUserBr() {
        return userBr;
    }

    public void setUserBr(String userBr) {
        this.userBr = userBr;
    }

    public int getUserGn() {
        return userGn;
    }

    public void setUserGn(int userGn) {
        this.userGn = userGn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userNo);
        dest.writeString(userId);
        dest.writeString(userNnm);
        dest.writeString(userImg);
        dest.writeString(userIntro);
        dest.writeString(userBr);
        dest.writeInt(userGn);
    }
}