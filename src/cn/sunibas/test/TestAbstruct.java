package cn.sunibas.test;

import cn.sunibas.action.retObject.abstractClass.CommonProto;
import cn.sunibas.action.retObject.abstractClass.ReturnStatus;
import cn.sunibas.util.ForDebug;
import cn.sunibas.util.MyStringExt;
import cn.sunibas.util.Object2JSON;
import org.junit.Test;

/**
 * Created by sunbing on 17-3-4.
 */
public class TestAbstruct {
    @Test
    public void testOne() {
        CommonProto commonProto = new CommonProto() {
        };
        commonProto.setStatusOnly(ReturnStatus.Success);
        System.out.println(Object2JSON.Object2Json(commonProto));
    }
}
