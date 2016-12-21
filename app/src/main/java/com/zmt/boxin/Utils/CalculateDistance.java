package com.zmt.boxin.Utils;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

/**
 * Created by yangs on 2016/5/11.
 */
public class CalculateDistance {
    private LatLng start, end;

    private float sum;

    private int number = 0;

    private LatLng latlng;

    public void setSum(float sum) {
        this.sum = sum;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getSum() {

        if (number == 0) {
            start = end = latlng;

        } else {
            start = end;
            end = latlng;
        }

        sum += AMapUtils.calculateLineDistance(start, end);
        return sum;
    }

    public CalculateDistance() {
        // TODO Auto-generated constructor stub

    }
}
