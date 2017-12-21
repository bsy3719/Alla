package com.fave.android.alla.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fave.android.alla.R;
import com.fave.android.alla.databinding.ActivityLoginBinding;
import com.fave.android.alla.item.LoginInfo;
import com.fave.android.alla.item.UserInfo;
import com.fave.android.alla.kakao.KakaoSignupActivity;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.NetworkService.Login;
import com.fave.android.alla.network.RestClient;
import com.fave.android.alla.util.MethodUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivitiy implements View.OnClickListener{

    private SessionCallback callback;      //콜백 선언

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton mSignInButton;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    private LoginInfo loginInfo = LoginInfo.getInstance();

    private EditText mIdEditText, mPasswordEidtText;
    private TextView mFindPasswordTextView, mSignupTextView;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("로그인");

        //자체로그인 셋팅
        setView();
        setGoogleLogin();
        setKakaoLogin();
    }

    private void setKakaoLogin(){
        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);
    }

    private void setGoogleLogin(){
        //구글 로그인 부분
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

        mSignInButton = findViewById(R.id.google_login_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this);
    }

    private void setView(){
        mIdEditText = findViewById(R.id.id_edit_text);
        mPasswordEidtText = findViewById(R.id.password_edit_text);
        mFindPasswordTextView = findViewById(R.id.find_password_text_view);
        mSignupTextView = findViewById(R.id.sign_up_text_view);
        mLoginButton = findViewById(R.id.login_button);

        mFindPasswordTextView.setOnClickListener(this);
        mSignupTextView.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else if  (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            outLogin(user.getEmail(), MethodUtil.getNickName(user.getEmail()), user.getPhotoUrl().toString(),1);
                            signOut();
                            loginInfo.setLoginFlag(true);
                            redirectMainActivity();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void inLoginCheck(){
        String id = mIdEditText.getText().toString();
        String password = mPasswordEidtText.getText().toString();

        if(id.getBytes().length <=0 || password.getBytes().length <=0){
            Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 비어있습니다.", Toast.LENGTH_SHORT).show();
        }else{
            inLogin(id, password, 0);
        }
    }

    private void inLogin(String id, String password, int gb){
        showProgressDialog();
        NetworkService service = new RestClient().getClient(LoginActivity.this).create(NetworkService.class);
        Call<Login> call = service.repoInLogin(id, password, gb);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                int errorCode = response.body().errorCode;

                //서버상 문제가 없을때
                if (errorCode == 100){
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    UserInfo userInfo = new UserInfo();
                    userInfo = response.body().userInfos.get(0);
                    //Log.d(TAG, userInfo.getUserId());
                    loginInfo.setLoginFlag(true);
                    loginInfo.setUserNo(userInfo.getUserNo());
                    redirectMainActivity();
                }else{
                    Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d(TAG, "로그인실패");
                t.printStackTrace();
                hideProgressDialog();
            }

        });
    }

    private void outLogin(String id, String nnm, String img, int gb){
        NetworkService service = new RestClient().getClient(LoginActivity.this).create(NetworkService.class);
        Call<Login> call = service.repoOutLogin(id, nnm, img, gb);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
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
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d(TAG, "로그인실패");
                t.printStackTrace();
            }

        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login);; // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    private void redirectMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    protected void redirectSignupActivity() { //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                inLoginCheck();
                break;
            case R.id.find_password_text_view:
                break;
            case R.id.google_login_button:
                signIn();
                break;
            case R.id.sign_up_text_view:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

}