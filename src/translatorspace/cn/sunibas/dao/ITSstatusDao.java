package cn.sunibas.dao;

import cn.sunibas.entity.TSstatus;

import java.util.List;

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
    List<TSstatus> findByTSuuidAndIndex(String tsuuid,int index);
    TSstatus getFirstByTSuuidAndIndex(String tsuuid,int index);

    /**
     * 修改(改)
     * */
    int updateMarkByTSsuuid(int mark,String tssuuid);

    /**
     * 删除(删)
     * */
    void delete(TSstatus tSstatus);
  }
