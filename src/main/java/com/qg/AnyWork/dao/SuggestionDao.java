package com.qg.AnyWork.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by logan on 2017/8/18.
 */
@Mapper
@Repository
public interface SuggestionDao {
    int addSuggestion(@Param("userId") int userId, @Param("suggestion")String suggestion);
}
