package com.fave.android.alla.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fave.android.alla.item.CardNews;
import com.fave.android.alla.R;
import com.fave.android.alla.databinding.FragmentCardNewsContentBinding;
import com.fave.android.alla.databinding.FragmentCardNewsTitleBinding;
import com.fave.android.alla.item.UserInfo;

public class CardNewsItemFragment extends Fragment {

    private static final String ARG_CARD_NEWS = "card_news";
    private static final String ARG_USER_INFO = "user_info";

    private static final String ARG_CARD_NEWS_POSITION = "card_news_position";
    private static final String ARG_CARD_NEWS_SIZE = "card_news_size";

    private FragmentCardNewsTitleBinding mTitleBinding;
    private FragmentCardNewsContentBinding mContentBinding;

    private int mPosition;
    private int mSize;
    private CardNews mCardNews;
    private UserInfo mUserInfo;

    private boolean mFavoriteFlag = false;

    public CardNewsItemFragment() {
        // Required empty public constructor
    }

    public static CardNewsItemFragment newInstance(CardNews cardNews, int size, int position) {
        CardNewsItemFragment fragment = new CardNewsItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_NEWS, cardNews);
        args.putInt(ARG_CARD_NEWS_POSITION, position);
        args.putInt(ARG_CARD_NEWS_SIZE, size);
        fragment.setArguments(args);
        return fragment;
    }

    public static CardNewsItemFragment newInstance(CardNews cardNews, UserInfo userInfo, int position) {
        CardNewsItemFragment fragment = new CardNewsItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_NEWS, cardNews);
        args.putParcelable(ARG_USER_INFO, userInfo);
        args.putInt(ARG_CARD_NEWS_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPosition = getArguments().getInt(ARG_CARD_NEWS_POSITION);

        if(mPosition == 0){
            mCardNews = getArguments().getParcelable(ARG_CARD_NEWS);
            mUserInfo = getArguments().getParcelable(ARG_USER_INFO);
        }else{
            mCardNews = getArguments().getParcelable(ARG_CARD_NEWS);
            mSize = getArguments().getInt(ARG_CARD_NEWS_SIZE);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mPosition == 0){
            mTitleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_news_title, container, false);

            setTitle();

           return mTitleBinding.getRoot();

        }else{
            mContentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_news_content, container, false);

            setContent();

            return mContentBinding.getRoot();

        }

    }

    private void setTitle(){
        //글 셋팅
        Glide.with(this).load(mCardNews.getImageUrl()).into(mTitleBinding.cardNewsTitleImageView);
        mTitleBinding.cardNewsTitleTextView.setText(mCardNews.getContent());
        mTitleBinding.viewCntTextView.setText(String.valueOf(mCardNews.getViewCnt()) + " views");
        mTitleBinding.commentCntTextView.setText(String.valueOf(mCardNews.getCommentCnt()));
        mTitleBinding.likeCntTextView.setText(String.valueOf(mCardNews.getLikeCnt()));

        //유저 정보 셋팅
        Glide.with(this).load(mUserInfo.getUserImg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mTitleBinding.userImageView);
        mTitleBinding.nickNameTextView.setText(mUserInfo.getUserNnm());
    }

    private void setContent(){
        Glide.with(this).load(mCardNews.getImageUrl()).into(mContentBinding.cardNewsContentImageView);
        mContentBinding.cardNewsContentTextView.setText(mCardNews.getContent());

        mContentBinding.cardNewsCntTextView.setText(String.valueOf(mPosition) + "/" + String.valueOf(mSize-1));

        mContentBinding.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFavoriteFlag){
                    mFavoriteFlag = !mFavoriteFlag;
                    mContentBinding.likeImageView.setBackground(getResources().getDrawable(R.drawable.ic_favorite));
                }else{
                    mFavoriteFlag = !mFavoriteFlag;
                    mContentBinding.likeImageView.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border));
                }
            }
        });
    }

}