package cn.sunibas.service;


import cn.sunibas.entity.TSNewText;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITSNewTextService {
    /**
     * 获取
     * */
    List<TSNewText> getAll();

    /**
     * 修改
     * */
    void update(TSNewText tsNewText);

    /**
     * 删除
     * */
    void delete(TSNewText tsNewText);
  }
