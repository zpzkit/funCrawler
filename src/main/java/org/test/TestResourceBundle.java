package org.test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * Created by watson zhang on 16/4/29.
 */
public class TestResourceBundle {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("mytest");
        if(db == null){

        }
        DBCollection dbCollection = db.getCollection("test");


    }
}
