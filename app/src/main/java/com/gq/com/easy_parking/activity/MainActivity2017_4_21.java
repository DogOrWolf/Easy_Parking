package com.gq.com.easy_parking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.bean.ParkingSpotInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2017_4_21 extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, RouteSearch.OnRouteSearchListener {

    private MapView mMapView = null;
    private AMap aMap;
    private RouteSearch routeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBottomSiderBar();
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            MyLocationStyle locationStyle = new MyLocationStyle();
            locationStyle.myLocationIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setLocationSource(this);//设置定位监听
            aMap.setMyLocationEnabled(true);//显示定位层 并触发定位
            aMap.setMyLocationStyle(locationStyle);

            //设置点击Marker监听
            aMap.setOnMarkerClickListener(this);

            //设置控件
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setCompassEnabled(true);//指南针用于向 App 端用户展示地图方向，默认不显示。通过如下接口控制其显示：
            uiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示

            //初始化步行导航对象routeSearch
            routeSearch = new RouteSearch(this);
            routeSearch.setRouteSearchListener(this);
        }

        createMyMarkers();


    }

    private LinearLayout ll_mymap_bottomsidebar;
    private TextView tv_mymap_posinfo;
    private TextView tv_mymap_parkinginfo;
    private TextView tv_navigation;

    private void initBottomSiderBar() {
        ll_mymap_bottomsidebar = (LinearLayout) findViewById(R.id.ll_mymap_bottomsidebar);
        tv_mymap_posinfo = (TextView) findViewById(R.id.tv_mymap_posinfo);
        tv_mymap_parkinginfo = (TextView) findViewById(R.id.tv_mymap_parkinginfo);
        tv_navigation = (TextView) findViewById(R.id.tv_navigation);
        ll_mymap_bottomsidebar.setVisibility(View.GONE);

    }

    private String[] titles = {
            "继续教育前",
            "东门",
            "信息科学实验楼",
            "新食堂右侧",
            "中心教学楼"
    };
    private double[] lontitudes = {
            116.31171942,
            116.32179916,
            116.31866097,
            116.31374717,
            116.31666541
    };
    private double[] latitude = {
            39.95792016,
            39.95943744,
            39.95773512,
            39.95769811,
            39.95891523
    };
    //    private Marker myMarker;
    private List<ParkingSpotInfo> parking_data = new ArrayList<>();

    private void createMyMarkers() {
        //准备可以停车的车位数据
        for (int i = 0; i < titles.length; i++) {
            ParkingSpotInfo parkSpot = new ParkingSpotInfo(titles[i], lontitudes[i], latitude[i]);
            parking_data.add(parkSpot);
        }

        for (int i = 0; i < titles.length; i++) {
            if (aMap != null) {
                MarkerOptions options = setMyMarkerOptions(parking_data.get(i).getTitle()
                        , "剩余车位数：" + parking_data.get(i).getRest());

//            myMarker = new Marker(myMarkerOptions);
                LatLng latLng = new LatLng(parking_data.get(i).getLatitude()
                        , parking_data.get(i).getLongitude());
//            myMarker.setPosition(laLng);

                Marker myMarker = aMap.addMarker(options);
                myMarker.setPosition(latLng);
                myMarker.setObject(parking_data.get(i));
//                ScaleAnimation markAnimation = new ScaleAnimation(0,1,0,1);
//                markAnimation.setDuration(1500);

            }
        }

    }

    private MarkerOptions setMyMarkerOptions(String title, String content) {
        MarkerOptions myMarkerOptions = new MarkerOptions();
        myMarkerOptions.draggable(true);
//        myMarkerOptions.icon(
//                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        myMarkerOptions.icon(
                BitmapDescriptorFactory.fromResource(R.drawable.yellow_parking_64px_));
        myMarkerOptions.title(title);
        myMarkerOptions.visible(true);
        myMarkerOptions.snippet(content);


        return myMarkerOptions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();

        if (null != mapLocationClient) {
            mapLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private OnLocationChangedListener mListener;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

        if (mapLocationClient == null) {
            mapLocationClient = new AMapLocationClient(this);
            aMapLocationClientOption = new AMapLocationClientOption();
            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mapLocationClient.setLocationOption(aMapLocationClientOption);
            mapLocationClient.setLocationListener(this);

            mapLocationClient.startLocation();

        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }

        mapLocationClient = null;
    }

    private double current_latitude;
    private double current_longitude;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                current_latitude = aMapLocation.getLatitude();
                current_longitude = aMapLocation.getLongitude();


                mListener.onLocationChanged(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private LatLonPoint toPosition;
    private LatLonPoint fromPosition;
    private RouteSearch.FromAndTo fromAndTo;

    @Override
    public boolean onMarkerClick(final Marker marker) {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, ll_mymap_bottomsidebar.getHeight(), 0);
        AnimationSet animationSet = new AnimationSet(MainActivity2017_4_21.this, null);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(1000);
        animationSet.setFillBefore(true);
        ll_mymap_bottomsidebar.startAnimation(animationSet);
        ll_mymap_bottomsidebar.setVisibility(View.VISIBLE);

        if (marker.getObject() != null) {
            ParkingSpotInfo park_info = (ParkingSpotInfo) marker.getObject();
            tv_mymap_posinfo.setText(park_info.getTitle());
            tv_mymap_parkinginfo.setText("剩余车位:" + park_info.getRest());
        } else {
            tv_mymap_posinfo.setText("当前位置");
            tv_mymap_parkinginfo.setText("");
        }

        //步行路线规划
        if (tv_navigation != null) {
            tv_navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity2017_4_21.this, "导航路线规划中..", Toast.LENGTH_SHORT).show();

                    double longitude_toPosition = marker.getPosition().longitude;
                    double latitude_toPosition = marker.getPosition().latitude;
                    toPosition = new LatLonPoint(latitude_toPosition, longitude_toPosition);
                    fromPosition = new LatLonPoint(current_latitude, current_longitude);

                    fromAndTo = new RouteSearch.FromAndTo(fromPosition, toPosition);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RouteSearch.WalkRouteQuery walkQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
                            routeSearch.calculateWalkRouteAsyn(walkQuery);//计算路线 然后再监听的方法里实现路线图
                        }
                    }).start();

                }
            });
        }


        Toast.makeText(this, "" + marker.getId() + "---" + marker.getTitle()
                + "---经度" + marker.getPosition().longitude
                + "---纬度" + marker.getPosition().latitude, Toast.LENGTH_SHORT).show();
        return false;
    }


    //导航监听
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        Toast.makeText(MainActivity2017_4_21.this, "code::"+i, Toast.LENGTH_SHORT).show();
        if (i == 1000) {
            Toast.makeText(MainActivity2017_4_21.this, "path::"+walkRouteResult.getPaths().size(), Toast.LENGTH_SHORT).show();
            if (walkRouteResult.getPaths().size() > 0) {

                for (int j = 0; j < walkRouteResult.getPaths().size(); j++) {
                    WalkPath walkPath = walkRouteResult.getPaths().get(j);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            MainActivity2017_4_21.this, aMap, walkPath, fromPosition, toPosition);
                    aMap.clear();//从地图上删除所有的Marker，Overlay，Polyline 等覆盖物。
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.setNodeIconVisibility(false);
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                }
            }

        } else {
            Toast.makeText(MainActivity2017_4_21.this, "计算路线失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
