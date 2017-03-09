package cn.sunibas.action.retObject;

/**
 * Created by IBAS on 2017/2/23.
 */
public class ManuscriptTranslateReturnObject {
    /**
     * 200  查询成功
     * 300  文稿丢失
     * 400  查询失败（没有内容）
     * */
    private int status;
    private String retStr;
    private PackageTSTextPart packageTSTextPart;

    public void oneSet(int status){
        this.status = status;
        switch (status) {
            case 200:
                this.retStr = "查询成功";
                break;
            case 300:
                this.retStr = "文稿丢失";
                break;
            case 400:
                this.retStr = "查询失败";
                break;
            case 500:
                this.retStr = "没有登陆";
                break;
        }
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

    public PackageTSTextPart getPackageTSTextPart() {
        return packageTSTextPart;
    }

    public void setPackageTSTextPart(PackageTSTextPart packageTSTextPart) {
        if (packageTSTextPart.getPartText().equals("")) {
            this.status = 400;
            return ;
        }
        this.packageTSTextPart = packageTSTextPart;
    }
}
