package com.fave.android.alla.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fave.android.alla.activity.CardNewsPagerActivity;
import com.fave.android.alla.item.CardNews;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.NetworkService.CardNewsList;
import com.fave.android.alla.R;
import com.fave.android.alla.network.RestClient;
import com.fave.android.alla.databinding.FragmentCardNewsListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardNewsListFragment extends Fragment{
    private FragmentCardNewsListBinding mBinding;
    private CardNewsAdapter mAdapter;
    private List<CardNews> mCardNewsList = new ArrayList<>();

    private static final String TAG = "CardNewsListFragment";

    public CardNewsListFragment() {}

    public static CardNewsListFragment newInstance() {
        CardNewsListFragment fragment = new CardNewsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //툴바 아이템 셋팅
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_card_news_list, menu);
    }

    //툴바 아이템 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //새로운 객체 생성시 crime 객체를 DB에 추가시켜 놓음
            case R.id.menu_item_latest:
                updateUI(0);
                return true;
            case R.id.menu_item_like:
                updateUI(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_news_list, container, false);
        mBinding.cardNewsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //updateUI(0);

        return mBinding.getRoot();
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

    //UI를 셋팅하고 업데이트
    private void updateUI(int sort){

        NetworkService service = new RestClient().getClient(getActivity()).create(NetworkService.class);
        Call<CardNewsList> call = service.repoCardNews(0, sort);
        call.enqueue(new Callback<CardNewsList>() {
            @Override
            public void onResponse(Call<CardNewsList> call, Response<CardNewsList> response) {
                int errorCode = response.body().errorCode;
                Log.d(TAG, "errorCode =" + String.valueOf(errorCode));
                Log.d(TAG, response.body().toString());
                //서버상 문제가 없을때
                if (errorCode == 100){
                    mCardNewsList = response.body().cardNews;
                    //리스트가 비어 있지 않았을때
                    if (mCardNewsList != null){
                        //어뎁터가 없으면 셋팅함
                        if (mAdapter == null){
                            mAdapter = new CardNewsAdapter(mCardNewsList);
                            mBinding.cardNewsRecyclerView.setAdapter(mAdapter);
                            //기존에 존재하는 경우 리스트를 갱신
                        }else {
                            //mCardNewsList.clear();
                            mAdapter.setCardNewsList(mCardNewsList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                }else{
                    Log.d(TAG, "errorCode not founded");

                }
            }

            @Override
            public void onFailure(Call<CardNewsList> call, Throwable t) {
                Log.d(TAG, "connection fail : " + t);

            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(0);
    }
}
