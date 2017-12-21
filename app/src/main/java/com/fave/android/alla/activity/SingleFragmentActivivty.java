package com.fave.android.alla.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.fave.android.alla.R;


/**
 * Created by seungyeop on 2017-10-11.
 */

public abstract class SingleFragmentActivivty extends BaseActivitiy {

    //프레그먼트 인스턴스 생서에 사용되는 메서드
    protected  abstract Fragment creatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = creatFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }
}
