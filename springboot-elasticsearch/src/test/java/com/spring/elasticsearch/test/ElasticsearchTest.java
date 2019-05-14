package com.spring.elasticsearch.test;

import com.spring.elasticsearch.bean.Car;
import com.spring.elasticsearch.dao.ElasticSearchMapper;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.DoubleTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class ElasticsearchTest {
    @Autowired
    ElasticsearchTemplate esTemplate;
    @Autowired
    ElasticSearchMapper elasticSearchMapper;

    /**
     * es 创建索引
     * 在创建索引时，可能会报错，信息如下：
     * Rejecting mapping update to [niici] as the final mapping would have more than 1 type: [cars, user]
     * 原因 es 在6.0的版本中，规定了一个index中不能出现多个type，在之后7.0的版本中会删除掉type
     */
    @Test
    public void createIndex() {
        // 1.创建索引
        esTemplate.createIndex(Car.class);
        // 2.指定映射配置
        esTemplate.putMapping(Car.class);
    }

    /**
     * 删除索引
     * 可以根据类名或者indexName
     */
    @Test
    public void deleteIndex() {
        esTemplate.deleteIndex(Car.class);
        esTemplate.deleteIndex("niici2");
    }

    /**
     * es 的新增和修改调用的是同一个方法，即修改会覆盖原有的数据
     */
    @Test
    public void addData() {
        Car car = new Car(1L, 10000, "red", "honda", "2014-10-28");
        elasticSearchMapper.save(car);
    }

    @Test
    public void addDataList() {
        Car car1 = new Car(2L, 20000, "red", "honda", "2014-11-05");
        Car car2 = new Car(3L, 30000, "green", "ford", "2014-05-18");
        ;
        elasticSearchMapper.saveAll(Arrays.asList(car1, car2));
    }


    @Test
    public void query() {
        Iterable<Car> list = elasticSearchMapper.findAll(Sort.by("price").descending());
        list.forEach(System.out::println);
    }

    @Test
    public void queryByPrice() {
        elasticSearchMapper.findByPriceBetween(10000, 50000).forEach(System.out::println);
    }


    //自定义查询开始

    /**
     * 基本查询
     */
    @Test
    public void search() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("make", "honda"));
        // 执行搜索，获取结果
        Page<Car> carPage = elasticSearchMapper.search(queryBuilder.build());
        // 总记录数
        log.info("总记录数----: " + carPage.getTotalElements());
        // 总页数
        log.info("总页数----: " + carPage.getTotalPages());
        // 当前页
        log.info("当前页----: " + carPage.getNumber());
        // 单页记录数 size
        log.info("每页大小----: " + carPage.getSize());
        carPage.forEach(System.out::println);
    }

    /**
     * 分页查询
     */
    @Test
    public void searchByPage() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("make", "honda"));
        // PageRequest 是 Pageable 接口下的一个实现类 of 方法 可以指定排序方式
        queryBuilder.withPageable(PageRequest.of(0, 10));
        // 执行搜索，获取结果
        Page<Car> carPage = elasticSearchMapper.search(queryBuilder.build());
        // 总记录数
        log.info("总记录数----: " + carPage.getTotalElements());
        // 总页数
        log.info("总页数----: " + carPage.getTotalPages());
        // 当前页
        log.info("当前页----: " + carPage.getNumber());
        // 单页记录数 size
        log.info("每页大小----: " + carPage.getSize());
        carPage.forEach(System.out::println);
    }


    /**
     * 排序查询
     * 实现排序的方式有2种
     * 1.Pageable 接口下的PageReuquest实现类的 of 方法可以指定排序方式
     * 2.基本查询构造器的 withSort方法 指定排序方式
     */
    @Test
    public void searchBySort() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("make", "honda"));
        // PageRequest 是 Pageable 接口下的一个实现类 of 方法 可以指定排序方式
        //queryBuilder.withPageable(PageRequest.of(0, 10,Sort.by(Sort.Order.desc("price"))));
        queryBuilder.withPageable(PageRequest.of(0, 10));
        // 添加排序方式
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        // 执行搜索，获取结果
        Page<Car> carPage = elasticSearchMapper.search(queryBuilder.build());
        // 总记录数
        log.info("总记录数----: " + carPage.getTotalElements());
        // 总页数
        log.info("总页数----: " + carPage.getTotalPages());
        // 当前页
        log.info("当前页----: " + carPage.getNumber());
        // 单页记录数 size
        log.info("每页大小----: " + carPage.getSize());
        carPage.forEach(System.out::println);
    }

    /**
     * 基本聚合
     */
    @Test
    public void queryAgg() {
        // 创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询任何数据结果，只查询聚合结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 添加一个聚合 聚合类型为 terms 聚合名称为 colors 聚合 字段为 color
        // AggreagetionBuilders 是 聚合的构造器
        queryBuilder.addAggregation(AggregationBuilders.terms("colors").field("color"));
        // 执行查询，将查询结果 强转为 AggregatedPage 类型
        AggregatedPage<Car> aggPage = (AggregatedPage<Car>) elasticSearchMapper.search(queryBuilder.build());
        // 解析
        // 从结果中 获取 名为prices的聚合
        // 通过聚合名获取聚合
        // 因为是通过 double 类型来进行的 term 聚合，所以要强转为 DoubleTerms 类型
        StringTerms agg = (StringTerms) aggPage.getAggregation("colors");
        //  获取聚合下的桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        buckets.forEach(bucket -> {
            log.info(bucket.getKeyAsString());
            log.info(bucket.getDocCount());
        });
    }

    /**
     * 聚合嵌套
     */
    @Test
    public void subAgg() {
        // 创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询数据结果，只查询聚合结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 在 color 的聚合内 添加一个子聚合 求价格平均值
        queryBuilder.addAggregation(AggregationBuilders.terms("colors").field("color")
                .subAggregation(AggregationBuilders.avg("avg_price").field("price")));

        // 执行查询
        AggregatedPage<Car> aggPage = (AggregatedPage<Car>) elasticSearchMapper.search(queryBuilder.build());

        // 解析
        StringTerms agg = (StringTerms) aggPage.getAggregation("colors");

        List<StringTerms.Bucket> buckets = agg.getBuckets();

        buckets.forEach(bucket -> {
            log.info(bucket.getKeyAsString() + " ，共" + bucket.getDocCount() + " 台");

            // 获取子聚合结果
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("avg_price");
            log.info("平均售价: " + avg.getValue());
        });
    }
    //自定义查询结束
}
