package com.gq.com.easy_parking.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.bean.UserInfo;
import com.gq.com.easy_parking.common.AppNetConfig;
import com.gq.com.easy_parking.utils.MD5Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {


    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.ed_login_phone)
    EditText edLoginPhone;
    @Bind(R.id.ed_login_psw)
    EditText edLoginPsw;
    @Bind(R.id.btn_login_login)
    Button btnLoginLogin;
    @Bind(R.id.btn_login_back)
    Button btnLoginBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initTitle();
    }

    private void initTitle() {
        ivHeaderBack.setVisibility(View.VISIBLE);
        ivHeaderSetting.setVisibility(View.INVISIBLE);
        tvHeader.setText("用户登陆");
    }

    @OnClick({R.id.btn_login_back,R.id.iv_header_back})
    public void Back() {
        finish();
    }

    @OnClick(R.id.btn_login_login)
    public void login() {
        String phoneNubmer = edLoginPhone.getText().toString().trim();
        String password = edLoginPsw.getText().toString().trim();

        if (!password.isEmpty() && !phoneNubmer.isEmpty()) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("phone", phoneNubmer);
            requestParams.put("password", MD5Utils.MD5(password));
            asyncHttpClient.post(AppNetConfig.LOGIN, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {

                    JSONObject jsonObject = JSON.parseObject(content);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String data = jsonObject.getString("data");
                        UserInfo userInfo = JSON.parseObject(data, UserInfo.class);

                        SharedPreferences sp = LoginActivity.this.getSharedPreferences("user_info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("name", userInfo.getName());
                        editor.putString("imageurl", userInfo.getImageurl());
                        editor.putString("phone", userInfo.getPhone());
                        editor.putBoolean("iscredit", userInfo.iscredit());
                        editor.commit();

                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Throwable error, String content) {
                    Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }

    }
}
