package cn.sunibas.entity;

import cn.sunibas.util.ForDebug;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IBAS on 2017/2/6.
 */
public class TSstatus implements Serializable {
    private String uuid;
    private String TSuuid;
    private Date createTime;
    private String TSkidid;
    private int TSindex;
    private int mark;

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

    public int getTSindex() {
        return TSindex;
    }

    public void setTSindex(int TSindex) {
        this.TSindex = TSindex;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public String toString(){
        return ForDebug.Object2String(this);
    }
}
