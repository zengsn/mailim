package cn.sunibas.action;

import cn.sunibas.redis.RedisBackUp;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by sunbing on 17-3-7.
 */
public class StartBakThread extends ActionSupport {
    RedisBackUp redisBackUp;

    public void setRedisBackUp(RedisBackUp redisBackUp) {
        this.redisBackUp = redisBackUp;
    }

    public void bak() {
        redisBackUp.init();
    }
}
