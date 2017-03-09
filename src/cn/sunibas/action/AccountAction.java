package cn.sunibas.action;

/**
 * Created by IBAS on 2017/2/7.
 */

import cn.sunibas.entity.TSkid;
import cn.sunibas.service.ITSkidService;
import cn.sunibas.util.CheckLoginStatus;
import cn.sunibas.util.MyUUID;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Date;

/**
 * 主要负责账号管理，例如登录，注册，忘记密码等操作
 * 这里应该全部改为异步的处理方式，使用json
 * */
public class AccountAction extends ActionSupport {

    //封装请求数据
    private TSkid tSkid = new TSkid();

    public TSkid getTSkid() {
        return tSkid;
    }

    public void setTSkid(TSkid tSkid) {
        this.tSkid = tSkid;
    }

    //ioc
    private ITSkidService itSkidService;

    public void setItSkidService(ITSkidService itSkidService) {
        this.itSkidService = itSkidService;
    }

    public String login() {
        if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
            return "login_success";
        }
        TSkid kid = itSkidService.login(tSkid);
        if (kid == null) {
            System.out.println("kid is null");
            return "login_fail";
        } else {
            System.out.println("kid is not null");
            ActionContext
                    .getContext()
                    .getSession()
                    .put("kidInfo",kid);
            return "login_success";
        }
    }

    public String register(){
        if (CheckLoginStatus.isLogin(ActionContext.getContext())) {
            return "login_success";
        }
        //这里是否应该加入注册的一些后端审查条件?
        //对于重名是否介意？
        //首先检查基本信息是否已经填写完毕
        boolean isOver = (tSkid.getName() != null) &&
                         (tSkid.getPwd() != null);
        //如果信息不完整则注册失败（或者还有检查重名，信息规则等）
        if (isOver) {
            //检查通过，负责时间和id
            tSkid.setId(MyUUID.getMyUUID());
            tSkid.setCreateTime(new Date());
        } else {
            return "register_fail";
        }
        itSkidService.register(tSkid);
        tSkid.setChecked(0);
        ActionContext
                .getContext()
                .getSession()
                .put("kidInfo",tSkid);
        return "register_success";
    }

    public String logout() {
        ActionContext.getContext().getSession().put("kidInfo",null);
        return "login_success";
    }

    /**
     * 获取全部小译的名称，并进行比对后进行命名提示，避免重名(暂时不编码)
     * */
    public void getNames(){

    }
}
