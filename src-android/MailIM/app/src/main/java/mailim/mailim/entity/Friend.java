package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/9/16.
 */
public class Friend implements Serializable {
    private int status;
    private User user;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
