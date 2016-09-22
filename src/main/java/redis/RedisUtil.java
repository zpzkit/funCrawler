package redis;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;


/**
 * Created by watson zhang on 16/6/22.
 */
public class RedisUtil {
    PropertiesConfiguration conf;
    String rHost;
    int rPort;

    public static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    public Jedis jedis;

    public RedisUtil(){
        try {
            conf = new PropertiesConfiguration("redis.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        rHost = conf.getString("host", "172.16.31.131");
        rPort = conf.getInt("port", 6379);
        try {
            jedis = new Jedis(rHost);
            JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), rHost, rPort);
            jedis = jedisPool.getResource();
        }catch (JedisException e){
            logger.error("connect {} error", rHost);
        }
    }

    public void pipeline(){
        Pipeline p = jedis.pipelined();
        for (int i = 0; i < 10000; i++){
            p.set(String.valueOf(i),String.valueOf(i));
        }
        HashMap<String, Double> set = new HashMap<>();
        set.put("a", (double) 0);
        set.put("b", (double) 1);
        set.put("c", (double) 2);
        set.put("d", (double) 3);
        set.put("e", (double) 4);
        p.zadd("chat_0", set);
        Response response = p.zrange("chat_0", 0, -1);
        Response response1 = p.zrange("chat_0", 0, -1);
        p.sync();
        Response<String> mess = p.get("1");
    }

    public void publishOne(String channel, final String mess){
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    jedis.publish(channel, mess);
                } catch (Exception ex) {
                }
            }
        });
        t.start();
    }

    public void pubsub(){
        jedis.subscribe(new JedisPubSub(){
            public void onMessage(String channel, String message) {
                unsubscribe();
            }

            public void onSubscribe(String channel, int subscribedChannels) {
                // now that I'm subscribed... publish
                publishOne("foo", "exit");
                logger.info(channel, subscribedChannels);
            }

            public void onUnsubscribe(String channel, int subscribedChannels) {
                logger.info(channel,subscribedChannels);
            }
        }, "foo");
    }

    public static void main(String[] args){
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.pipeline();
        return;
    }
}
