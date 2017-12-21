package com.fave.android.alla.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fave.android.alla.R;
import com.fave.android.alla.databinding.FragmentProfileBinding;
import com.fave.android.alla.item.LoginInfo;
import com.fave.android.alla.item.UserInfo;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.NetworkService.Login;
import com.fave.android.alla.network.NetworkService.ErrorCode;
import com.fave.android.alla.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding mBinding;

    private UserInfo mUserInfo;
    private LoginInfo mLoginInfo = LoginInfo.getInstance();

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        updateUserInfo();

      return mBinding.getRoot();
    }

    private void updateUserInfo(){
        NetworkService service = new RestClient().getClient(getActivity()).create(NetworkService.class);
        Call<NetworkService.Login> call = service.repoUserInfo(mLoginInfo.getUserNo());
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int errorCode = response.body().errorCode;

                //서버상 문제가 없을때
                if (errorCode == 100){
                    mUserInfo = response.body().userInfos.get(0);

                    //유저정보 셋팅
                    Glide.with(getActivity()).load(mUserInfo.getUserImg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mBinding.userImageView);
                    mBinding.nickNameEditText.setText(mUserInfo.getUserNnm());
                    mBinding.birthEditText.setText(mUserInfo.getUserBr());
                    mBinding.introEditText.setText(mUserInfo.getUserIntro());

                    if (mUserInfo.getUserGn() == 0){
                        mBinding.genderEditText.setText("남성");
                    }else{
                        mBinding.genderEditText.setText("여성");
                    }

                }else{
                    //네트워크 오류일떄

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }

        });
    }

    private void checkProfile(){
        String nickName = mBinding.nickNameEditText.getText().toString();
        String intro = mBinding.introEditText.getText().toString();
        String birth = mBinding.birthEditText.getText().toString();
        String gender = mBinding.genderEditText.getText().toString();
        int genCode = 0;

        if (gender.equals("남성")){
            genCode = 0;
        }else if(gender.equals("여성")){
            genCode = 1;
        }

        updateProfile(mLoginInfo.getUserNo(), nickName, intro, birth, genCode);

    }

    private void updateProfile(int userNo, String nickName, String intro, String birth, int gender){

        NetworkService service = new RestClient().getClient(getActivity()).create(NetworkService.class);
        Call<ErrorCode> call = service.repoProfile(mLoginInfo.getUserNo(), nickName, intro, birth, gender);
        call.enqueue(new Callback<ErrorCode>() {
            @Override
            public void onResponse(Call<ErrorCode> call, Response<ErrorCode> response) {
                int errorCode = response.body().errorCode;

                //서버상 문제가 없을때
                if (errorCode == 100){
                    getActivity().finish();
                }else{
                    //네트워크 오류일떄

                }
            }

            @Override
            public void onFailure(Call<ErrorCode> call, Throwable t) {

            }
        });

    }

    //툴바 아이템 셋팅
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_profile, menu);

    }

    //툴바 아이템 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //새로운 객체 생성시 crime 객체를 DB에 추가시켜 놓음
            case R.id.menu_item_confirm:
                checkProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
