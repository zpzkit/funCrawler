package crawler;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by watson zhang on 16/9/19.
 */
public class DownLoadFileV4 {

    /* 下载 url 指向的网页 */
    public String downloadFile(String url) throws Exception {
        String filePath = null;
        // 初始化，此处构造函数就与3.1中不同
        HttpClient httpclient = new DefaultHttpClient();
        //设置代理和超时，没有使用代理时注掉
        HttpHost proxy = new HttpHost("172.16.91.109", 808);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);


        HttpHost targetHost = new HttpHost(url.replace("http://", ""));
        HttpGet httpget = new HttpGet("/");
        // 查看默认request头部信息
        System.out.println("Accept-Charset:" + httpget.getFirstHeader("Accept-Charset"));
        // 以下这条如果不加会发现无论你设置Accept-Charset为gbk还是utf-8，他都会默认返回gb2312（本例针对google.cn来说）
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
        // 用逗号分隔显示可以同时接受多种编码
        httpget.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpget.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        // 验证头部信息设置生效
        System.out.println("Accept-Charset:" + httpget.getFirstHeader("Accept-Charset").getValue());

        // Execute HTTP request
        System.out.println("executing request " + httpget.getURI());

        HttpResponse response = null;
        try {
            response = httpclient.execute(targetHost, httpget);

            // HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println("Location: " + response.getLastHeader("Location"));
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getLastHeader("Content-Type"));
            System.out.println(response.getLastHeader("Content-Length"));
            System.out.println("----------------------------------------");

            // 判断页面返回状态判断是否进行转向抓取新链接
            int statusCode = response.getStatusLine().getStatusCode();
            if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY) || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_SEE_OTHER)
                    || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                // 此处重定向处理 此处还未验证
                String newUri = response.getLastHeader("Location").getValue();
                httpclient = new DefaultHttpClient();
                httpget = new HttpGet(newUri);
                response = httpclient.execute(httpget);
            }

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            // 查看所有返回头部信息
            Header headers[] = response.getAllHeaders();
            int ii = 0;
            while (ii < headers.length) {
                System.out.println(headers[ii].getName() + ": " + headers[ii].getValue());
                ++ii;
            }

            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            if (entity != null) {
                // 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
                byte[] bytes = EntityUtils.toByteArray(entity);
                if(response.getLastHeader("Content-Type") != null){
                    filePath = "f:\\spider\\" + getFileNameByUrl(url, response.getLastHeader("Content-Type").getValue());
                }else{
                    filePath = "f:\\spider\\" + url.substring(url.lastIndexOf("/"), url.length());
                }
                saveToLocal(bytes, filePath);

                String charSet = "";

                // 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
                charSet = EntityUtils.getContentCharSet(entity);

                System.out.println("In header: " + charSet);
                // 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
                if (charSet == "") {
                    String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
                    Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
                    boolean result = m.find();
                    if (m.groupCount() == 1) {
                        charSet = m.group(1);
                    } else {
                        charSet = "";
                    }
                }
                System.out.println("Last get: " + charSet);
                // 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
                //System.out.println("Encoding string is: " + new String(bytes, charSet));
            }} catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();
            httpget.abort();
        }

        return filePath;
    }

    /**
     * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
     */
    public String getFileNameByUrl(String url, String contentType) {
        // remove http://
        url = url.substring(7);
        // text/html类型
        if (contentType.indexOf("html") != -1&& url.indexOf(".jpg")!=-1&& url.indexOf(".gif")!=-1) {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
            return url;
        }

        else if(url.indexOf(".jpg")!=-1|| url.indexOf(".gif")!=-1){
            url =url;
            return url;
        }// 如application/pdf类型
        else {
            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    /**
     * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
     */
    private void saveToLocal(byte[] data, String filePath) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            for (int i = 0; i < data.length; i++)
                out.write(data[i]);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
