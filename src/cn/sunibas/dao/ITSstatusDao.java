package cn.sunibas.dao;

import cn.sunibas.entity.TSstatus;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITSstatusDao {
    /**
     * 保存
     * */
    void save(TSstatus tSstatus);

    /**
     * 查找
     * */
    TSstatus findByUuid(String uuid);

    /**
     * 修改(改)
     * */


    /**
     * 删除(删)
     * */
    void delete(TSstatus tSstatus);
  }
