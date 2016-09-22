package org.MyTest;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.junit.Test;
import org.springframework.boot.test.IntegrationTest;

/**
 * Created by watson zhang on 16/4/13.
 */
public class JsonTest {
    @Test
    public void JsonTest(){
        DBObject dbObject = (DBObject) JSON.parse("{" +
                "    \"_id\" : null," +
                "    \"log\" : \"{\\\"pid\\\":\\\"111111111111111111111111111\\\",\\\"$inc\\\":{\\\"sum\\\":1},\\\"_id\\\":\\\"111111111111111111111111111\\\",\\\"$set\\\":{\\\"fac\\\":\\\"missin-do\\\",\\\"pid\\\":\\\"274\\\"}}\"" +
                "}");
        System.out.println(dbObject);
    }

    @Test
    public void Test(){
        System.out.println("123");
    }

}
