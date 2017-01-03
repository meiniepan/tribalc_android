package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CityModel;
import com.gs.buluo.app.bean.DistrictModel;
import com.gs.buluo.app.bean.ProvinceModel;
import com.gs.buluo.app.utils.XmlParserHandler;
import com.gs.buluo.app.view.activity.BaseActivity;
import com.gs.buluo.app.view.widget.wheel.OnWheelChangedListener;
import com.gs.buluo.app.view.widget.wheel.WheelView;
import com.gs.buluo.app.view.widget.wheel.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.ButterKnife;

/**
 * Created by jingnan on 2016/5/26.
 */

public class AddressPickPanel extends Dialog implements View.OnClickListener, OnWheelChangedListener {

    private final BaseActivity mActivity;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mBtnConfirm;
    private OnSelectedFinished onSelectedFinished;
    /**
     * 解析省市区
     */
    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";
    private View rootView;

    public AddressPickPanel(BaseActivity activity, OnSelectedFinished onSelectedFinished) {
        super(activity,R.style.my_dialog);
        mActivity = activity;
        this.onSelectedFinished=onSelectedFinished;
        initView();
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void initView() {
        rootView = LayoutInflater.from(mActivity).inflate(R.layout.picker_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                onSelectedFinished.onSelected(mCurrentProviceName+"-"+mCurrentCityName+"-" +mCurrentDistrictName);
                dismiss();
                break;
        }
    }

    private void setUpViews() {
        mViewProvince = (WheelView) rootView.findViewById(R.id.id_province);
        mViewCity = (WheelView) rootView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) rootView.findViewById(R.id.id_district);
        mBtnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(mActivity, mProvinceDatas));
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        } else if (wheel == mViewCity) {
            updateAreas();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }

    }

    /**
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(mActivity, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(mActivity, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mActivity.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
        }
    }

    public interface OnSelectedFinished {
        void onSelected(String string);
    }
}
