package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/9/16.
 */
public class User implements Serializable {
    private int id;
    private String username;// 用户名
    private String password;// 密码
    private String nickname;// 昵称
    private boolean sex;// 性别
    private String email;// 邮箱
    private String emailpwd;//邮箱授权码
    private String qianming;// 个性签名

    public User(){
        int id = 0;
        sex = true;
    }

    public void clear(){
        id = -1;
        username = "";
        password = "";
        nickname = "";
        email = "";
        emailpwd = "";
        qianming = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
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

    public String getQianming() {
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }
}
