package com.zmt.boxin.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.CalculateDistance;
import com.zmt.boxin.Utils.Line_Draw;

import java.util.ArrayList;
import java.util.List;

import static com.zmt.boxin.R.id.run_page_time;

public class RunActivity extends AppCompatActivity implements AMapLocationListener, LocationSource {

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener = null;

    // 初始化定位客户端，设置监听 声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;

    private List<LatLng> list = new ArrayList<>();

    private ImageView TB;
    private ImageView TB_left;
    private ImageView TB_right;
    private LinearLayout run_page_all;//隐藏run_page_all
    private boolean run_page_all_onclick = false;//是否点击地图，点击隐藏run_page_all
    private boolean TB_onclick = false;//是否按下TB按钮,开始跑步

    private TextView run_page_km;
    private TextView run_page_speed;

    private boolean IsRun = false;

    private static int number;
    private static float sum;
    private CalculateDistance CD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        TB = (ImageView) findViewById(R.id.TB);
        TB_left = (ImageView) findViewById(R.id.TB_left);
        TB_right = (ImageView) findViewById(R.id.TB_right);
        run_page_all = (LinearLayout) findViewById(R.id.run_page_running);
        run_page_km = (TextView) findViewById(R.id.run_page_km);
        run_page_speed = (TextView) findViewById(R.id.run_page_speed);
        sum = 0.0f;// 初始化sum
        number = 0;// 初始化num

        /**
         * 跑步暂停按钮
         */
        TB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.TB){
                    if(!TB_onclick){
                        TB.setVisibility(View.GONE);
                        TB_left.setVisibility(View.VISIBLE);
                        TB_right.setVisibility(View.VISIBLE);
                        TB_onclick = true;
                        IsRun = true;
                    } else {
                        // 结束跑步
                        TB.setVisibility(View.VISIBLE);
                        TB_left.setVisibility(View.GONE);
                        TB_right.setVisibility(View.GONE);
                        TB_onclick = false;
                        IsRun = false;
                    }
                }
            }
        });

        /**
         * 跑步开始按钮
         */
        TB_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.TB_left){
                    if(!TB_onclick){
                        TB.setVisibility(View.GONE);
                        TB_left.setVisibility(View.VISIBLE);
                        TB_right.setVisibility(View.VISIBLE);
                        TB_onclick = true;
                        IsRun = true;
                    } else {
                        // 结束跑步
                        TB.setVisibility(View.VISIBLE);
                        TB_left.setVisibility(View.GONE);
                        TB_right.setVisibility(View.GONE);
                        TB_onclick = false;
                        IsRun = false;
                    }
                }
            }
        });

        /**
         * 跑步结束按钮
         */
        TB_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.TB_right){
                    finish();
                }
            }
        });


        init();

        /**
         * 点击地图隐藏部件
         */
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(!run_page_all_onclick){
                    run_page_all_onclick = true;
                    run_page_all.setVisibility(View.GONE);
                } else {
                    run_page_all_onclick = false;
                    run_page_all.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void init() {
        // TODO Auto-generated method stub
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        // TODO Auto-generated method stub
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.blue_circle));// 设置小蓝点的图标

        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.anchor(0.5f, 0.5f);// 设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));// 设置缩放比例 4~19，17比较合适
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        deactivate();
        mapView.onDestroy();

    }

    @Override
    // 暂停
    protected void onPause() {//
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();

    }

    @Override
    // 重新开始
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /*
     * 实现AMapLocationListener接口，获取定位结果
     * AMapLocationListener接口只有onLocationChanged方法可以实现
     * ，用于接收异步返回的定位结果，参数是AMapLocation类型。
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (amapLocation != null && mListener != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Log.e("speed", amapLocation.getSpeed() + "");
                if (amapLocation.getSpeed() >= 0) {// 果速度为0则不更新地图，和画线
                    if (IsRun) {
                        if (CD == null){
                            CD = new CalculateDistance();
                        }
                        CD.setNumber(number++);
                        CD.setSum(sum);
                        new Line_Draw(amapLocation, aMap, mListener, list, CD);
                        sum = CD.getSum();

                        /**
                         * run_page_time
                         * run_page_time_hide
                         * run_page_speed_hide
                         */
                        run_page_km.setText(sum + "");//总里程
                        run_page_speed.setText(amapLocation.getSpeed() + "");//实时速度
                    } else {
                        // 结束跑步,画出终点
                        // 跑步结束时，最终的点如何确定？
                        if (list.size() > 0) {
                            aMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_location))
                                    .position(list.get(list.size() - 1))
                                    .draggable(false));
                        }
                    }
                }
            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError",
                        "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
            }
        }
    }

    // activate()第一次加载地图时会调用，点击定位按钮后调用
    @Override
    public void activate(OnLocationChangedListener arg0) {
        // TODO Auto-generated method stub

        mListener = arg0;// 好像放里面放外面没有什么区别 , 不能删除向 onLocationChanged 传送数据

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(
                    this.getApplicationContext());

            AMapLocation myLocation = mLocationClient.getLastKnownLocation();
            if (myLocation != null) {
                Log.e("LastKnownLocation", "不为空");

                mListener.onLocationChanged(myLocation);// 显示系统小蓝点
            } else
                Log.e("LastKnownLocation", "为空");

            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

            mLocationClient.setLocationListener(this);// 设置定位回调监听

            // 设置定位模式为High_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mLocationOption.setNeedAddress(true);// 设置是否返回地址信息（默认返回地址信息）

            mLocationOption.setOnceLocation(false);// 设置是否只定位一次,默认为false

            mLocationOption.setWifiActiveScan(true);// 设置是否强制刷新WIFI，默认为强制刷新

            mLocationOption.setMockEnable(true);// 设置是否允许模拟位置,默认为false，不允许模拟位置

            mLocationOption.setInterval(2000);// 设置定位间隔,单位毫秒,默认为2000ms

            mLocationClient.setLocationOption(mLocationOption);// 给定位客户端对象设置定位参数

            mLocationClient.startLocation();// 启动定位
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.amap.api.maps2d.LocationSource#deactivate()
     * 停止定位。重写该方法时，需移除定位监听，并销毁定位服务对象。 应该在 onDestory() 中调用
     */
    @Override
    public void deactivate() {
        // TODO Auto-generated method stub

        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
}
