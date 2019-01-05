package com.hlyp.api.dao;

import com.hlyp.api.bean.User;
import com.hlyp.api.bean.UserExample;
import java.util.List;

import com.hlyp.api.dao.baseDao.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper extends MyMapper<User> {

}