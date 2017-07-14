package com.gq.com.easy_parking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.activity.MasterActivity;
import com.gq.com.easy_parking.activity.MyMapConfigActivity;
import com.gq.com.easy_parking.activity.NavActivity;
import com.gq.com.easy_parking.bean.ParkingMarker;
import com.gq.com.easy_parking.bean.ParkingSpotInfo;
import com.gq.com.easy_parking.common.AppNetConfig;
import com.gq.com.easy_parking.utils.CodeUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2017/4/21.
 */
public class MyMapFragment extends SupportMapFragment implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, RouteSearch.OnRouteSearchListener {


    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.ll_header)
    LinearLayout llHeader;
    @Bind(R.id.iv_close_mymap)
    ImageView ivCloseMymap;
    @Bind(R.id.tv_mymap_posinfo)
    TextView tvMymapPosinfo;
    @Bind(R.id.tv_mymap_parkinginfo)
    TextView tvMymapParkinginfo;
    @Bind(R.id.tv_mymap_parkingspotdetail)
    TextView tvMymapParkingspotdetail;
    @Bind(R.id.tv_navigation)
    TextView tvNavigation;
    @Bind(R.id.tv_mymap_navgation3D)
    TextView tvMymapNavgation3D;
    @Bind(R.id.ll_mymap_bottomsidebar)
    LinearLayout llMymapBottomsidebar;
    @Bind(R.id.ll_mymap)
    LinearLayout llMymap;


    private MapView myMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_mymap, null);
        myMap = (MapView) view.findViewById(R.id.my_map);
        myMap.onCreate(savedInstanceState);
        ButterKnife.bind(this, view);
        initTitle();
        initBottomSiderBar();
        initMap();
        initMarkerData();
//        createMyMarkers();//初始化停车位marker  这里写在异步初始化marker数据里了

//        view.setFocusable(true);
//        view.setFocusableInTouchMode(true);
//        llMymap.setOnKeyListener(myKeyListener);//设置key监听
        return view;
    }

    //初始化头部标题
    private void initTitle() {
        ivHeaderBack.setVisibility(View.INVISIBLE);
        ivHeaderSetting.setVisibility(View.VISIBLE);
        tvHeader.setText("地图");

        //点击设置
        ivHeaderSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amap.clear();
                re_locate();
                llMymapBottomsidebar.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), MyMapConfigActivity.class));
            }
        });
    }

    //初始化侧边栏（底部）
    private void initBottomSiderBar() {
        llMymapBottomsidebar.setVisibility(View.GONE);

        //点击关闭
        ivCloseMymap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re_locate();
                llMymapBottomsidebar.setVisibility(View.GONE);
            }
        });

    }

    //ButterKnife
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /*******************************
     * Map初始化以及定位功能*******start
     ************************************/


    //Map
    private AMap amap;

    //初始化Map
    private void initMap() {
        if (amap == null) {
            amap = myMap.getMap();
            //注意这个设置监听必须在setMyLocationEnabled(true)触发之前 不然第一次初始化页面时候不定位
            amap.setLocationSource(this);//定位监听
            amap.moveCamera(CameraUpdateFactory.zoomTo(16));
            //设置图标样式
            MyLocationStyle style = new MyLocationStyle();
            style.myLocationIcon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //设置定位图标样式是否显示
            amap.getUiSettings().setMyLocationButtonEnabled(true);
            amap.setMyLocationEnabled(true);//true显示定位层并可触发定位 默认是false
            amap.setMyLocationStyle(style);

            //设置控件
            UiSettings uiSettings = amap.getUiSettings();
            uiSettings.setCompassEnabled(true);//指南针用于向 App 端用户展示地图方向，默认不显示。通过如下接口控制其显示：
            uiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
            uiSettings.setZoomControlsEnabled(true);
//            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            //设置点击Marker监听
            amap.setOnMarkerClickListener(this);

            //初始化步行导航对象routeSearch
            routeSearch = new RouteSearch(getActivity());
            routeSearch.setRouteSearchListener(this);

        }
    }

    //实现LocationSource的回调方法 定位
    private AMapLocationClient location_client;//定位的主类
    private AMapLocationClientOption location_client_option;//定位的选项
    private OnLocationChangedListener locationChangedListener;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        Toast.makeText(getActivity(),"location_client"+location_client,Toast.LENGTH_SHORT).show();
        locationChangedListener = onLocationChangedListener;
        if (location_client == null) {
            location_client = new AMapLocationClient(getActivity());
            location_client_option = new AMapLocationClientOption();
            //设置option 可以看定位的api
            location_client_option.setLocationMode(
                    AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            location_client.setLocationOption(location_client_option);
            location_client.setLocationListener(this);//设置定位监听

            location_client.startLocation();
//            Toast.makeText(getActivity(),"location_client"+location_client.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private double current_latitude;
    private double current_longitude;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (locationChangedListener != null && location_client != null) {
            if (aMapLocation.getErrorCode() == 0) {
                current_latitude = aMapLocation.getLatitude();
                current_longitude = aMapLocation.getLongitude();

                locationChangedListener.onLocationChanged(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
        //取消定位 并置空
        if (location_client != null) {
            location_client.stopLocation();
            location_client.onDestroy();
        }
        location_client = null;
    }

    //重新定位 并删除所有标识
    private void re_locate() {
        amap.clear();
        amap.moveCamera(CameraUpdateFactory.zoomTo(16));
        amap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
        //设置图标样式
        MyLocationStyle style = new MyLocationStyle();
        style.myLocationIcon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        //设置定位图标样式是否显示
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        amap.setMyLocationEnabled(true);//true显示定位层并可触发定位 默认是false
        amap.setMyLocationStyle(style);


        createMyMarkers();//停车位marker

    }
/*****************************Map初始化以及定位功能*******end********************************************/


/************************ 停车位marker*******start***************************************/


    //    private Marker myMarker;
//    private List<ParkingSpotInfo> parking_data = new ArrayList<>();
    private List<ParkingMarker> parking_data = new ArrayList<>();

    //创建markers
    private void createMyMarkers() {
//        initMarkerData();//初始化marker数据

        for (int i = 0; i < parking_data.size(); i++) {
            if (amap != null) {
                MarkerOptions options = setMyMarkerOptions(parking_data.get(i).getTitle()
                        , "剩余车位数：" + parking_data.get(i).getRest());

                LatLng latLng = new LatLng(parking_data.get(i).getLatitude()
                        , parking_data.get(i).getLongitude());

                Marker myMarker = amap.addMarker(options);
                myMarker.setPosition(latLng);
                myMarker.setObject(parking_data.get(i));

            }
        }
    }

    //配置marker选项
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


    private void initMarkerData() {
//        //实际数据是联网获取的 这部分用于测试
//        for (int i = 0; i < titles.length; i++) {
//            ParkingMarker parkSpot =
//                    new ParkingMarker(ids[i], titles[i],
//                            lontitudes[i], latitude[i], true, rests[i], capacity[i]);
//            parking_data.add(parkSpot);
//        }

        //异步加载：
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String AllMarkers_Url = AppNetConfig.ALLMARKERS;
        asyncHttpClient.post(AllMarkers_Url,null,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
//                JSONObject jsonObject =  JSON.parseObject(content);
//                String str = jsonObject.toJSONString();
                parking_data = JSON.parseArray(content, ParkingMarker.class);

                createMyMarkers();
                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(getActivity(),"加载停车地点失败",Toast.LENGTH_SHORT).show();
                super.onFailure(error, content);
            }
        });
//同步加载：
//                SyncHttpClient syncHttpClient = new SyncHttpClient() {
//                    @Override
//                    public String onRequestFailed(Throwable throwable, String s) {
//                        Toast.makeText(getActivity(),"加载停车地点失败",Toast.LENGTH_SHORT).show();
//                        return null;
//                    }
//                };
//                String AllMarkers_Url = AppNetConfig.ALLMARKERS;
//                syncHttpClient.post(AllMarkers_Url,null,new AsyncHttpResponseHandler(){
//                    @Override
//                    public void onSuccess(String content) {
//                        if(null != content){
//                            JSONObject jsonObject =  JSON.parseObject(content);
//                            String str = jsonObject.toJSONString();
//                            parking_data = JSON.parseArray(str, ParkingMarker.class);
//
//
//                        }
//                        super.onSuccess(content);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable error, String content) {
//                        Toast.makeText(getActivity(),"加载停车地点失败",Toast.LENGTH_SHORT).show();
//                        super.onFailure(error, content);
//                    }
//                });


    }


    //默认的marker数据 实际数据是联网获取的 这部分用于测试
    private int[] ids = {
            1, 2, 3, 4, 5
    };
    private String[] titles = {
            "继续教育前",
            "东门",
            "信息科学实验楼",
            "新食堂右侧",
            "中心教学楼"
    };
    private int[] rests = {
            3, 10, 20, 8, 6
    };
    private int[] capacity = {
            30, 20, 50, 60, 40
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

    //amap点击marker的监听 并设置步行路线导航
    private LatLonPoint toPosition;
    private LatLonPoint fromPosition;
    private RouteSearch.FromAndTo fromAndTo;
    private Marker currentMarker;
    private RouteSearch routeSearch;

    @Override
    public boolean onMarkerClick(final Marker marker) {

        currentMarker = marker;

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, llMymapBottomsidebar.getHeight(), 0);
        AnimationSet animationSet = new AnimationSet(getActivity(), null);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(1000);
        animationSet.setFillBefore(true);
        llMymapBottomsidebar.startAnimation(animationSet);
        amap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        llMymapBottomsidebar.setVisibility(View.VISIBLE);

        if (marker.getObject() != null) {
            ParkingMarker park_info = (ParkingMarker) marker.getObject();
            tvMymapPosinfo.setText(park_info.getTitle());
            tvMymapParkinginfo.setText("剩余车位:" + park_info.getRest());
        } else {
            tvMymapPosinfo.setText("当前位置:--->");//这里有个marker获得经纬度变为文字位置的方法
            tvMymapParkinginfo.setText("经纬度变为文字");
        }

        //点击路线规划（步行模式和汽车模式）路线规划Navigation  导航名叫Navigation_3D
        if (tvNavigation != null) {
            tvNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity(), "导航路线规划中..", Toast.LENGTH_SHORT).show();
                    double longitude_toPosition = currentMarker.getPosition().longitude;
                    double latitude_toPosition = currentMarker.getPosition().latitude;
                    toPosition = new LatLonPoint(latitude_toPosition, longitude_toPosition);
                    fromPosition = new LatLonPoint(current_latitude, current_longitude);

                    fromAndTo = new RouteSearch.FromAndTo(fromPosition, toPosition);

                    //根据设置选项页面中的步行还是驾车模式选择路线规划 isWalk=true表示步行
                    if (MyMapConfigActivity.isWalk) {
                        //步行路线规划
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RouteSearch.WalkRouteQuery walkQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
                                routeSearch.calculateWalkRouteAsyn(walkQuery);//计算路线 然后再监听的方法里实现路线图
                            }
                        }).start();
                    } else {
                        //驾车路线规划
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RouteSearch.DriveRouteQuery driveQuery =
                                        new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, null);
                                routeSearch.calculateDriveRouteAsyn(driveQuery);//计算路线 然后再监听的方法里实现路线图
                            }
                        }).start();
                    }
                }
            });
        }
        //点击详情
        if (tvMymapParkingspotdetail != null) {
            tvMymapParkingspotdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (marker.getObject() != null) {
                        ParkingMarker spotInfo = (ParkingMarker) marker.getObject();
                        MasterActivity parentActivity = (MasterActivity) getActivity();
                        parentActivity.getSelect(2, spotInfo);
                    }

                }
            });
        }

        if(tvMymapNavgation3D != null){
            tvMymapNavgation3D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CodeUtils.showToast(getActivity(),"3D导航");
                    Intent intent = new Intent(getActivity(),NavActivity.class);
                    intent.putExtra("to_latitude",marker.getPosition().latitude);
                    intent.putExtra("to_longitude",marker.getPosition().longitude);

                    intent.putExtra("from_latitude",current_latitude);
                    intent.putExtra("from_longitude",current_longitude);
                    startActivity(intent);
                }
            });

        }
//        Toast.makeText(getActivity(), "" + marker.getId() + "---" + marker.getTitle()
//                + "---经度" + marker.getPosition().longitude
//                + "---纬度" + marker.getPosition().latitude, Toast.LENGTH_SHORT).show();
        return false;
    }

    //路线规划的监听
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        {
//        Toast.makeText(getActivity(), "code::"+i, Toast.LENGTH_SHORT).show();
            if (i == 1000 && driveRouteResult != null) {
//            Toast.makeText(getActivity(), "path::"+walkRouteResult.getPaths().size(), Toast.LENGTH_SHORT).show();
                if (driveRouteResult.getPaths().size() > 0) {
                    for (int j = 0; j < driveRouteResult.getPaths().size(); j++) {
                        DrivePath drivePath = driveRouteResult.getPaths().get(j);
                        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                getActivity(), amap, drivePath, fromPosition, toPosition);
                        amap.clear();//从地图上删除所有的Marker，Overlay，Polyline 等覆盖物。
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.setNodeIconVisibility(true);
                        drivingRouteOverlay.addToMap();
                        drivingRouteOverlay.zoomToSpan();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "计算路线失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
//        Toast.makeText(getActivity(), "code::"+i, Toast.LENGTH_SHORT).show();
        if (i == 1000 && walkRouteResult != null) {
//            Toast.makeText(getActivity(), "path::"+walkRouteResult.getPaths().size(), Toast.LENGTH_SHORT).show();
            if (walkRouteResult.getPaths().size() > 0) {
                for (int j = 0; j < walkRouteResult.getPaths().size(); j++) {
                    WalkPath walkPath = walkRouteResult.getPaths().get(j);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            getActivity(), amap, walkPath, fromPosition, toPosition);
                    amap.clear();//从地图上删除所有的Marker，Overlay，Polyline 等覆盖物。
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.setNodeIconVisibility(false);
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                }
            }
        } else {
            Toast.makeText(getActivity(), "计算路线失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
/************************************停车位marker*******end***************************************/

    /**********************************
     * 以下是生命周期方法 需要重写******start
     ***************************************/


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        myMap.onDestroy();

        if (null != location_client) {
            location_client.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        myMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        myMap.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        myMap.onSaveInstanceState(outState);
    }


/*********************************生命周期方法 需要重写*********end************************************/


///*********************************key监听*********start************************************************/
//
//    private View.OnKeyListener myKeyListener = new View.OnKeyListener(){
//
//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        switch (event.getAction()){
//            case KeyEvent.KEYCODE_BACK :
//                Toast.makeText(getActivity(),"back",Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return false;
//    }
//};
///*********************************key监听*********end************************************************/

}
