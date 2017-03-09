package cn.sunibas.action.retObject.abstractClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ibas on 2/26/17.
 */
public abstract class CommonProto {
    private int status;
    private String retStr;
    private Map<Integer,String> statusToRetStr = new HashMap<Integer, String>();

    public CommonProto(){
        statusToRetStr.put(600,"设置错误");
        statusToRetStr.put(500,"没有登陆");
        statusToRetStr.put(400,"失败");
        statusToRetStr.put(300,"发生错误");
        statusToRetStr.put(200,"成功");
    }

    public void setStatusRetStrMap(int status,String retStr) {
        statusToRetStr.put(status,retStr);
    }

    public void setStatusOnly(int status) {
        this.status = status;
        this.retStr = statusToRetStr.get(status);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRetStr() {
        return retStr;
    }

    public void setRetStr(String retStr) {
        this.retStr = retStr;
    }
}
