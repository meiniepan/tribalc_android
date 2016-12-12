package com.gs.buluo.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs on 2016/12/12.
 */
public class CompanyInfo implements Serializable {

    /**
     * id : 6894287f0cf210fc9ceffghj
     * pictures : ["/company/pictures/5820539b8d6a4b5693f39beb/dfer45.jpg","/company/pictures/5820539b8d6a4b5693f39beb/dfer46.jpg"]
     * logo : /company/logo/5820539b8d6a4b5693f39beb/dfer46.jpg
     * name : 阿里巴巴
     * desc : 阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。
     */

    private String id;
    private String logo;
    private String name;
    private String desc;
    private List<String> pictures;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
