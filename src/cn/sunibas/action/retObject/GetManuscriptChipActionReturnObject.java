package cn.sunibas.action.retObject;

import cn.sunibas.action.retObject.abstractClass.CommonProto;

/**
 * Created by IBAS on 2017/2/23.
 */
public class GetManuscriptChipActionReturnObject extends CommonProto {
    /**
     * 200  查询成功
     * 300  文稿丢失
     * 400  查询失败（没有内容）
     * */
    public GetManuscriptChipActionReturnObject() {
        setStatusRetStrMap(200,"查询成功");
        setStatusRetStrMap(300,"文稿丢失");
        setStatusRetStrMap(400,"查询失败");
        //setStatusRetStrMap(500,"没有登陆");
    }

    private PackageTSTextPart packageTSTextPart;

    public PackageTSTextPart getPackageTSTextPart() {
        return packageTSTextPart;
    }

    public void setPackageTSTextPart(PackageTSTextPart packageTSTextPart) {
        if (packageTSTextPart.getPartText().equals("")) {
            setStatus(400);
            return ;
        }
        this.packageTSTextPart = packageTSTextPart;
    }
}
