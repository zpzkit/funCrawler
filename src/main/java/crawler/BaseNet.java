package crawler;

import crawler.bean.SrcUrl;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by watson zhang on 16/9/19.
 */
public class BaseNet {
    Queue<SrcUrl> urlQueue = new PriorityQueue<>();

    public void busyBox(){


    }

    String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/536.36 (KHTML, like Gecko) Chrome/45.0.2452.101 Safari/536.36";
    public void getContent(String url, String charset){
        CookieStore cookieStore = new BasicCookieStore();
        Cookie cookie = new BasicClientCookie("123", "123");
        cookieStore.addCookie(cookie);
        try {
            String s = Executor.newInstance().execute(
                    Request.Get(url).userAgent(ua).connectTimeout(2000))
                    .returnContent().asString(Charset.forName(charset));
            //System.out.println(s);
            Document parse = Jsoup.parse(s);
            String s1 = parse.select(".f-d-item-content.a").outerHtml();
            System.out.println(s1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        BaseNet baseNet = new BaseNet();
        String url = "http://tieba.baidu.com";
        String charset = "gb2312";
        baseNet.getContent(url, charset);
    }
}
