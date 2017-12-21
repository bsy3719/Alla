package com.fave.android.alla.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fave.android.alla.R;
import com.fave.android.alla.activity.LoginActivity;
import com.fave.android.alla.activity.SignUpActivity;
import com.fave.android.alla.databinding.FragmentSignUpBinding;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.RestClient;
import com.fave.android.alla.util.MethodUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding mBinding;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        mBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpCheck();
            }
        });

        return mBinding.getRoot();
    }

    private void signUpCheck(){
        String id = mBinding.idEditText.getText().toString();
        String password = mBinding.passwordEditText.getText().toString();
        String rePassword = mBinding.passwordReEditText.getText().toString();

        if (id.getBytes().length <=0 || password.getBytes().length <=0 || rePassword.getBytes().length <= 0){
            Toast.makeText(getActivity(), "비어있는 칸이 있습니다.", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(rePassword)){
            Toast.makeText(getActivity(), "비밀번호와 비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT).show();
        }else{
            String nickName = MethodUtil.getNickName(id);
            signUp(id, password, nickName);
        }

    }

    private void signUp(String id, String password, String nickName){
        NetworkService service = new RestClient().getClient(getActivity()).create(NetworkService.class);
        Call<NetworkService.ErrorCode> call = service.repoSignUp(id, password, nickName);
        call.enqueue(new Callback<NetworkService.ErrorCode>() {
            @Override
            public void onResponse(Call<NetworkService.ErrorCode> call, Response<NetworkService.ErrorCode> response) {
                int errorCode = response.body().errorCode;
                //서버상 문제가 없을때
                if (errorCode == 100){
                    Toast.makeText(getActivity(), "회원가입 성공!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    Toast.makeText(getActivity(), "네트워크 오류입니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NetworkService.ErrorCode> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

}
