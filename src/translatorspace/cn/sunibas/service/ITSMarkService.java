package cn.sunibas.service;

import cn.sunibas.entity.TSMark;

import java.util.List;

/**
 * Created by sunbing on 17-3-4.
 */
public interface ITSMarkService {
    void insert(TSMark tsMark);

    List<TSMark> getList(String TSSuuid);
}
