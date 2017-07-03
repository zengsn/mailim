package cn.sunibas.entity;

import java.io.Serializable;

/**
 * Created by sunbing on 17-3-4.
 */
public class TSMark implements Serializable{
    private String TSSuuid;
    private String TSKuuid;
    private int mark;
    private boolean backup;
    private boolean enable;

    public String getTSSuuid() {
        return TSSuuid;
    }

    public void setTSSuuid(String TSSuuid) {
        this.TSSuuid = TSSuuid;
    }

    public String getTSKuuid() {
        return TSKuuid;
    }

    public void setTSKuuid(String TSKuuid) {
        this.TSKuuid = TSKuuid;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
