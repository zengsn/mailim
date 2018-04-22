package mailim.mailim.entity;

import java.io.Serializable;

/**
 * Created by zzh on 2017/9/16.
 */
public class User implements Serializable {
    private String email;// 邮箱
    private String password;// 密码
    private String nickname;// 昵称
    private boolean sex;// 性别
    private int age;//年龄
    private String city;//城市
    private String motto;// 座右铭

    public User(){
        sex = true;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User)obj;
        return email.equals(user.email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
