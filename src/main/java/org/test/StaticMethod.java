package org.test;


import redis.clients.jedis.Jedis;

import java.util.Optional;

/**
 * Created by watson zhang on 16/4/15.
 */
public class StaticMethod {
    public static void main(String[] args) throws Exception {
        Optional<String> a  = Optional.empty();
        System.out.println(a.get());
    }

}
