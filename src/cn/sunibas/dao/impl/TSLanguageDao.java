package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSLanguageDao;
import cn.sunibas.entity.TSLanguage;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
public class TSLanguageDao implements ITSLanguageDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TSLanguage> getAll() {
        return (List<TSLanguage>)sessionFactory.getCurrentSession()
                .createQuery("from TSLanguage")
                .list();
    }
}
