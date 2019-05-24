package com.niici.jsoup.test;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class JsoupTestApplication {

    @Test
    public void doGetProvince() {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/index.html").get();
            Elements element = doc.select("table[class=provincetable]").select("tr[class=provincetr]").select("tr");
            element.stream().forEach(ele -> {
                String addressName = ele.select("a").text();
                log.info(addressName);
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void doGetCity() {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/13.html").get();
            Elements element = doc.select("table[class=citytable]").select("tr[class=citytr]").select("td");
            for (int i = 1; i <= element.size(); i+=2) {
                log.info(element.get(i).select("a").text());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void doGetArea() {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/13/1301.html").get();
            Elements element = doc.select("table[class=countytable]").select("tr[class=countytr]");
            for (int i = 1; i < element.size(); i++) {
                log.info(element.get(i).select("tr").select("td").get(1).text());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    @Test
    public void doGetStreet(){
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/33/01/330102.html").get();
            Elements element = doc.select("table[class=towntable]").select("tr[class=towntr]").select("td");
            for (int i = 1; i <= element.size(); i+=2) {
                log.info(element.get(i).select("a").text());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }}
