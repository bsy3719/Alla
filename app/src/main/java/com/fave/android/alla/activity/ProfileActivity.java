package com.fave.android.alla.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fave.android.alla.fragment.ProfileFragment;

public class ProfileActivity extends SingleFragmentActivivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("내 프로필");
    }

    @Override
    protected Fragment creatFragment() {
        return new ProfileFragment();
    }
}
