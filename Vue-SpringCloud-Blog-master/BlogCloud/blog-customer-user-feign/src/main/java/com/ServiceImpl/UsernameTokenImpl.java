package com.ServiceImpl;

import com.Service.UsernameToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

@Service
public class UsernameTokenImpl implements UsernameToken {

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getUsername(String token) {

        //根据Redis中的双向绑定可以根据Token来获得username
            //打开Redis
            Jedis jedis = new Jedis("121.43.96.182", 15112,2000);
            jedis.auth("123456");
            String username="";
            username=jedis.get(token);
            return username;
    }
}
