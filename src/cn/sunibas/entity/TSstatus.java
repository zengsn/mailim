package cn.sunibas.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/6.
 */
public class TSstatus {
    private String uuid;
    private String TSuuid;
    private Date createTime;
    private String TSkidid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTSuuid() {
        return TSuuid;
    }

    public void setTSuuid(String TSuuid) {
        this.TSuuid = TSuuid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTSkidid() {
        return TSkidid;
    }

    public void setTSkidid(String TSkidid) {
        this.TSkidid = TSkidid;
    }
}
