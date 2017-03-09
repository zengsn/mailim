package cn.sunibas.dao.impl;

import cn.sunibas.dao.ITSstatusDao;
import cn.sunibas.entity.TSstatus;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public class TSstatusDao implements ITSstatusDao {

    // IOC容器(依赖)注入SessionFactory对象
    private SessionFactory sessionFactory;
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TSstatus tSstatus) {
        sessionFactory
                .getCurrentSession()
                .save(tSstatus);
    }

    @Override
    public TSstatus findByUuid(String uuid) {
        return (TSstatus)sessionFactory
                .getCurrentSession()
                .createQuery("from TSstatus where uuid=?")
                .setString(0,uuid)
                .uniqueResult();
    }

    @Override
    public List<TSstatus> findByTSuuidAndIndex(String tsuuid, int index) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from TSstatus where TSuuid=? and TSindex=?")
                .setString(0,tsuuid)
                .setInteger(1,index)
                .list();
    }

    @Override
    public TSstatus getFirstByTSuuidAndIndex(String tsuuid, int index) {
        List<TSstatus> list =  sessionFactory
                .getCurrentSession()
                .createQuery("from TSstatus where TSuuid=? and TSindex=?")
                .setString(0,tsuuid)
                .setInteger(1,index)
                .setFirstResult(0)
                .setMaxResults(1)
                .list();
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public int updateMarkByTSsuuid(int mark, String tssuuid) {
        return sessionFactory.getCurrentSession()
                .createSQLQuery("UPDATE T_TSSTATUS SET TSMark=" + mark + " WHERE Uuid='" + tssuuid + "'")
                .addEntity(TSstatus.class)
                .executeUpdate();
    }

    @Override
    public void delete(TSstatus tSstatus) {
        sessionFactory
                .getCurrentSession()
                .delete(tSstatus);
    }
}
