package com.zmt.boxin.Utils;

import android.graphics.Color;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.zmt.boxin.R;

import java.util.List;

/**
 * Created by yangs on 2016/5/11.
 */
public class Line_Draw {
    public Line_Draw(AMapLocation amapLocation, AMap aMap,
                     OnLocationChangedListener mlistener, List<LatLng> list, CalculateDistance CD) {
        // TODO Auto-generated constructor stub

        double Latitude = amapLocation.getLatitude();

        double Longitude = amapLocation.getLongitude();

        LatLng ll = new LatLng(Latitude, Longitude);

        CD.setLatlng(ll);

        list.add(ll);

        Log.e("Point", "Latitude=" + Latitude + " Longitude=" + Longitude
                + " Speed" + amapLocation.getSpeed() + " Sum:" + CD.getSum());

        aMap.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE)
                .width(20));

        // 画初始点
        aMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.start_location))
                .position(list.get(0)).draggable(false));
    }
}
