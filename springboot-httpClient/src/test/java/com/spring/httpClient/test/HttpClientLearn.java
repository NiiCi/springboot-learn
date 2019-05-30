package com.spring.httpClient.test;

import com.alibaba.fastjson.JSONObject;
import com.spring.httpClient.bean.ResultPage;
import com.spring.httpClient.util.GeoHashUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class HttpClientLearn {

    /**
     * httpGet 请求
     */

    //1.从数据库获取 经纬度的集合
    //2.从上一个请求中获取Cookie
    //3.循环调用方法 , offset 每次加24

    public static void doGetEleShopInfo(String lat, String lon, String Cookie) throws URISyntaxException {
        long offset = 0L;
        lat = "30.281037";
        lon = "120.026646";
        String terminal = "web";
        Cookie = "tzyy=ec7da8b4be8178d57d9d3fe17ca95a3c; SID=2803R3TVAB0gmpEMRLfoVItP0Uyn8YkChv3A;";
        String geohash = GeoHashUtil.encode(Double.parseDouble(lat), Double.parseDouble(lon));
        // 获取一个 httpClient 客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //HttpGet 实现了 HttpUriRequest 接口
        HttpGet httpGet = new HttpGet();
        // 设置 请求路径 和 请求参数

        // get 请求携带参数
        httpGet.setHeader("Cookie", Cookie);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3676.400 QQBrowser/10.4.3505.400");

        // 请求参数配置 , 设置超时时间和重定向
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .setSocketTimeout(60000)
                .setRedirectsEnabled(true)
                .build();
        // 将配置信息 加载到 httpGet 中
        httpGet.setConfig(requestConfig);

        List<List<ResultPage>> shopInfo = new ArrayList<>();

        try {
            for (int i = 0; i < 10000 ; i++) {
                Thread.sleep(5000);
                offset = 24 * i;
                log.info("---- 当前页: "+offset);
                URI uri = new URIBuilder("https://www.ele.me/restapi/shopping/restaurants")
                        .setParameter("latitude", lat)
                        .setParameter("longitude", lon)
                        .setParameter("offset", String.valueOf(offset))
                        .setParameter("limit", "24")
                        .setParameter("extras[]", "activities")
                        //.setParameter("extras[]","tags")
                        .setParameter("geohash", geohash)
                        //.setParameter("extra_filters","home")
                        //rank_id 参数 不能省去,省去则需要重新登录
                        //.setParameter("rank_id","")
                        .setParameter("terminal", terminal)
                        .build();
                httpGet.setURI(uri);
                CloseableHttpResponse httpResponse = null;
                // 执行 get 请求
                // for 循环执行
                httpResponse = httpClient.execute(httpGet);
                if (httpResponse == null || httpResponse.getStatusLine() == null) {
                    return;
                }
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 获取结果集实体 , 转换成字符串
                    String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    log.info(strResult);
                    List<ResultPage> resultPage = JSONObject.parseArray(strResult, ResultPage.class);
                   /* resultPage.parallelStream().forEachOrdered(resultPage1 -> {
                        log.info(JSONObject.toJSON(resultPage1));
                    });*/
                    shopInfo.add(resultPage);
                    log.info(resultPage.stream().count());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Test
    public void testApi() throws URISyntaxException {
        doGetEleShopInfo("","","");
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
            log.error(e.getMessage(), e);
        } finally {
            {
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

    }
}
