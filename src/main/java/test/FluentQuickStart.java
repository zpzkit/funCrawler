package test;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class FluentQuickStart {

    public static void main(String[] args) throws Exception {
        // The fluent API relieves the user from having to deal with manual
        // deallocation of system resources at the cost of having to buffer
        // response content in memory in some cases.

        Request.Get("http://www.baidu.com")
                .execute().returnContent();
        Request.Get("weibo.com").execute().returnContent();
        return;
/*        Request.Post("http://targethost/login")
                .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
                .execute().returnContent();*/
    }
}
