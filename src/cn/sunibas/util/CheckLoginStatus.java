package cn.sunibas.util;

import cn.sunibas.entity.TSkid;
import com.opensymphony.xwork2.ActionContext;

/**
 * Created by Administrator on 2017/2/11.
 */
public class CheckLoginStatus {
    public static boolean isLogin(ActionContext actionContext){
        if (actionContext.getSession().get("kidInfo") != null) {
            return true;
        } else {
            return false;
        }
    }

    public static TSkid getKid(ActionContext actionContext){
        return (TSkid)actionContext.getSession().get("kidInfo");
    }
}
