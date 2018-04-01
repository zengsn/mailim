package mailim.mailim.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzh on 2017/9/16.
 */
public class Friend implements Serializable {
    private String email;
    private int status;
    private String emailLastTime;
    private String username;
    private int star;

    public static int STAR_DEFAULT = 0;
    public static int STAR_USER = -1;
    public static int STAR_ONE = 1;

    public Friend(){
        status = 0;
        star = STAR_DEFAULT;
        setEmailLastTime(String.valueOf(System.currentTimeMillis()/1000));
    }

    public Friend(String email){
        setEmail(email);
        status = 0;
        setEmailLastTime(String.valueOf(System.currentTimeMillis()/1000));
    }

    @Override
    public boolean equals(Object obj) {
        Friend fridnd = (Friend)obj;
        return email.equals(fridnd.getEmail());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getEmailLastTime() {
        return emailLastTime;
    }

    public void setEmailLastTime(String emailLastTime) {
        this.emailLastTime = emailLastTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
