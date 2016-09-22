package org.MongoTest;

import com.mongodb.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.mongodb.morphia.utils.IndexDirection.*;

/**
 * Created by watson zhang on 16/4/13.
 */
@Entity
public class MongoTest {
    @Indexed(value = ASC, name = "bandName",unique = true, sparse = true)
    private String id;

    DBCollection dc;
    DB db;

    public static void main(String[] args) throws UnknownHostException {

        MongoTest test = new MongoTest();
        test.findAll();
        test.addObj();
        test.addList();

    }

    public MongoTest() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("mytest");
        dc = db.getCollection("test");

    }

    public void findAll(){
        DBCursor cursor = dc.find();
        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }


    public void addObj(){
        DBObject user = new BasicDBObject();
        user.put("name", "Watson");
        user.put("age", 18);
        user.put("gender", "man");

        dc.insert(user);
        findAll();
    }

    public void addList(){
        DBObject user = new BasicDBObject();
        user.put("name","Jeak");
        user.put("age", 20);
        user.put("gender", "man");

        DBObject user2 = new BasicDBObject();
        user2.put("name", "Jeak");
        user2.put("age", 24);

        List<DBObject> dbList = new ArrayList<>();
        dbList.add(user);
        dbList.add(user2);

        WriteConcern writeConcern = new WriteConcern();
        System.out.println(writeConcern.callGetLastError());
        writeConcern.getWString();
        dc.insert(dbList);
        findAll();
    }

    public void update(){
        DBObject user = new BasicDBObject();
        //dc.update()


    }
}
