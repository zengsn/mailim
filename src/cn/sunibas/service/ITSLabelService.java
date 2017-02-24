package cn.sunibas.service;

import cn.sunibas.entity.TSLabel;

import java.util.List;

/**
 * Created by IBAS on 2017/2/20.
 */
public interface ITSLabelService {
    void save(TSLabel tsLabel);

    List<TSLabel> getByUuidLimit(String uuid,int from,int count);

    List<TSLabel> getByLabelLimit(String label,int from,int count);

    List<TSLabel> getByLabel(String label);

    List<TSLabel> getTopLabelList(int count);
}
