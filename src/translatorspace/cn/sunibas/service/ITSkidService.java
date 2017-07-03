package cn.sunibas.service;

import cn.sunibas.entity.TSkid;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITSkidService {
    /**
     * 注册
     * */
    void register(TSkid tSkid);

    /**
     * 登陆
     * */
    TSkid login(TSkid tSkid);

    /**
     * 修改小译信息，。。。。。（暂时不管）
     * */
 }
