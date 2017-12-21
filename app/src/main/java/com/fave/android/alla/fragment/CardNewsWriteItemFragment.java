package com.fave.android.alla.fragment;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.fave.android.alla.R;
import com.fave.android.alla.databinding.FragmentCardNewsWriteItemBinding;

public class CardNewsWriteItemFragment extends Fragment {

    private static final String ARG_CARD_NEWS_WRITE_URI = "card_news_write_uri";
    private static final String ARG_CARD_NEWS_WRITE_POSITION = "card_news_write_position";

    private FragmentCardNewsWriteItemBinding mBinding;
    private String mImagePath;
    private int mPosition;

    private WriteListener activityCallback;

    public interface WriteListener{
        void onReceivedData(String content, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            activityCallback = (WriteListener) context;
        } catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + " must implement ActivityListener");
        }
    }

    public CardNewsWriteItemFragment() {

    }

    public static CardNewsWriteItemFragment newInstance(String path, int position) {
        CardNewsWriteItemFragment fragment = new CardNewsWriteItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CARD_NEWS_WRITE_URI, path);
        args.putInt(ARG_CARD_NEWS_WRITE_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImagePath = getArguments().getString(ARG_CARD_NEWS_WRITE_URI);
        mPosition = getArguments().getInt(ARG_CARD_NEWS_WRITE_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_news_write_item, container, false);

        if (mPosition == 0) {
            mBinding.cardNewsEditText.setHint(R.string.write_title_edit_text);
        }else{
            mBinding.cardNewsEditText.setHint(R.string.write_content_edit_text);
        }

        Glide.with(this).load(mImagePath).into(mBinding.cardNewsImageView);

        mBinding.cardNewsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                activityCallback.onReceivedData(charSequence.toString(), mPosition);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

         //키보드 자동실행
        mBinding.cardNewsEditText.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mBinding.cardNewsEditText, 0);
            }
        }, 100);
    }
}
