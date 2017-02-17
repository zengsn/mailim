package cn.sunibas.dao;

import cn.sunibas.entity.TSNewText;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public interface ITSNewTextDao {

    /**
     * 保存(增)由触发器完成
     * */


    /**
     * 查找(查)
     * */
    List<TSNewText> getAll();

    /**
     * 修改(改)(修改status)
     * */
    void update(TSNewText tsNewText);

    /**
     * 删除(删)(处理完即删除)
     * */
    void delete(TSNewText tsNewText);

   }
