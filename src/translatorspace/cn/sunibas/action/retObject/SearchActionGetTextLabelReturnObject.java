package cn.sunibas.action.retObject;

import cn.sunibas.entity.TSLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public class SearchActionGetTextLabelReturnObject {
    private List<String> labels = new ArrayList<String>();
    public SearchActionGetTextLabelReturnObject(List<TSLabel> tsLabels){
        for (int i = 0;i < tsLabels.size();i++) {
            labels.add(tsLabels.get(i).getTsLabel());
        }
    }

    public List<String> getLabels() {
        return labels;
    }
}
