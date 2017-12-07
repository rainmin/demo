package com.rainmin.demo.map;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TextOptions;
import com.rainmin.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements AMapLocationListener {

    private AMap mMap;
    private AMapLocationClient mLocationClient;

    MapView mMapView;
    EditText mEtSearch;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mEtSearch = (EditText)view.findViewById(R.id.et_search);
        mMapView = (MapView)view.findViewById(R.id.map);

//        LatLng centerBJPoint = new LatLng(30.668526, 104.032933);
//        AMapOptions mapOptions = new AMapOptions();
//        mapOptions.camera(new CameraPosition(centerBJPoint, 10f, 0, 0));
//        mMapView = new MapView(getActivity(), mapOptions);
        mMapView.onCreate(savedInstanceState);

        initListener();
        initMap();

        return view;
    }

    private void initListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(v.getText().toString().trim())) {
                        LatLng latLng = new LatLng(27.390000, 106.570000);
                        LatLng latLng1 = new LatLng(27.400000, 106.580000);
                        final Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("统子窝锰矿"));
                        final Marker marker1 = mMap.addMarker(new MarkerOptions().position(latLng1).title("煤子窝锰矿"));
                        mMap.addText(new TextOptions() .position(latLng).text("统子窝锰矿")
                                .fontSize(30).fontColor(Color.BLUE).backgroundColor(Color.GRAY));
                        mMap.addText(new TextOptions().position(latLng1).text("煤子窝锰矿")
                                .fontSize(30).fontColor(Color.BLUE).backgroundColor(Color.TRANSPARENT));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                    return true;
                }
                return false;
            }
        });

        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getActivity().startActivity(new Intent(getActivity(), MapSearchActivity.class));
                }
            }
        });
    }

    private void initMap() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
        UiSettings uiSettings = mMap.getUiSettings();
        //隐藏定位按钮
        uiSettings.setMyLocationButtonEnabled(false);
        //隐藏logo
        uiSettings.setLogoBottomMargin(-50);

//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        //定位蓝点展现模式
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效
//        myLocationStyle.interval(2000);
//        mMap.setMyLocationStyle(myLocationStyle);
//        mMap.setMyLocationEnabled(true);

        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果
        locationClientOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        locationClientOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        locationClientOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒
        locationClientOption.setHttpTimeOut(30000);

        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(locationClientOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        //移动地图中心到指定位置
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.703277, 106.94876), 10f));
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                Log.d("rainmin", "locate success, latitude: " + latitude
                        + " longitude: " + longitude);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Toast.makeText(getActivity(), "location fail", Toast.LENGTH_SHORT).show();
                Log.e("rainmin","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mEtSearch.clearFocus();
        //mEtSearch.setFocusable(false);
        //启动定位
        //mLocationClient.startLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        mLocationClient.stopLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁定位客户端
        mLocationClient.onDestroy();
        mMapView.onDestroy();
    }
}
