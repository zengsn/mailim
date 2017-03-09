package cn.sunibas.dao;

import cn.sunibas.entity.TSkid;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITSkidDao {

    /**
     * 保存(增)
     * */
    void save(TSkid tSkid);

    /**
     * 查找(查)
     * */
    TSkid findById(String id);
    TSkid findByNameAndPassword(String name,String pwd);

    /**
     * 修改(改)
     * */
    void update(TSkid tSkid);

    /**
     * 删除(删)
     * */

   }
