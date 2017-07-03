package cn.sunibas.action.retObject;

import cn.sunibas.action.retObject.abstractClass.CommonProto;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ManuscriptActionReturnObject extends CommonProto {
    /**
     * 200  上传生工
     * 300  上传成功但是文件数量有异常
     * 400  上传失败或者没有文件
     * 500  没有登陆
     * 600  没有设置语言
     * */
    public ManuscriptActionReturnObject() {
        setStatusRetStrMap(200,"上传成功");
        setStatusRetStrMap(300,"上传成功但是文件数量异常");
        setStatusRetStrMap(400,"上传失败或没有文件");
        //setStatusRetStrMap(500,"没有登陆");
        setStatusRetStrMap(600,"没有设置语言");
    }
 }
