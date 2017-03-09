package cn.sunibas.service;

import cn.sunibas.entity.TSLabelCount;

import java.util.List;

/**
 * Created by IBAS on 2017/2/21.
 */
public interface ITSLabelCountService {
    /**
     * 获取部分
     */
    List<TSLabelCount> getTop();
}
