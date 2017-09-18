package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/9/16.
 */
public class Message implements Serializable {
    private int raw;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }
}
