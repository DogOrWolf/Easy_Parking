package com.gq.com.easy_parking.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.activity.LoginActivity;
import com.gq.com.easy_parking.activity.UserInfoActivity;
import com.gq.com.easy_parking.utils.BitmapUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hasee on 2017/4/21.
 */
public class MeFragment extends Fragment {

    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.ll_header)
    LinearLayout llHeader;
    @Bind(R.id.iv_user_bg)
    ImageView ivUserBg;
    @Bind(R.id.iv_user_img)
    ImageView ivUserImg;
    @Bind(R.id.rl_user_icon)
    RelativeLayout rlUserIcon;
    @Bind(R.id.tv_user_login)
    TextView tvUserLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        initTitle();
        if (LogoutFlag) {
            isLogin();
        }
        return view;
    }

    private boolean LogoutFlag = true;//记录当前是否登陆 默认是未登录

    //判断当前是否登陆
    private void isLogin() {
//        Toast.makeText(getActivity(),"isLogin()",Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (null == sp || name.isEmpty()) {
            tvUserLogin.setText("请点击头像登陆");
        } else {
            //获取个人信息并显示 将状态改变为登陆
            tvUserLogin.setText(name);
//            Picasso.with(getActivity()).load(sp.getString("imageurl","")).into(ivUserImg);
            Picasso.with(getActivity()).load(sp.getString("imageurl", "")).transform(new Transformation() {
                //            Picasso.with(getActivity()).load(sp.getString("imageurl","")).networkPolicy(NetworkPolicy.NO_CACHE).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Bitmap bitmap = BitmapUtils.circleBitmap(source);
                    source.recycle();
                    return bitmap;
                }

                @Override
                public String key() {
                    return "";
                }
            }).into(ivUserImg);
            LogoutFlag = false;//表示登陆了
        }

    }

    //如果是喂登陆状态就点击转到登陆页面 否则 不响应
    @OnClick(R.id.rl_user_icon)
    public void toLoginPage() {
        if (LogoutFlag) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initTitle() {
        ivHeaderBack.setVisibility(View.INVISIBLE);
        ivHeaderSetting.setVisibility(View.VISIBLE);
        tvHeader.setText("我的信息");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        //页面重新加载时 判断是否登陆了
        if (LogoutFlag) {
            isLogin();
        }
        super.onResume();
    }

    //设置部分 退出后将状态更改为未登录
    private int request_code = 66;

    @OnClick(R.id.iv_header_setting)
    public void setting() {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        startActivityForResult(intent, request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 66:
                if (data != null) {
                    LogoutFlag = data.getBooleanExtra("isLogout", false);
                }
        }
    }

    @Override
    public void onDestroy() {
        SharedPreferences sp = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        if (sp.getString("name", "").isEmpty()) {
            Toast.makeText(getActivity(), "您还没有登陆", Toast.LENGTH_SHORT).show();
        } else {
            sp.edit().clear().commit();
        }
        super.onDestroy();
    }
}
