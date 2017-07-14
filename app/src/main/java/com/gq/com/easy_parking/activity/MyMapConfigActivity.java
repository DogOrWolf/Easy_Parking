package com.gq.com.easy_parking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.utils.CodeUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MyMapConfigActivity extends Activity {

    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.ll_header)
    LinearLayout llHeader;
    @Bind(R.id.sticky_switch)
    StickySwitch stickySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map_config);
        ButterKnife.bind(this);

        initPage();
    }

    private void initPage() {
        ivHeaderBack.setVisibility(View.VISIBLE);
        ivHeaderSetting.setVisibility(View.INVISIBLE);
        tvHeader.setText("地图设置选项");
        llHeader.setBackgroundColor(getResources().getColor(R.color.setting_background));

        if(isWalk){
            stickySwitch.setDirection(StickySwitch.Direction.LEFT);
        }else {
            stickySwitch.setDirection(StickySwitch.Direction.RIGHT);
        }

        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener(){

            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
//                CodeUtils.showToast(MyMapConfigActivity.this,direction.name()+"Text:"+text);
                isWalk =(direction ==StickySwitch.Direction.LEFT) ? true : false;
//                CodeUtils.showToast(MyMapConfigActivity.this,"Left:"+isWalk);
            }
        });
    }

    public static boolean isWalk = true;//默认是在左边 即步行模式
}
