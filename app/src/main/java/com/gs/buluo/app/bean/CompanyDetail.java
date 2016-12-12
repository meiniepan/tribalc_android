package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by fs on 2016/12/12.
 */
public class CompanyDetail implements Serializable{
    public CompanyInfo company;
    public String department;
    public String position;
    public String personNum;
    public String comfirmed;
}
