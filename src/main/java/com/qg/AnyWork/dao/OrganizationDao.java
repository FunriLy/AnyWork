package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Organization;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 */
@Mapper
@Repository
public interface OrganizationDao {

    /***
     * 通过关键字查找相关组织
     * @param organizationName
     * @return
     */
    List<Organization> getByKeyWords(@Param("organizationName") String organizationName);

    /***
     * 查看用户加入的组织
     * @param userId
     * @return
     */
    List<Organization> getByUserId(@Param("userId") int userId);


    /***
     * 根据组织id查找组织
     * @param organizationId
     * @return
     */
    Organization getById(@Param("organizationId") int organizationId);

    /***
     * 加入组织
     * @param organizationId
     * @param userId
     * @return
     */
    int joinOrganization(@Param("organizationId") int organizationId,@Param("userId") int userId);

    /***
     * 查看是否加入过组织
     * @param organizationId
     * @param userId
     * @return
     */
    int isJoin(@Param("organizationId") int organizationId,@Param("userId") int userId);
}
