package com.fave.android.alla.activity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fave.android.alla.fragment.CardNewsWriteItemFragment;
import com.fave.android.alla.item.LoginInfo;
import com.fave.android.alla.item.WriteItem;
import com.fave.android.alla.network.RestClient;
import com.fave.android.alla.R;
import com.fave.android.alla.util.ImageUtil;
import com.fave.android.alla.databinding.ActivityCardNewsWritePagerBinding;
import com.fave.android.alla.network.NetworkService;
import com.fave.android.alla.network.NetworkService.CardNewsListWrite;
import com.fave.android.alla.network.NetworkService.CardNewsItemWrite;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardNewsWritePagerActivity extends BaseActivitiy implements CardNewsWriteItemFragment.WriteListener{

    private static final int REQUEST_CODE_IMAGE_PICKER = 200;
    private static final String TAG = "CardNewsWriteAcitvity";

    private ActivityCardNewsWritePagerBinding mBinding;
    private List<Uri> mUriList;
    private List<WriteItem> mWriteItemList = new ArrayList<>();

    private LoginInfo mLoginInfo = LoginInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_card_news_write_pager);
        getSupportActionBar().setTitle("글쓰기");

        checkPermission();
    }

    //tedPermission을 이용하여 퍼미션 체크
    private void checkPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getPhoto();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(CardNewsWritePagerActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    //fishbun을 이용하여 사진을 가져옴
    private void getPhoto() {
        FishBun.with(this).MultiPageMode()
                .setIsUseDetailView(false)
                .setCamera(true)
                .exceptGif(true)
                .setButtonInAlbumActivity(true)
                .setRequestCode(REQUEST_CODE_IMAGE_PICKER)
                .setActionBarColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark), false)
                .startAlbum();
    }

    //사진 uri리스트를 가져옴
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_PICKER:
                if (resultCode == RESULT_OK) {
                    mUriList = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    getCompressedImageFile();
                    setAdapter();
                    break;
                }else{
                    finish();
                }
        }
    }

    //이미지 파일 압축
    private void getCompressedImageFile(){
        for (int i = 0; i < mUriList.size(); i++) {
            String path = ImageUtil.getPath(CardNewsWritePagerActivity.this, mUriList.get(i));

            try {
                File compressedImageFile = new Compressor(this).compressToFile(new File(path));

                if ( compressedImageFile != null){
                    mWriteItemList.add(new WriteItem(compressedImageFile, ""));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //뷰페이저 어뎁터를 셋팅
    private void setAdapter(){
        FragmentManager fm = getSupportFragmentManager();
        mBinding.cardNewsWriteViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                String path = ImageUtil.getPath(CardNewsWritePagerActivity.this, mUriList.get(position));
                return CardNewsWriteItemFragment.newInstance(path, position);
            }
            @Override
            public int getCount() {
                return mUriList.size();
            }
        });
    }

    //카드 뉴스 리스트 쓰기
    private void wirteCardNews(){
        NetworkService service = new RestClient().getClient(this).create(NetworkService.class);
        Call<CardNewsListWrite> call = service.repoCardNewsWrite(mLoginInfo.getUserNo(), 0, 0);
        call.enqueue(new Callback<CardNewsListWrite>() {
            @Override
            public void onResponse(Call<CardNewsListWrite> call, Response<CardNewsListWrite> response) {
                int errorCode = response.body().errorCode;
                int lastId = response.body().lastId;

                if (errorCode == 100){
                    for (int i = 0; i < mWriteItemList.size(); i++) {
                        File imageFile = mWriteItemList.get(i).getImageFile();
                        String content = mWriteItemList.get(i).getContent();
                        uploadPhoto(imageFile, i, lastId, content);
                    }
                }

            }
            @Override
            public void onFailure(Call<CardNewsListWrite> call, Throwable t) {

            }
        });
    }

    //사진 업로드
    private void uploadPhoto(File imageFile, int sq, int no, String content){
        //fileImages - File리스트
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", imageFile.getPath(), RequestBody.create(MediaType.parse("file:"), imageFile));
        MultipartBody.Part partSq = MultipartBody.Part.createFormData("CNI_SQ", Integer.toString(sq));
        MultipartBody.Part partNo = MultipartBody.Part.createFormData("CNL_NO", Integer.toString(no));
        MultipartBody.Part partContent = MultipartBody.Part.createFormData("CNI_CONTENT", content);

        NetworkService service = new RestClient().getClient(this).create(NetworkService.class);
        Call<CardNewsItemWrite> call = service.repoCardNewsItemWrite(partFile, partSq, partNo, partContent);
        call.enqueue(new Callback<CardNewsItemWrite>() {
            @Override
            public void onResponse(Call<CardNewsItemWrite> call, Response<CardNewsItemWrite> response) {
                int errorCode = response.body().errorCode;
                int sq = response.body().sq;

                Log.d(TAG, String.valueOf(errorCode) + String.valueOf(sq));

                if (errorCode == 100){
                    //Toast.makeText(CardNewsWritePagerActivity.this, "업로드 완료했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                }

            }

            @Override
            public void onFailure(Call<CardNewsItemWrite> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_card_news_write_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_card_news_write:
                showProgressDialog();
                wirteCardNews();
                hideProgressDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //프래그먼트로부터 데이터를 받아옴
    @Override
    public void onReceivedData(String content, int position) {
        mWriteItemList.get(position).setContent(content);
    }
}
