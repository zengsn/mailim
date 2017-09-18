package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/8/30.
 */
public class Account implements Serializable {
    private int id = 0;
    private String username;
    private String nickname;
    private String psw;
    private String email;
    private String e_psw;
    private boolean isLogin;
    private boolean validity_email;

    public Account(String username, String psw){
        super();
        this.id = 0;
        this.username = username;
        this.psw = psw;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getE_psw() {
        return e_psw;
    }

    public void setE_psw(String e_psw) {
        this.e_psw = e_psw;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isValidity_email() {
        return validity_email;
    }

    public void setValidity_email(boolean validity_email) {
        this.validity_email = validity_email;
    }
}
