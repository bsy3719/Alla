package com.fave.android.alla.kakao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fave.android.alla.activity.BaseActivitiy;
import com.fave.android.alla.activity.LoginActivity;
import com.fave.android.alla.activity.MainActivity;
import com.fave.android.alla.item.LoginInfo;
import com.fave.android.alla.item.UserInfo;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.RestClient;
import com.fave.android.alla.util.MethodUtil;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seungyeop on 2017-11-20.
 */

public class KakaoSignupActivity extends BaseActivitiy {

    private static final String TAG = "KakaoSignupActivity";

    private LoginInfo loginInfo = LoginInfo.getInstance();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() {
        //로딩 다이얼로그
        showProgressDialog();
        //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {

                //성공 시 userProfile 형태로 반환
                outLogin(userProfile.getEmail(), MethodUtil.getNickName(userProfile.getEmail()), userProfile.getThumbnailImagePath(), 2);
                loginInfo.setLoginFlag(true);
                onClickLogout();
                redirectMainActivity(); // 로그인 성공시 MainActivity로
                //로딩 다이얼로그 없앰
                hideProgressDialog();
            }
        });
    }



    private void outLogin(String id, String nnm, String img, int gb){
        NetworkService service = new RestClient().getClient(KakaoSignupActivity.this).create(NetworkService.class);
        Call<NetworkService.Login> call = service.repoOutLogin(id, nnm, img, gb);
        call.enqueue(new Callback<NetworkService.Login>() {
            @Override
            public void onResponse(Call<NetworkService.Login> call, Response<NetworkService.Login> response) {
                int errorCode = response.body().errorCode;
                Log.d(TAG, "로그인성공");

                //서버상 문제가 없을때
                if (errorCode == 501 || errorCode == 502){
                    UserInfo userInfo = new UserInfo();
                    userInfo = response.body().userInfos.get(0);
                    //Log.d(TAG, userInfo.getUserId());
                    loginInfo.setUserNo(userInfo.getUserNo());
                }else{

                }
            }

            @Override
            public void onFailure(Call<NetworkService.Login> call, Throwable t) {
                Log.d(TAG, "로그인실패");
                t.printStackTrace();
            }

        });

    }

    //카카오톡 로그아웃
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectMainActivity();
            }
        });
    }

    private void redirectMainActivity() {
        startActivity(new Intent(KakaoSignupActivity.this, MainActivity.class));
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
