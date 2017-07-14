package com.gq.com.easy_parking.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gq.com.easy_parking.R;
import com.gq.com.easy_parking.common.AppNetConfig;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.hitomi.universalloader.UniversalImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Spot_pic_showActivity extends AppCompatActivity {


    @Bind(R.id.gv_images)
    GridView gvImages;
    @Bind(R.id.coordinator)
    android.support.design.widget.CoordinatorLayout coordinator;

    private int spotId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_pic_show);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        spotId = intent.getIntExtra("spotId",0);
        transferee = Transferee.getDefault(this);

        showDetailPic();
        testTransferee();
    }


    /********************************
     * gridview transferee 点击图片查看原始图 start
     ******************************************************/
    private Transferee transferee;
    private DisplayImageOptions options;
    private List<String> thumbnailImageList;
    private List<String> sourceImageList;

    private void showDetailPic() {
//        int id = spotInfo.getId();
        thumbnailImageList = new ArrayList<>();
        sourceImageList = new ArrayList<>();

        for(int i = 1;i<7;i++){
            String picUrl_thumbnail = AppNetConfig.SpotDetail+"parkingImages/compressed/"+spotId+"/img00"+i+".jpg";
            thumbnailImageList.add(picUrl_thumbnail);

            String picUrl_source = AppNetConfig.SpotDetail+"parkingImages/"+spotId+"/"+i+".jpg";
            sourceImageList.add(picUrl_source);
        }
//        thumbnailImageList = new ArrayList<>();
//        thumbnailImageList.add("http://static.fdc.com.cn/avatar/sns/1486263782969.png@233w_160h_20q");
//        thumbnailImageList.add("http://static.fdc.com.cn/avatar/sns/1485055822651.png@233w_160h_20q");
//        thumbnailImageList.add("http://static.fdc.com.cn/avatar/sns/1486194909983.png@233w_160h_20q");
//        thumbnailImageList.add("http://static.fdc.com.cn/avatar/sns/1486194996586.png@233w_160h_20q");
//        thumbnailImageList.add("http://static.fdc.com.cn/avatar/sns/1486195059137.png@233w_160h_20q");
//        thumbnailImageList.add("http://static.fdc.com.cn/avatar/sns/1486173497249.png@233w_160h_20q");
//
//        sourceImageList = new ArrayList<>();
//        sourceImageList.add("http://static.fdc.com.cn/avatar/sns/1486263782969.png");
//        sourceImageList.add("http://static.fdc.com.cn/avatar/sns/1485055822651.png");
//        sourceImageList.add("http://static.fdc.com.cn/avatar/sns/1486194909983.png");
//        sourceImageList.add("http://static.fdc.com.cn/avatar/sns/1486194996586.png");
//        sourceImageList.add("http://static.fdc.com.cn/avatar/sns/1486195059137.png");
//        sourceImageList.add("http://static.fdc.com.cn/avatar/sns/1486173497249.png");
    }

    protected void testTransferee() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Spot_pic_showActivity.this));
        options = new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.mipmap.ic_empty_photo)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();

        gvImages.setAdapter(new NineGridAdapter());
    }

    private class NineGridAdapter extends CommonAdapter<String> {

        public NineGridAdapter() {
            super(Spot_pic_showActivity.this, R.layout.item_grid_image, thumbnailImageList);
        }

        @Override
        protected void convert(ViewHolder viewHolder, String item, final int position) {
            final ImageView imageView = viewHolder.getView(R.id.image_view);
            ImageLoader.getInstance().displayImage(item, imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bindTransferee(imageView, position);

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
        }

        private void bindTransferee(ImageView imageView, final int position) {
            // 如果指定了缩略图，那么缩略图一定要先加载完毕
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TransferConfig config = TransferConfig.build()
                            .setNowThumbnailIndex(position)
                            .setSourceImageList(sourceImageList)
                            .setThumbnailImageList(thumbnailImageList)
                            .setMissPlaceHolder(R.mipmap.ic_empty_photo)
                            .setErrorPlaceHolder(R.mipmap.ic_empty_photo)
                            .setOriginImageList(wrapOriginImageViewList(thumbnailImageList.size()))
                            .setProgressIndicator(new ProgressPieIndicator())
                            .setIndexIndicator(new NumberIndexIndicator())
                            .setJustLoadHitImage(true)
                            .setImageLoader(UniversalImageLoader.with(Spot_pic_showActivity.this))
                            .create();
                    transferee.apply(config).show();
                }
            });
        }

        protected List<ImageView> wrapOriginImageViewList(int size) {
            List<ImageView> originImgList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ImageView thumImg = (ImageView) ((LinearLayout) gvImages.getChildAt(i)).getChildAt(0);
                originImgList.add(thumImg);
            }
            return originImgList;
        }
    }


    /********************************
     * gridview transferee 点击图片查看原始图 end
     ******************************************************/
    @Override
    protected void onDestroy() {
        transferee.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
        }

        return super.onKeyUp(keyCode, event);
    }
}
