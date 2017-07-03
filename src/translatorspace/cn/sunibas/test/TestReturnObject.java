package cn.sunibas.test;

import cn.sunibas.action.retObject.GetManuscriptChipActionReturnObject;
import cn.sunibas.util.Object2JSON;
import org.junit.Test;

/**
 * Created by ibas on 2/26/17.
 */
public class TestReturnObject {
    @Test
    public void testOne() {
        GetManuscriptChipActionReturnObject getManuscriptChipActionReturnObject
                = new GetManuscriptChipActionReturnObject();
        getManuscriptChipActionReturnObject.setStatusOnly(200);
        System.out.print(Object2JSON.Object2Json(getManuscriptChipActionReturnObject));
    }
}
