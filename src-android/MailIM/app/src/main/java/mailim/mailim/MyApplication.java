package mailim.mailim;

import android.app.Application;
import android.content.Context;

/**
 * Created by zzh on 2017/8/30.
 */
public class MyApplication extends Application {
    private boolean isLogin;
    private String pop3Server = "pop.163.com";
    private String smtpServer = "smtp.163.com";
    private String imapServer = "imap.163.com";
    private String protocol = "pop3";
    private String email = "zhangzhanhong218@163.com";
    private String emailpwd = "1234567zzh";
    private String username = "";
    private String password = "";

    @Override
    public void onCreate(){
        inti();
        super.onCreate();
    }

    public Context getContext(){
        return this.getApplicationContext();
    }

    private void inti(){
        setLogin(false);
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getPop3Server() {
        return pop3Server;
    }

    public void setPop3Server(String pop3Server) {
        this.pop3Server = pop3Server;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getImapServer() {
        return imapServer;
    }

    public void setImapServer(String imapServer) {
        this.imapServer = imapServer;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailpwd() {
        return emailpwd;
    }

    public void setEmailpwd(String emailpwd) {
        this.emailpwd = emailpwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
