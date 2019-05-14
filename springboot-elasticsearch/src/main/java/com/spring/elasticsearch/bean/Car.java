package com.spring.elasticsearch.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
/**
 * 作用在类上，标记实体类为 es的文档对象
 * indexName: 对应索引库名称 ---- 相当于数据库
 * type: 对应在索引库中的类型 ---- 相当于表
 * shards: 分片数量，默认为5
 * replicas:　副本数量，默认为1
 */
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "niici2",type = "cars",shards = 1,replicas = 0)
public class Car {
    @Id
    private long id;
    /**
     * Field 标记属性为文档的一个字段，并指定映射配置
     * type: 字段类型，FieldType枚举类提供了可供选择的类型
     * index: 是否索引，只有为true的字段，才能被查询，默认为true
     * store: 是否存储，es 默认会在 _source 中备份一份，store为true时会额外再存储一份，一般默认为false
     * analyzer: 分词器名称，ik 分词器名称为 ik_max_word
     */
    @Field(type = FieldType.Long)
    private double price;
    @Field(type= FieldType.Keyword)
    private String color;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",fielddata = true)
    private String make;
    @Field(type = FieldType.Date)
    private String sold;
}
