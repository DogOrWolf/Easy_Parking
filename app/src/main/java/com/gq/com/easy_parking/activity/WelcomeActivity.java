package com.gq.com.easy_parking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.gq.com.easy_parking.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    private final int WHAT_WELCOME = 1;
    @Bind(R.id.rl_welcome)
    RelativeLayout rlWelcome;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_WELCOME) {
                startActivity(new Intent(WelcomeActivity.this, MasterActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(4000);
        rlWelcome.setAnimation(animation);
        handler.sendEmptyMessageDelayed(WHAT_WELCOME, 4000);
    }
}
