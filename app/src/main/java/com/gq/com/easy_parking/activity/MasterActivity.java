package com.gq.com.easy_parking.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.bean.ParkingMarker;
import com.gq.com.easy_parking.bean.ParkingSpotInfo;
import com.gq.com.easy_parking.fragment.HomeFragment;
import com.gq.com.easy_parking.fragment.MeFragment;
import com.gq.com.easy_parking.fragment.MyMapFragment;
import com.gq.com.easy_parking.fragment.SpotDetailFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MasterActivity extends FragmentActivity {

    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.iv_main_home)
    ImageView ivMainHome;
    @Bind(R.id.tv_main_home)
    TextView tvMainHome;
    @Bind(R.id.ll_main_home)
    LinearLayout llMainHome;
    @Bind(R.id.iv_main_mymap)
    ImageView ivMainMymap;
    @Bind(R.id.tv_main_mymap)
    TextView tvMainMymap;
    @Bind(R.id.ll_main_mymap)
    LinearLayout llMainMymap;
    @Bind(R.id.iv_main_spotdetail)
    ImageView ivMainSpotdetail;
    @Bind(R.id.tv_main_spotdetail)
    TextView tvMainSpotdetail;
    @Bind(R.id.ll_main_spotdetail)
    LinearLayout llMainSpotdetail;
    @Bind(R.id.iv_main_me)
    ImageView ivMainMe;
    @Bind(R.id.tv_main_me)
    TextView tvMainMe;
    @Bind(R.id.ll_main_me)
    LinearLayout llMainMe;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ButterKnife.bind(this);

        fragmentManager = this.getSupportFragmentManager();
        getSelect(0);
    }
    @OnClick({R.id.ll_main_home,R.id.ll_main_mymap,R.id.ll_main_spotdetail,R.id.ll_main_me})
    public void showFrag(View view){
        switch (view.getId()){
            case R.id.ll_main_home:
                getSelect(0);
                break;
            case R.id.ll_main_mymap:
                getSelect(1);
                break;
            case R.id.ll_main_spotdetail:
                getSelect(2);
                break;
            case R.id.ll_main_me:
                getSelect(3);
                break;
        }
    }
    private HomeFragment homeFragment;
    private MyMapFragment myMapFragment;
    private SpotDetailFragment spotDetailFragment;
    private MeFragment meFragment;
    public void getSelect(int i) {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments();
        resetTabs();
        switch (i){
            case 0:
                if(null == homeFragment){
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fl_main,homeFragment);
                }
                fragmentTransaction.show(homeFragment);
                ivMainHome.setBackgroundResource(R.drawable.main_bottom_selected);
                tvMainHome.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 1:
                if(null == myMapFragment){
                    myMapFragment = new MyMapFragment();
                    fragmentTransaction.add(R.id.fl_main,myMapFragment);
                }
                fragmentTransaction.show(myMapFragment);
                ivMainMymap.setBackgroundResource(R.drawable.map_selected);
                tvMainMymap.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;

            case 2:
                if(null == spotDetailFragment){
                    spotDetailFragment = new SpotDetailFragment();
                    fragmentTransaction.add(R.id.fl_main,spotDetailFragment);
                }
                fragmentTransaction.show(spotDetailFragment);
                ivMainSpotdetail.setBackgroundResource(R.drawable.main_bottom_detail_selected);
                tvMainSpotdetail.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 3:
                if(null == meFragment){
                    meFragment = new MeFragment();
                    fragmentTransaction.add(R.id.fl_main,meFragment);
                }
                fragmentTransaction.show(meFragment);
                ivMainMe.setBackgroundResource(R.drawable.bottom06);
                tvMainMe.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
        }

        fragmentTransaction.commit();
    }

    private void resetTabs() {
        ivMainHome.setBackgroundResource(R.drawable.main_bottom);
        ivMainMymap.setBackgroundResource(R.drawable.map);
        ivMainMe.setBackgroundResource(R.drawable.bottom05);
        ivMainSpotdetail.setBackgroundResource(R.drawable.main_bottom_detail);
        tvMainHome.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainMymap.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainMe.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainSpotdetail.setTextColor(getResources().getColor(R.color.home_back_unselected));
    }

    private void hideFragments() {
        if(null != homeFragment){
            fragmentTransaction.hide(homeFragment);
        }
        if(null != myMapFragment){
            fragmentTransaction.hide(myMapFragment);
        }
        if(null != meFragment){
            fragmentTransaction.hide(meFragment);
        }
        if(null != spotDetailFragment){
            fragmentTransaction.remove(spotDetailFragment);//因为这个页面要经常刷新 所以删除 从而保证spotDetailFragment里的resume方法执行
            spotDetailFragment = null;
        }
    }
    //getSelect当是第三项车位详情时需要传入对象ParkingSpotInfo
    public ParkingMarker spotInfo;
    public void getSelect(int i, ParkingMarker info){
        this.spotInfo = info;
        getSelect(i);
    }
    //点击两次退出应用
    private Boolean isFirstBack = true;
    private final int WHAT_BACK = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == WHAT_BACK){
                isFirstBack = true;
            }
        }
    };
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK ){
            if(isFirstBack){
                Toast.makeText(this,"点击两次退出应用",Toast.LENGTH_SHORT).show();
                isFirstBack = false;
//                这么写可能存在内存泄漏
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isFirstBack = true;
//                    }
//                },2000);
                handler.sendEmptyMessageDelayed(WHAT_BACK,2000);
                return true;
            }else{
                return super.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
