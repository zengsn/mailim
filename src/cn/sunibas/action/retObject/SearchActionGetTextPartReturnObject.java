package cn.sunibas.action.retObject;

import cn.sunibas.entity.TSTextPart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IBAS on 2017/2/22.
 */
public class SearchActionGetTextPartReturnObject {
    /**
     * 200 成功
     * 300 查询结束没有更多内容（这里是当前有查找到内容）
     * 400 失败或者错误或者没有内容
     */
    private int status;
    private String retStr;
    private List<PackageTSTextPart> data = new ArrayList<PackageTSTextPart>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRetStr() {
        return retStr;
    }

    public void setRetStr(String retStr) {
        this.retStr = retStr;
    }

    public List<PackageTSTextPart> getData() {
        return data;
    }

    public void addData(List<TSTextPart> tsTextParts) {
        Iterator<TSTextPart> iterable = tsTextParts.iterator();
        while (iterable.hasNext()) {
            data.add(new PackageTSTextPart(iterable.next()));
        }
    }
}
