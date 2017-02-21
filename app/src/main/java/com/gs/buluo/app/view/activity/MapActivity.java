package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CoordinateBean;
import com.gs.buluo.app.bean.ListStore;
import com.gs.buluo.app.presenter.BasePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class MapActivity extends BaseActivity implements OnGetGeoCoderResultListener {
    private MapView mMapView;
    BaiduMap mBaiduMap;
    LocationClient mLocClient;
    GeoCoder mSearch = null;
    public MyLocationListener myListener = new MyLocationListener();
    boolean isFirstLoc = true; // 是否首次定位

    @Bind(R.id.food_map_latitude)
    EditText mLatitude;
    @Bind(R.id.food_map_longitude)
    EditText mLongitude;
    private Marker mMarker;

    @Override
    protected void bindView(Bundle savedInstanceState) {
//        MapView.setMapCustomEnable(true);
//        setMapCustomFile(this);
        ArrayList<CoordinateBean> posList = getIntent().getParcelableArrayListExtra(Constant.ForIntent.COORDINATE);
        double[] positons = getIntent().getDoubleArrayExtra(Constant.ForIntent.SERVE_POSITION);
        mMapView = (MapView) findViewById(R.id.map_food);
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        if (positons == null || positons.length == 0)mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        if (posList != null && posList.size() > 0) {
            initOverlay(posList);
        } else if (positons != null) {
            initPos(positons);
        }

        findViewById(R.id.food_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ptCenter = new LatLng((Float.valueOf(mLatitude.getText()
                        .toString())), (Float.valueOf(mLongitude.getText().toString())));

                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
            }

//            EditText editCity = (EditText) findViewById(R.id.city);       城市街道名搜索
//            EditText editGeoCodeKey = (EditText) findViewById(R.id.geocodekey);
//            // Geo搜索
//            mSearch.geocode(new GeoCodeOption().city(
//                    editCity.getText().toString()).address(editGeoCodeKey.getText().toString()));
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            private InfoWindow mInfoWindow;

            @Override
            public boolean onMarkerClick(final Marker marker) {
                View view = View.inflate(getCtx(), R.layout.map_popup, null);
                TextView name = (TextView) view.findViewById(R.id.map_name);
                TextView address = (TextView) view.findViewById(R.id.map_address);
                InfoWindow.OnInfoWindowClickListener listener = null;
                Bundle info = marker.getExtraInfo();
                name.setText(info.getString("name"));
                address.setText(info.getString("address"));
                final String id = info.getString("sid");
                if (id != null) {
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            Intent intent = new Intent(getCtx(), ServeDetailActivity.class);
                            intent.putExtra(Constant.SERVE_ID,id);
                            startActivity(intent);
                        }
                    };
                }
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -47, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });
    }

    private void initPos(double[] positons) {
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(positons[1])
                .longitude(positons[0]).build();
        mBaiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(positons[1], positons[0]);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void initOverlay(ArrayList<CoordinateBean> posList) {
        LatLng llA;
        for (CoordinateBean coordinateBean : posList) {
            llA = new LatLng(coordinateBean.latitude, coordinateBean.longitude);
//            switch (coordinateBean.store.)
            ListStore store = coordinateBean.store;
            if (store == null) continue;
            BitmapDescriptor bdA;
            switch (store.category) {
                case ENTERTAINMENT:
                    bdA = BitmapDescriptorFactory.fromResource(R.mipmap.map_fun);
                    break;
                case HAIRDRESSING:
                    bdA = BitmapDescriptorFactory.fromResource(R.mipmap.map_dress);
                    break;
                case FITNESS:
                    bdA = BitmapDescriptorFactory.fromResource(R.mipmap.map_fitness);
                    break;
                case KEEPHEALTHY:
                    bdA = BitmapDescriptorFactory.fromResource(R.mipmap.map_health);
                    break;
                default:
                    bdA = BitmapDescriptorFactory.fromResource(R.mipmap.map_fun);
                    break;
            }
            MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                    .zIndex(9).draggable(true);
            // 掉下动画
            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));

            Bundle bundle = new Bundle();
            bundle.putString("name", store.name);
            bundle.putString("address", store.district + store.address);
            bundle.putString("sid", coordinateBean.serveId);
            mMarker.setExtraInfo(bundle);
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    private void setMapCustomFile(Context context) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/custom_config.txt");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + "custom_config.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MapView.setCustomMapStylePath(moduleName + "/custom_config.txt");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {      //输入搜索地址的回调
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(MapActivity.this, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  //输入纬度的回调
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        Toast.makeText(MapActivity.this, result.getAddress(),
                Toast.LENGTH_LONG).show();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
