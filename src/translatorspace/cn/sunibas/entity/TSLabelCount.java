package cn.sunibas.entity;

import cn.sunibas.util.ForDebug;

/**
 * Created by IBAS on 2017/2/21.
 */
public class TSLabelCount {
    private String label;
    private int count;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString(){
        return ForDebug.Object2String(this);
    }
}
