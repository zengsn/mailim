package cn.sunibas.dao;

import cn.sunibas.entity.TSLanguage;

import java.util.List;

/**
 * Created by IBAS on 2017/2/15.
 */
public interface ITSLanguageDao {
    /**
     * 仅仅需要获取，并获取一次，一次获取全部
    * */
    List<TSLanguage> getAll();
 }
