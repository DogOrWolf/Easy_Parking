package com.gq.com.easy_parking.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.com.easy_parking.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends Activity {

    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.iv_userinfo_bg)
    ImageView ivUserinfoBg;
    @Bind(R.id.iv_userinfo_img)
    ImageView ivUserinfoImg;
    @Bind(R.id.tv_userinfo_changeimg)
    TextView tvUserinfoChangeimg;
    @Bind(R.id.btn_userinfo_logout)
    Button btnUserinfoLogout;
    @Bind(R.id.ll_header)
    LinearLayout llHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        initTitles();
    }

    private void initTitles() {
        ivHeaderBack.setVisibility(View.VISIBLE);
        ivHeaderSetting.setVisibility(View.INVISIBLE);
        tvHeader.setText("用户信息");

        llHeader.setBackgroundColor(getResources().getColor(R.color.divider_line));

    }

    @OnClick(R.id.iv_header_back)
    public void back(){
        setBackActivityResult(null);
        finish();
    }

    @OnClick(R.id.btn_userinfo_logout)
    public void logout(){
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        if(sp.getString("name","").isEmpty()){
            Toast.makeText(this,"您还没有登陆",Toast.LENGTH_SHORT).show();
        }else {
            sp.edit().clear().commit();
            setBackActivityResult(true);
            Toast.makeText(this,"退出成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private int resultcode = 88;
    public void setBackActivityResult(Boolean isLogout) {
        if(null == isLogout){
            setResult(resultcode,null);
        }else {
            Intent data = new Intent();
            data.putExtra("isLogout",isLogout.booleanValue());
            setResult(resultcode,data);
        }
    }
}
