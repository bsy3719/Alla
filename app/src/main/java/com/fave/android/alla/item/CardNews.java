package com.fave.android.alla.item;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seungyeop on 2017-11-06.
 */

public class CardNews implements Parcelable{
    @SerializedName("CNL_NO")
    private int cardNewsNo;
    @SerializedName("CNI_SQ")
    private int seqeunce;
    @SerializedName("CNI_CONTENT")
    private String content;
    @SerializedName("CNI_IMG")
    private String imageUrl;
    @SerializedName("CNL_VIEW_CNT")
    private int viewCnt;
    @SerializedName("CNL_LIKE_CNT")
    private int likeCnt;
    @SerializedName("CNL_CMT_CNT")
    private int commentCnt;

    protected CardNews(Parcel in) {
        cardNewsNo = in.readInt();
        seqeunce = in.readInt();
        content = in.readString();
        imageUrl = in.readString();
        viewCnt = in.readInt();
        likeCnt = in.readInt();
        commentCnt = in.readInt();
    }

    public static final Creator<CardNews> CREATOR = new Creator<CardNews>() {
        @Override
        public CardNews createFromParcel(Parcel in) {
            return new CardNews(in);
        }

        @Override
        public CardNews[] newArray(int size) {
            return new CardNews[size];
        }
    };

    public int getCardNewsNo() {
        return cardNewsNo;
    }

    public void setCardNewsNo(int cardNewsNo) {
        this.cardNewsNo = cardNewsNo;
    }

    public int getSeqeunce() {
        return seqeunce;
    }

    public void setSeqeunce(int seqeunce) {
        this.seqeunce = seqeunce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cardNewsNo);
        dest.writeInt(seqeunce);
        dest.writeString(content);
        dest.writeString(imageUrl);
        dest.writeInt(viewCnt);
        dest.writeInt(likeCnt);
        dest.writeInt(commentCnt);
    }
}
