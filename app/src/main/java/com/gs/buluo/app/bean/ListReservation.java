package com.gs.buluo.app.bean;

import com.gs.buluo.app.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjn on 2016/11/29.
 */
public class ListReservation implements Serializable{
    public String id;
    public String storeId;
    public String storeSetMealId;
    public String storeName;
    public String mainPicture;
    public String markPlace;
    public long appointTime;
    public String personNum;
    public List<String>  tags;
    public  ReserveStatus status;

    public enum ReserveStatus {
        PROCESSING("PROCESSING"),FAILURE("FAILURE"),SUCCEED("SUCCEED"),CANCEL("CANCEL");
        public String status;
        ReserveStatus(String processing) {
            status=processing;
        }
    }
}
