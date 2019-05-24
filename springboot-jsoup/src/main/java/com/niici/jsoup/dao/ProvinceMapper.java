package com.niici.jsoup.dao;


import com.niici.jsoup.bean.Province;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface ProvinceMapper extends Mapper<Province>, InsertListMapper<Province> {
}
