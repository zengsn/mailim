package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/9/16.
 */
public class Message implements Serializable {
    private int raw;
    private String username;
    private String email;
    private String last;
    private String time;

    public Message(String name,String email,String last){
        setUsername(name);
        setEmail(email);
        setLast(last);
        setRaw(1);
    }

    @Override
    public boolean equals(Object obj) {
        String string = (String)obj;
        return username.equals(string);
    }

    public boolean isRaw(){
        return raw>0;
    }

    public void addRaw(){
        raw++;
    }

    public void subRaw(){
        if(raw>0)raw--;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
