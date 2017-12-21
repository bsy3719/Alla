package com.fave.android.alla.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fave.android.alla.R;
import com.fave.android.alla.activity.CardNewsPagerActivity;
import com.fave.android.alla.activity.LoginActivity;
import com.fave.android.alla.activity.ProfileActivity;
import com.fave.android.alla.databinding.FragmentMyPageBinding;
import com.fave.android.alla.databinding.FragmentMyPageLoginBinding;
import com.fave.android.alla.item.CardNews;
import com.fave.android.alla.item.LoginInfo;
import com.fave.android.alla.item.UserInfo;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.NetworkService.CardNewsList;
import com.fave.android.alla.network.NetworkService.Login;
import com.fave.android.alla.network.RestClient;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment {

    private FragmentMyPageLoginBinding mLoginBinding;
    private FragmentMyPageBinding mMypageBinding;

    private LoginInfo mLoginInfo = LoginInfo.getInstance();

    private CardNewsAdapter mAdapter;
    private List<CardNews> mCardNewsList = new ArrayList<>();
    private UserInfo mUserInfo = new UserInfo();

    private String TAG = "MyPageFragment";

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(mLoginInfo.isLoginFlag()){
            mMypageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false);
            mMypageBinding.cardNewsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            updateList();
            updateUserInfo();

            mMypageBinding.userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                }
            });

            return mMypageBinding.getRoot();

        }else{
            mLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_login, container, false);

            mLoginBinding.loginRequireButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });

            return mLoginBinding.getRoot();
        }

    }

    private class CardNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardNews mCardNews;
        private ImageView mListImageView;
        private TextView mTitleTextView;

        public CardNewsHolder(View itemView) {
            super(itemView);

            mListImageView = itemView.findViewById(R.id.list_item_card_news_image_view);
            mTitleTextView = itemView.findViewById(R.id.list_item_card_news_text_view);

            itemView.setOnClickListener(this);

        }

        public void bindCardNews(CardNews cardNews){
            mCardNews = cardNews;
            mTitleTextView.setText(mCardNews.getContent());
            Glide.with(getActivity()).load(mCardNews.getImageUrl()).into(mListImageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CardNewsPagerActivity.newIntent(getActivity(), mCardNews.getCardNewsNo());
            startActivity(intent);
        }
    }

    private class CardNewsAdapter extends RecyclerView.Adapter<CardNewsHolder>{

        private List<CardNews> mCardNewsList;

        public CardNewsAdapter(List<CardNews> list){
            mCardNewsList = list;
        };

        @Override
        public CardNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_card_news, parent, false);
            return new CardNewsHolder(view);
        }

        @Override
        public void onBindViewHolder(CardNewsHolder holder, int position) {
            CardNews cardNews = mCardNewsList.get(position);
            holder.bindCardNews(cardNews);
        }

        @Override
        public int getItemCount() {
            return mCardNewsList.size();
        }

        public void setCardNewsList(List<CardNews> cardNewsList){
            mCardNewsList = cardNewsList;
        }
    }

    //List 셋팅
    private void updateList(){
        NetworkService service = new RestClient().getClient(getActivity()).create(NetworkService.class);
        Call<CardNewsList> call = service.repoMyCardNews(mLoginInfo.getUserNo());
        call.enqueue(new Callback<CardNewsList>() {
            @Override
            public void onResponse(Call<CardNewsList> call, Response<CardNewsList> response) {
                int errorCode = response.body().errorCode;

                //서버상 문제가 없을때
                if (errorCode == 100){
                    mCardNewsList = response.body().cardNews;
                    if (mCardNewsList != null){
                        if (mAdapter == null){
                            mAdapter = new CardNewsAdapter(mCardNewsList);
                            mMypageBinding.cardNewsRecyclerView.setAdapter(mAdapter);
                        }else {
                            mAdapter.setCardNewsList(mCardNewsList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }else{
                    //네트워크 오류일때
                }
            }

            @Override
            public void onFailure(Call<CardNewsList> call, Throwable t) {

            }

        });
    }

    private void updateUserInfo(){
        NetworkService service = new RestClient().getClient(getActivity()).create(NetworkService.class);
        Call<Login> call = service.repoUserInfo(mLoginInfo.getUserNo());
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int errorCode = response.body().errorCode;

                //서버상 문제가 없을때
                if (errorCode == 100){
                    mUserInfo = response.body().userInfos.get(0);
                    Glide.with(getActivity()).load(mUserInfo.getUserImg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mMypageBinding.userImageView);
                    mMypageBinding.nickNameTextView.setText(mUserInfo.getUserNnm());

                }else{
                    //네트워크 오류일떄

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mLoginInfo.isLoginFlag()) {
            updateList();
            updateUserInfo();
        }
    }

}
