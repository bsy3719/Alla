package com.fave.android.alla.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fave.android.alla.fragment.CardNewsItemFragment;
import com.fave.android.alla.item.CardNews;
import com.fave.android.alla.item.UserInfo;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.NetworkService.CardNewItem;
import com.fave.android.alla.network.RestClient;
import com.fave.android.alla.R;
import com.fave.android.alla.databinding.ActivityCardNewsPagerBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardNewsPagerActivity extends AppCompatActivity {

    private static final String EXTRA_CARD_NEWS_PK = "com.fave.android.alla.card_news_pk";

    private ActivityCardNewsPagerBinding mBinding;
    private List<CardNews> mCardNewsList;
    private UserInfo mUserInfo;
    private static final String TAG = "CardNewsPager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_card_news_pager);
        getCardNews();

    }

    private void getCardNews(){
        int card_news_pk = getIntent().getIntExtra(EXTRA_CARD_NEWS_PK, 0);
        Log.d(TAG, String.valueOf(card_news_pk));

        NetworkService service = new RestClient().getClient(this).create(NetworkService.class);
        Call<CardNewItem> call = service.repoCardNewsItem(card_news_pk);
        call.enqueue(new Callback<CardNewItem>() {
            @Override
            public void onResponse(Call<CardNewItem> call, Response<CardNewItem> response) {
                int errorCode = response.body().errorCode;


                //서버상 문제가 없을때
                if (errorCode == 100) {
                    mCardNewsList = response.body().cardNews;
                    mUserInfo = response.body().userInfos.get(0);
                    setAdapter();
                }else{

                }
            }
            @Override
            public void onFailure(Call<CardNewItem> call, Throwable t) {

            }
        });

    }

    private void setAdapter(){

        FragmentManager fm = getSupportFragmentManager();
        mBinding.cardNewsViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0){
                    CardNews cardNews = mCardNewsList.get(position);
                    return CardNewsItemFragment.newInstance(cardNews, mUserInfo,position);
                }else {
                    CardNews cardNews = mCardNewsList.get(position);
                    return CardNewsItemFragment.newInstance(cardNews, mCardNewsList.size(), position);
                }
            }
            @Override
            public int getCount() {
                return mCardNewsList.size();
            }
        });

    }

    public static Intent newIntent(Context context, int cardNewsNo){
        Intent intent = new Intent(context, CardNewsPagerActivity.class);
        intent.putExtra(EXTRA_CARD_NEWS_PK, cardNewsNo);
        return intent;
    }
}
