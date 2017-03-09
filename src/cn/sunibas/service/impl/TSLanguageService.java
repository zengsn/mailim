package cn.sunibas.service.impl;

import cn.sunibas.dao.ITSLanguageDao;
import cn.sunibas.entity.TSLanguage;
import cn.sunibas.service.ITSLanguageService;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
public class TSLanguageService implements ITSLanguageService {
    //注入dao
    private ITSLanguageDao itsLanguageDao;

    public void setItsLanguageDao(ITSLanguageDao itsLanguageDao) {
        this.itsLanguageDao = itsLanguageDao;
    }

    @Override
    public List<TSLanguage> getAll() {
        return itsLanguageDao.getAll();
    }
}
