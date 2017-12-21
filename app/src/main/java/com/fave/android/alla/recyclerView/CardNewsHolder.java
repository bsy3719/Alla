package com.fave.android.alla.recyclerView;

/**
 * Created by seungyeop on 2017-12-14.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fave.android.alla.R;
import com.fave.android.alla.activity.CardNewsPagerActivity;
import com.fave.android.alla.item.CardNews;

/*public class CardNewsHolder extends RecyclerView.ViewHolder{

    private CardNews mCardNews;
    private ImageView mListImageView;
    private TextView mTitleTextView;

    private Context mContext;

    public CardNewsHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
        mListImageView = itemView.findViewById(R.id.list_item_card_news_image_view);
        mTitleTextView = itemView.findViewById(R.id.list_item_card_news_text_view);
        itemView.setOnClickListener(this);

    }

    public void bindCardNews(CardNews cardNews){
        mCardNews = cardNews;
        mTitleTextView.setText(mCardNews.getContent());
        Glide.with(mContext).load(mCardNews.getImageUrl()).into(mListImageView);
    }

    @Override
    public abstract void onClick(View v) {
        Intent intent = CardNewsPagerActivity.newIntent(getActivity(), mCardNews.getCardNewsNo());
        startActivity(intent);
    }
}*/
