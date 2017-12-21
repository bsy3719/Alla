package com.fave.android.alla.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fave.android.alla.fragment.SignUpFragment;

public class SignUpActivity extends SingleFragmentActivivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("회원가입");
    }

    @Override
    protected Fragment creatFragment() {
        return new SignUpFragment();
    }
}
