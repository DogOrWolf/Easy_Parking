package com.gq.com.easy_parking.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.amap.api.maps2d.model.Marker;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.NaviLatLng;
import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.listener.MyAMapNaviListener;
import com.gq.com.easy_parking.listener.MyAMapNaviViewListener;
import com.gq.com.easy_parking.utils.CodeUtils;

public class NavActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        initNavView(savedInstanceState);
        AMapNavi mapNavi = AMapNavi.getInstance(this);
        double from_latitude = getIntent().getDoubleExtra("from_latitude", 0);
        double from_longitude = getIntent().getDoubleExtra("from_longitude", 0);

        double to_latitude = getIntent().getDoubleExtra("to_latitude", 0);
        double to_longitude = getIntent().getDoubleExtra("to_longitude", 0);

        if(0 != to_latitude && 0 != to_longitude && 0 != from_latitude && 0 != from_longitude){
            NaviLatLng from_latlng = new NaviLatLng(from_latitude,from_longitude);
            NaviLatLng to_latlng = new NaviLatLng(to_latitude,to_longitude);

            mapNavi.addAMapNaviListener(new MyAMapNaviListener(mapNavi,from_latlng,to_latlng,NavActivity.this));
        }
    }


    private AMapNaviView navView;
    private void initNavView(Bundle savedInstanceState) {
        if(navView == null){
            navView = (AMapNaviView) findViewById(R.id.navi_view);
        }
        navView.onCreate(savedInstanceState);
        navView.setAMapNaviViewListener(new MyAMapNaviViewListener());
    }


    @Override
    protected void onResume() {
        super.onResume();
        navView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        navView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
//        navView.stopNavi();
//        navView.destroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (event.getAction()){
//            case KeyEvent.KEYCODE_BACK:
//                finish();
//                break;
//        }
        return super.onKeyUp(keyCode, event);
    }
}
