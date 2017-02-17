package cn.sunibas.action.retObject;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ManuscriptActionReturnObject {
    /**
     * 200  上传生工
     * 300  上传成功但是文件数量有异常
     * 400  上传失败或者没有文件
     * 500  没有登陆
     * 600  没有设置语言
     * */
    private int status;
    private String retStr;

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
