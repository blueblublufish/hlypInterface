package com.hlyp.api.dao;

import com.hlyp.api.bean.User;
import com.hlyp.api.dao.baseDao.MyMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Lenovo on 2018/12/25.
 */
@Mapper
public interface UserDao extends MyMapper<User> {
   /* @Select("SELECT * FROM USER WHERE username = #{username}")
    User findByName(@Param("username") String username);

    @Insert("INSERT INTO USER(username,sex) VALUES (#{username},#{sex})")
    int insert(@Param("username")String username,@Param("sex") String sex);*/
}
