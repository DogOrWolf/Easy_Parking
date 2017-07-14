package com.gq.com.easy_parking.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.activity.MasterActivity;
import com.gq.com.easy_parking.activity.Spot_pic_showActivity;
import com.gq.com.easy_parking.bean.ParkingMarker;
import com.gq.com.easy_parking.bean.ParkingSpotInfo;
import com.gq.com.easy_parking.common.AppNetConfig;
import com.gq.com.easy_parking.utils.LocationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2017/4/21.
 */
public class SpotDetailFragment extends Fragment {

    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.ll_header)
    LinearLayout llHeader;
    @Bind(R.id.tv_spot_name_detail)
    TextView tvSpotNameDetail;
    @Bind(R.id.tv_number_capacity_detail)
    TextView tvNumberCapacityDetail;
    @Bind(R.id.tv_rest_number_detail)
    TextView tvRestNumberDetail;
    @Bind(R.id.iv_pic_main_detail)
    ImageView ivPicMainDetail;
    @Bind(R.id.tv_site_detail)
    TextView tvSiteDetail;
    @Bind(R.id.gridview_detail)
    GridView gridviewDetail;

    private ParkingMarker spotInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragmnt_spot_detail, null);

        ButterKnife.bind(this, view);
        spotInfo = ((MasterActivity)getActivity()).spotInfo;
        initTitle();
        if(null != spotInfo){
            //通过当前点击的地点id查询地点数据 并显示页面
            initPageBySpotInfo(spotInfo);

        }
        return view;
    }

    private void initPageBySpotInfo(ParkingMarker spotInfo) {
        //初始化详情列表部分
        initDetaillist(spotInfo);
        //初始化gridview部分
        initGridView(spotInfo);
    }

    private void initDetaillist(ParkingMarker spotInfo) {
//        Toast.makeText(getActivity(),"..."+spotInfo.getTitle(),Toast.LENGTH_SHORT).show();
        //设值
        tvSpotNameDetail.setText(spotInfo.getTitle());
        tvRestNumberDetail.setText(""+spotInfo.getRest());
        tvNumberCapacityDetail.setText(""+spotInfo.getCapacity());
        //通过经纬度获取查询地点详细数据
        tvSiteDetail.setText("详细信息获取中...请稍等");
        LatLonPoint point = new LatLonPoint(spotInfo.getLatitude(),spotInfo.getLongitude());
        LocationUtils.latlonToPosition(getActivity(), point,tvSiteDetail);

        //更改详细信息的标题图片
//        String url = AppNetConfig.BASE_URL+"images/yasuo/"+spotInfo.getId()+"/overview.jpg";
        String url = AppNetConfig.SpotDetail+"parkingImages/compressed/"+spotInfo.getId()+"/overview.jpg";
        Picasso.with(getActivity()).load(url).fit().centerCrop().into(ivPicMainDetail);


    }

    private void initTitle() {
        ivHeaderBack.setVisibility(View.VISIBLE);
        ivHeaderSetting.setImageResource(R.drawable.more);
        ivHeaderSetting.setVisibility(View.VISIBLE);
        tvHeader.setText("详情");

        ivHeaderSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Spot_pic_showActivity.class);
                if(null != spotInfo){
                    intent.putExtra("spotId",spotInfo.getId());
                }else {
                    intent.putExtra("spotId",0);
                }
                startActivity(intent);
            }
        });

        ivHeaderBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterActivity parent = (MasterActivity)getActivity();
                parent.getSelect(1);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        //刷新页面数据 重新加载
        spotInfo = ((MasterActivity)getActivity()).spotInfo;
        if(null != spotInfo){
            //通过当前点击的地点id查询地点数据 并显示页面
            initPageBySpotInfo(spotInfo);

        }
        super.onResume();
    }
    //初始化grid数据
    private ArrayList<String> picUrls;
    private String [] titles = {"前:","右:","后:","左:","中:","上:"};
    private void initGridView(ParkingMarker spotInfo) {

        int id = spotInfo.getId();
        picUrls = new ArrayList<>();
        for(int i = 1;i<7;i++){
            String picUrl = AppNetConfig.SpotDetail+"parkingImages/compressed/"+id+"/img00"+i+".jpg";
            picUrls.add(picUrl);
            Log.d("picUrl",picUrl);
//            Toast.makeText(getActivity(),""+picUrl,Toast.LENGTH_SHORT).show();
        }


        MyAdapter adapter = new MyAdapter();
        gridviewDetail.setAdapter(adapter);
    }
    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return picUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = View.inflate(getActivity(),R.layout.item_grid,null);
            }
            ImageView image = (ImageView) convertView.findViewById(R.id.iv_griditem);
            TextView title = (TextView) convertView.findViewById(R.id.tv_item_title);
            title.setText(titles[position]);
            Picasso.with(getActivity()).load(picUrls.get(position)).into(image);
            return convertView;
        }
    }


}
