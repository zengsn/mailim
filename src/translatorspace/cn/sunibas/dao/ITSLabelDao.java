package cn.sunibas.dao;

import cn.sunibas.entity.TSLabel;

import java.util.List;

/**
 * Created by IBAS on 2017/2/20.
 */
public interface ITSLabelDao {
    /**
     * 保存
     */
    void save(TSLabel tsLabel);

//    /**
//     * 查询
//     */
//    List<TSLabel> getAll();

    /**
     * 根据标签查询uuid
     */
    List<TSLabel> getByUuidLimit(String uuid,int from,int count);

    /**
     * 根据标签名查询文稿的uuid集合
     */
    List<TSLabel> getByLabelLimit(String label,int from,int count);

    List<TSLabel> getByLabel(String label);

    List<TSLabel> getTopLabelList(int count);
}
