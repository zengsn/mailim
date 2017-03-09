package cn.sunibas.service;

import cn.sunibas.entity.TSstatus;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITSstatusService {
    /**
     * 创建状态
     * */
    void createStatus(TSstatus tSstatus);

    /**
     * 删除状态
     * */
    void deleteStatus(TSstatus tSstatus);

    /**
     * 查看状态
     * */
    TSstatus getStatus(String uuid);

 }
