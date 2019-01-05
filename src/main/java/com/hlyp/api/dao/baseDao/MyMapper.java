package com.hlyp.api.dao.baseDao;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by Lenovo on 2018/12/25.
 */
public interface MyMapper<T> extends Mapper<T>,MySqlMapper<T> {
}
