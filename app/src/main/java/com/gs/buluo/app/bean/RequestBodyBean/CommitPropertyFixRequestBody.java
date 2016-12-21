package com.gs.buluo.app.bean.RequestBodyBean;

import java.util.List;

/**
 * Created by fs on 2016/12/13.
 */
public class CommitPropertyFixRequestBody {
    public String floor;
    public long appointTime;
    public String fixProject;
    public String problemDesc;
    public List<String> pictures;

    @Override
    public String toString() {
        return "CommitPropertyFixRequestBody{" +
                "floor='" + floor + '\'' +
                ", appointTime=" + appointTime +
                ", fixProject='" + fixProject + '\'' +
                ", problemDesc='" + problemDesc + '\'' +
                ", pictures=" + pictures +
                '}';
    }

}
