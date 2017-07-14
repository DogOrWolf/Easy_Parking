package com.gq.com.easy_parking.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.activity.MasterActivity;
import com.gq.com.easy_parking.bean.Image;
import com.gq.com.easy_parking.bean.Index;
import com.gq.com.easy_parking.bean.Product;
import com.gq.com.easy_parking.common.AppNetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hasee on 2017/4/21.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.iv_header_back)
    ImageView ivHeaderBack;
    @Bind(R.id.tv_header)
    TextView tvHeader;
    @Bind(R.id.iv_header_setting)
    ImageView ivHeaderSetting;
    @Bind(R.id.ll_header)
    LinearLayout llHeader;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.tv_title_home)
    TextView tvTitleHome;
    @Bind(R.id.tv_name_home)
    TextView tvNameHome;
    @Bind(R.id.btn_search_home)
    Button btnSearchHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        initTitles();
        initData();
        setBannerFromSp();
        return view;
    }

    private void setBannerFromSp() {
        SharedPreferences sp = getActivity().getSharedPreferences("index_sp", Context.MODE_PRIVATE);
        String index_data = sp.getString("index_data", "");
        if(!index_data.isEmpty()){
            setBanner(index_data);
        }
    }

    //初始化头部标题
    private void initTitles() {
        ivHeaderBack.setVisibility(View.GONE);
        ivHeaderSetting.setVisibility(View.GONE);

        btnSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterActivity parent = (MasterActivity)getActivity();
                parent.getSelect(1);
            }
        });
    }
    //ButterKnife
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //获取banner和首页数据
    private Index indexData = new Index();
    private void initData() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = AppNetConfig.INDEX;
        asyncHttpClient.post(url,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                setBanner(content);
                saveIndexDataToSp(content);
                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(getActivity(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveIndexDataToSp(String content){
        if(content != null && !content.isEmpty()){
            SharedPreferences sp = this.getActivity().getSharedPreferences("index_sp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("index_data",content);
            editor.commit();
        }
    }
    private void setBanner(String content){
        if(null != indexData){
            JSONObject jsonObject =  JSON.parseObject(content);
            String proInfo = jsonObject.getString("proInfo");
            String imageArr = jsonObject.getString("imageArr");
            Product product = JSON.parseObject(proInfo, Product.class);
            List<Image> images = JSON.parseArray(imageArr, Image.class);

            indexData.setImages(images);
            indexData.setProduct(product);

            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            List<String> imageUrls = new ArrayList<>();
            for(int i=0; i<indexData.getImages().size();i++){
                imageUrls.add(indexData.getImages().get(i).getIMAURL());
            }
            //这里的图片路径是http://192.168.1.106:8088/parkingImages/car01.jpg这是通过主机8088端口转发至虚拟机80端口nginx上
            //直接使用手机访问虚拟机地址http://192.168.34.128/parkingImages/car01.jpg是不行的
            banner.setImages(imageUrls);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.CubeOut);
            //设置标题集合（当banner样式有显示title时）
            String[] titles = new String[]{"一键找车位","手快抢车位","想不到你是这样的app","导航，精确定位"};
            banner.setBannerTitles(Arrays.asList(titles));
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(1500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.start();

        }
    }
    //banner需要的加载类
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).error(R.drawable.error).into(imageView);
        }

//    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
//    @Override
//    public ImageView createImageView(Context context) {
//        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
//        SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
//        return simpleDraweeView;
//    }
    }
}
