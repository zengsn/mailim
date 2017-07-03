package cn.sunibas.service;

import cn.sunibas.entity.TStext;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITStextService {
    /**
     * 创建文稿
     * */
    void createText(TStext tStext);

    /**
     * 删除文稿
     * */
    void deleteText(TStext tStext);

    /**
     * 查看文稿
     * */
    TStext getText(String uuid);

    /**
     * 修改文稿
     * */
    void updateText(TStext tStext);
 }
