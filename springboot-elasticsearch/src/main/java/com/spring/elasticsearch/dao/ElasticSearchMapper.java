package com.spring.elasticsearch.dao;

import com.spring.elasticsearch.bean.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 继承 Repository 接口下的子接口 ElasticsearchRepository 实现对 es 的 CRUD
 */
public interface ElasticSearchMapper extends ElasticsearchRepository<Car,Long> {

    public List<Car> findByPriceBetween(double p1, double p2);
}
