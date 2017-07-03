package cn.sunibas.dao;

import cn.sunibas.entity.TSMark;

import java.util.List;

/**
 * Created by sunbing on 17-3-4.
 */
public interface ITSMarkDao {
    void insert(TSMark tsMark);

    List<TSMark> getList(String TSSuuid);
}
