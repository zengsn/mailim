package cn.sunibas.dao;


import cn.sunibas.entity.TStext;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITStextDao {
    /**
     * 保存
     * */
    void save(TStext tStext);

    /**
     * 查找
     * */
    TStext findByUuid(String uuid);

    /**
     * 修改(改)
     * */
    void update(TStext tStext);

    /**
     * 删除(删)
     * */
    void delete(TStext tStext);
    void deleteByUuid(String uuid);
   }
