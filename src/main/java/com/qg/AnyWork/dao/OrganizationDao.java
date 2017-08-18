package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Organization;

import com.qg.AnyWork.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 * 对于组织的数据库操作
 */
@Mapper
@Repository
public interface OrganizationDao {

    /***
     * 通过关键字查找相关组织
     * @param organizationName 组织名关键字
     * @return List<Organization> 组织列表
     */
    List<Organization> getByKeyWords(@Param("organizationName") String organizationName);

    /***
     * 查看用户加入的组织
     * @param userId 用户
     * @return List<Organization> 用户加入的组织列表
     */
    List<Organization> getByUserId(@Param("userId") int userId);

    /***
     * 加入组织
     * @param organizationId 组织id
     * @param userId 用户id
     * @return int 返回影响的行数 1即成功 0即失败
     */
    int joinOrganization(@Param("organizationId") int organizationId,@Param("userId") int userId);

    /***
     * 查看是否加入过组织
     * @param organizationId 组织id
     * @param userId 用户id
     * @return int 返回满足条件的行数 1即加入 0即未加入
     */
    int isJoin(@Param("organizationId") int organizationId,@Param("userId") int userId);

    /***
     * 根据组织id查找组织
     * @param organizationId 组织id
     * @return Organization 组织
     */
    Organization getById(@Param("organizationId") int organizationId);

    /***
     * 退出组织接口
     * @param organizationId  组织id
     * @param userId 用户id
     * @return
     */
    int exitOrganization(@Param("organizationId") int organizationId,@Param("userId") int userId);

    /***
     * 获取当前组织下的人数
     * @param organizationId
     * @return
     */
    int getOrganizationCount(@Param("organizationId")int organizationId);

    /***
     * 添加组织借口
     * @param organization
     * @return
     */
    int addOrganization(@Param("organization") Organization organization);

    /***
     * 更新组织信息
     * @param organization
     * @return
     */
    int updateOrganization(@Param("organization") Organization organization);

    /***
     * 删除组织
     * @param organizationId
     * @return
     */
    int deleteOrganization(@Param("organizationId") int organizationId);

    /***
     * 查看我创建过的组织
     * @param userId
     * @return
     */
    List<Organization> getMyOrganization(@Param("userId") int userId);

    /***
     * 获得该组织下的成员列表
     * @param organizationId
     * @return
     */
    List<User> getOrganizationPeople(@Param("organizationId") int organizationId);


}
