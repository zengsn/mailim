package cn.sunibas.dao;

import cn.sunibas.entity.TSLabelCount;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface ITSLabelCountDao {
    /**
     * 获取部分
     */
    public List<TSLabelCount> getTop();
}
