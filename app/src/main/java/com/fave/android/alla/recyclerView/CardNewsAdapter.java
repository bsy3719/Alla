package com.fave.android.alla.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fave.android.alla.R;
import com.fave.android.alla.fragment.CardNewsListFragment;
import com.fave.android.alla.item.CardNews;

import java.util.List;

/**
 * Created by seungyeop on 2017-12-14.
 */

/*public class CardNewsAdapter extends RecyclerView.Adapter<CardNewsListFragment.CardNewsHolder>{

    private List<CardNews> mCardNewsList;

    public CardNewsAdapter(List<CardNews> list){
        mCardNewsList = list;
    };

    @Override
    public CardNewsListFragment.CardNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.list_item_card_news, parent, false);
        return new CardNewsListFragment.CardNewsHolder(view);
    }

    @Override
    public void onBindViewHolder(CardNewsListFragment.CardNewsHolder holder, int position) {
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
}*/
