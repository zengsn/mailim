package mailim.mailim;

import android.app.Application;

/**
 * Created by zzh on 2017/8/30.
 */
public class Data extends Application {
    private boolean isLogin;
    private String pop3Server = "pop.163.com";
    private String smtpServer = "smtp.163.com";
    private String imapServer = "imap.163.com";
    private String protocol = "pop3";
    private String user = "zhangzhanhong218";
    private String pwd = "1234567zzh";

    @Override
    public void onCreate(){
        inti();
        super.onCreate();
    }

    private void inti(){
        isLogin = false;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
