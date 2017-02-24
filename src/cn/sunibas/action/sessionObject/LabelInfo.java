package cn.sunibas.action.sessionObject;

import cn.sunibas.entity.TSLabel;

import java.util.List;

/**
 * Created by IBAS on 2017/2/22.
 */
public class LabelInfo {
    private int min = 3;
    private List<TSLabel> tsLabelList;
    private int from;
    private int to;
    //status == false 表示可以继续
    //status == true  表示结束
    private boolean status;

    public List<TSLabel> getTsLabelList() {
        return tsLabelList;
    }

    public void setTsLabelList(List<TSLabel> tsLabelList) {
        if (tsLabelList.size() < min) {
            this.status = true;
        } else {
            //将内容切割为一次不少于min个，第一次为最大的可能情况
            //size = 5 > min;5 < 2 * min;to = 5
            //size = 6 > min;6 = 2 * min;to = 3
            this.status = false;
            this.from = 0;
            this.to = tsLabelList.size() % min + min;
        }
        this.tsLabelList = tsLabelList;
    }

    public boolean getStatus() {
        return status;
    }

    /**
     * 这里获取一个list，用于查询的in的内容
     * @return
     */
    public String getTarget() {
        StringBuilder stringBuilder = new StringBuilder();
        for (;from < to;from++) {
            stringBuilder.append("'");
            stringBuilder.append(tsLabelList.get(from).getTsUuid());
            stringBuilder.append("',");
        }
        to += min;
        if (from == tsLabelList.size()) {
            status = true;
        }
        return stringBuilder.toString().substring(0,stringBuilder.length() - 1);
    }
}
