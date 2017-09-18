package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/9/16.
 */
public class User implements Serializable {
    private String username;
    private String remarksName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemarksName() {
        return remarksName;
    }

    public void setRemarksName(String remarksName) {
        this.remarksName = remarksName;
    }
}
