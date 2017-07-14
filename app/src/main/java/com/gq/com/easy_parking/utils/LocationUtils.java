package com.gq.com.easy_parking.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by hasee on 2017/4/23.
 */
public class LocationUtils {
    private static GeocodeSearch geocodeSearch;
    private static String address;
    private static LatLonPoint point;
    private static TextView view;
    public static void latlonToPosition(Context context, LatLonPoint latlonPoint, TextView toTextView){
        geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(new MySearch());
        point = latlonPoint;
        view = toTextView;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RegeocodeQuery query = new RegeocodeQuery(point,1000,GeocodeSearch.AMAP);
                    geocodeSearch.getFromLocationAsyn(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static class MySearch implements GeocodeSearch.OnGeocodeSearchListener {

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if( i == 1000){
                view.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    }
}
