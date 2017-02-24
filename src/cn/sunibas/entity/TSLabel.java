package cn.sunibas.entity;

import cn.sunibas.util.ForDebug;

import java.io.Serializable;

/**
 * Created by IBAS on 2017/2/20.
 */
public class TSLabel implements Serializable{
    private String tsLabel;
    private String tsUuid;

    public String getTsLabel() {
        return tsLabel;
    }

    public void setTsLabel(String tsLabel) {
        this.tsLabel = tsLabel;
    }

    public String getTsUuid() {
        return tsUuid;
    }

    public void setTsUuid(String tsUuid) {
        this.tsUuid = tsUuid;
    }

    @Override
    public String toString(){
        return ForDebug.Object2String(this);
    }
}
