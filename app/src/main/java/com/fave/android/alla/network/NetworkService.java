package com.fave.android.alla.network;

import com.fave.android.alla.item.CardNews;
import com.fave.android.alla.item.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by seungyeop on 2017-11-10.
 */

public interface NetworkService {
    @FormUrlEncoded
    @POST("card_news/list.php")
    Call<CardNewsList> repoCardNews(@Field("CNI_SQ") int sq, @Field("SORT") int sort);

    @FormUrlEncoded
    @POST("card_news/item.php")
    Call<CardNewItem> repoCardNewsItem(@Field("CNL_NO") int no);

    @FormUrlEncoded
    @POST("login/login.php")
    Call<Login> repoInLogin(@Field("USER_ID") String id,  @Field("USER_PW") String password, @Field("USER_GB") int gb);

    @FormUrlEncoded
    @POST("login/login.php")
    Call<Login> repoOutLogin(@Field("USER_ID") String id, @Field("USER_NNM") String nickName, @Field("USER_IMG") String img, @Field("USER_GB") int gb);

    @FormUrlEncoded
    @POST("login/signup.php")
    Call<ErrorCode> repoSignUp(@Field("USER_ID") String id,  @Field("USER_PW") String password, @Field("USER_NNM") String nickName);

    @FormUrlEncoded
    @POST("user/list.php")
    Call<CardNewsList> repoMyCardNews(@Field("USER_NO") int userNo);

    @FormUrlEncoded
    @POST("user/info.php")
    Call<Login> repoUserInfo(@Field("USER_NO") int userNo);

    @FormUrlEncoded
    @POST("user/modify.php")
    Call<ErrorCode> repoProfile(@Field("USER_NO") int userNo, @Field("USER_NNM") String nickName, @Field("USER_INTRO") String intro, @Field("USER_BR") String birth, @Field("USER_GN") int gender);


    @FormUrlEncoded
    @POST("card_news/list_write.php")
    Call<CardNewsListWrite> repoCardNewsWrite(@Field("USER_NO") int userNo, @Field("CNL_GB") int gb, @Field("CNL_ST") int st);

    @Multipart
    @POST("card_news/item_write.php")
    Call<CardNewsItemWrite> repoCardNewsItemWrite(@Part MultipartBody.Part file, @Part MultipartBody.Part sq, @Part MultipartBody.Part no, @Part MultipartBody.Part content);

    class ErrorCode{
        @SerializedName("errorCode")
        public int errorCode;
    }

    class Login{
        @SerializedName("errorCode")
        public int errorCode;
        @SerializedName("userInfo")
        public List<UserInfo> userInfos= new ArrayList<>();
    }

    class CardNewsList{
        @SerializedName("errorCode")
        public int errorCode;
        @SerializedName("list")
        public List<CardNews> cardNews = new ArrayList<>();
    }

    class CardNewItem{
        @SerializedName("errorCode")
        public int errorCode;
        @SerializedName("items")
        public List<CardNews> cardNews = new ArrayList<>();
        @SerializedName("userInfo")
        public List<UserInfo> userInfos = new ArrayList<>();

    }

    class CardNewsListWrite{
        @SerializedName("errorCode")
        public int errorCode;
        @SerializedName("lastId")
        public int lastId;
    }

    class CardNewsItemWrite{
        @SerializedName("errorCode")
        public int errorCode;
        @SerializedName("sq")
        public int sq;
    }

}
