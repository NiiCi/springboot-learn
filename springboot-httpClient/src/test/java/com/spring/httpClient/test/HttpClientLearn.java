package com.spring.httpClient.test;

import com.alibaba.fastjson.JSONObject;
import com.spring.httpClient.HttpClientApplication;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.AbstractExecutionAwareRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class HttpClientLearn {

    /**
     * httpGet 请求
     */
    @Test
    public void doGet() throws URISyntaxException {
        // 获取一个 httpClient 客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //HttpGet 实现了 HttpUriRequest 接口
        HttpGet httpGet = new HttpGet();
        // 设置 请求路径 和 请求参数
        URI uri = new URIBuilder("http://www.baidu.com").setParameter("wd", "java").build();
        // get 请求携带参数
        httpGet.setURI(uri);
        // 请求参数配置 , 设置超时时间和重定向
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        // 将配置信息 加载到 httpGet 中
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;

        try {
            // 执行 get 请求
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse == null || httpResponse.getStatusLine() == null) {
                return;
            }
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 获取结果集实体 , 转换成字符串
                String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                log.info(JSONObject.toJSONString(strResult), "UTF-8");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * httpPost 请求
     */
    @Test
    public void doPost() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://byoa.baoyun2018.com/api/baoyun/oa/user/login");
        // 创建一个 entity 模拟一个表单
        List<NameValuePair> params = new ArrayList<>();
        // 添加请求参数
        params.add(new BasicNameValuePair("accountName", "15174902014"));
        params.add(new BasicNameValuePair("password", "@FlyingPig"));

        try {
            // 包装成一个 entity 对象
            StringEntity entity = new UrlEncodedFormEntity(params);
            // 加载到 post 请求中
            httpPost.setEntity(entity);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            // 获取 响应码
            int code = httpResponse.getStatusLine().getStatusCode();
            log.info(code);
            // 获取结果集
            HttpEntity contentEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(contentEntity);
            log.info(result);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }

    }
}
