package com.fave.android.alla.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.fave.android.alla.R;
import com.fave.android.alla.databinding.ActivityMainBinding;
import com.fave.android.alla.fragment.CardNewsListFragment;
import com.fave.android.alla.fragment.MyPageFragment;
import com.fave.android.alla.item.LoginInfo;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private LoginInfo loginInfo = LoginInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initFragment();
        setBottomNavi();
    }

    private void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, CardNewsListFragment.newInstance()).commitAllowingStateLoss();
    }

    private void setBottomNavi(){

        AHBottomNavigationItem homeItem = new AHBottomNavigationItem(R.string.tab_home, R.drawable.ic_bottom_home, R.color.colorPrimary);
        AHBottomNavigationItem peopleItem = new AHBottomNavigationItem(R.string.tab_write, R.drawable.ic_menu_edit, R.color.colorWhite);
        AHBottomNavigationItem personItem = new AHBottomNavigationItem(R.string.tab_person, R.drawable.ic_bottom_person, R.color.colorWhite);

        mBinding.bottomNavigation.addItem(homeItem);
        mBinding.bottomNavigation.addItem(peopleItem);
        mBinding.bottomNavigation.addItem(personItem);

        // Set current item programmatically
        mBinding.bottomNavigation.setCurrentItem(0);

        // Manage titles
        //mBinding.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        mBinding.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //mBinding.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // 클릭시 아이콘색
        mBinding.bottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimary));
        // 평소 아이콘색
        mBinding.bottomNavigation.setInactiveColor(getResources().getColor(R.color.colorDarkGray));

        // 바텀바 백그라운드 색
        //mBinding.bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // 스크롤시 바텀바가 사라질지 유무
        //mBinding.bottomNavigation.setBehaviorTranslationEnabled(false);

        // Enable the translation of the FloatingActionButton
        //mBinding.bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);



        // Force to tint the drawable (useful for font with icon for example)
        //mBinding.bottomNavigation.setForceTint(true);

        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21
        // <item name="android:windowTranslucentNavigation">true</item>
        // <item name="android:fitsSystemWindows">true</item>
        //mBinding.bottomNavigation.setTranslucentNavigationEnabled(true);

        // Use colored navigation with circle reveal effect
        //mBinding.bottomNavigation.setColored(true);

        // Customize notification (title, background, typeface)
        //mBinding.bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        // Add or remove notification for each item
        //mBinding.bottomNavigation.setNotification("1", 2);
        // OR
        /*AHNotification notification = new AHNotification.Builder()
                .setText("1")
                .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
                .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
                .build();
        mBinding.bottomNavigation.setNotification(notification, 1);*/

        // Enable / disable item & set disable color
        //mBinding.bottomNavigation.enableItemAtPosition(2);
        //mBinding.bottomNavigation.disableItemAtPosition(2);
        //mBinding.bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

        // Set listeners
        mBinding.bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(position == 0){
                    transaction.replace(R.id.fragment_container, CardNewsListFragment.newInstance()).addToBackStack(null).commit();
                }else if (position == 1){
                    if (loginInfo.isLoginFlag()){
                        startActivity( new Intent(MainActivity.this, CardNewsWritePagerActivity.class));
                    }else {
                        showLoginDialog();
                    }
                    //transaction.replace(R.id.fragment_container, GroupFragment.newInstance()).addToBackStack(null).commit();
                }else if(position == 2){
                    transaction.replace(R.id.fragment_container, MyPageFragment.newInstance()).addToBackStack(null).commit();
                }

                return true;
            }
        });

        /*mBinding.bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        MenuItem loginItem = menu.findItem(R.id.menu_item_login);

        if (loginInfo.isLoginFlag()){
            loginItem.setTitle(R.string.logout);
        }else {
            loginItem.setTitle(R.string.login);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_card_news_list:
                if (loginInfo.isLoginFlag()){
                    startActivity( new Intent(MainActivity.this, CardNewsWritePagerActivity.class));
                }else {
                    showLoginDialog();
                }
                return true;
            case R.id.menu_item_login:
                if (loginInfo.isLoginFlag()){
                    loginInfo.setLoginFlag(false);
                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void showLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그인이 필요한 서비스입니다")
                .setCancelable(true)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
