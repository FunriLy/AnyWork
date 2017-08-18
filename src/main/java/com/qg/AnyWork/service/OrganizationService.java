package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.OrganizationDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.OrganizationException;
import com.qg.AnyWork.model.Chapter;
import com.qg.AnyWork.model.Organization;
import com.qg.AnyWork.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by logan on 2017/7/11.
 */
@Service
public class OrganizationService {
    @Autowired
    private OrganizationDao organizationDao;

    /***
     * 根据组织关键字模糊查找
     * @param organizationName
     * @return
     */
    public RequestResult<List<Organization>> search(String organizationName,int userId){
        if (organizationName==null) throw new OrganizationException("搜索的组织名为null");
        List<Organization> organizations = organizationDao.getByKeyWords("%"+organizationName+"%");
        List<Organization> myOrganizations = organizationDao.getByUserId(userId);
        List<Integer> myOrganizationsId = new ArrayList<Integer>();
        for (Organization myO : myOrganizations) {
            myOrganizationsId.add(myO.getOrganizationId());
        }
        for (Organization o : organizations) {
            o.setCount(organizationDao.getOrganizationCount(o.getOrganizationId()));
            if (myOrganizationsId.contains(o.getOrganizationId()))
                o.setIsJoin(1);
        }
        return new RequestResult(StatEnum.ORGAN_SEARCH_SUCCESS,organizations);
    }


    public Organization getById(int organizationId){
        return organizationDao.getById(organizationId);
    }


    /***
     * 加入组织
     * @param organizationId
     * @param token
     * @param userId
     * @return
     */
    public RequestResult<Organization> join(int organizationId,long token,int userId){
        if (organizationDao.isJoin(organizationId,userId) > 0) throw new OrganizationException("用户已加入该组织");
        Organization organization =organizationDao.getById(organizationId);
        if (organization==null) throw new OrganizationException("组织不存在");
        if (organization.getToken()!=token)throw new OrganizationException("口令错误");
        organizationDao.joinOrganization(organizationId,userId);
        return new RequestResult(StatEnum.ORGAN_JOIN_SUCCESS,organization);
    }

    /***
     * 获取我的组织列表
     * @param userId
     * @return
     */
    public RequestResult<List<Organization>> searchByUserId(int userId){
        List<Organization> organizations = organizationDao.getByUserId(userId);
        for (Organization o :
                organizations) {
            o.setIsJoin(1);
            o.setCount(organizationDao.getOrganizationCount(o.getOrganizationId()));
        }
        return new RequestResult(StatEnum.ORGAN_SEARCH_SUCCESS,organizations);
    }

    /***
     * 退出组织
     * @param organizationId
     * @param userId
     * @return
     */
    public RequestResult exitOrganization(int organizationId,int userId){
        if (organizationDao.isJoin(organizationId,userId) == 0) throw new OrganizationException("用户未加入该组织");
        int flag = organizationDao.exitOrganization(organizationId,userId);
        if (flag == 0)
            return new RequestResult(0,"退出失败");
        return new RequestResult(1,"退出成功");
    }

    /***
     * 创建组织
     * @return
     */
    public RequestResult addOrganization(Organization organization){
        int radomInt = new Random().nextInt(99999);
        organization.setToken(radomInt);
        int flag =  organizationDao.addOrganization(organization);
        organizationDao.joinOrganization(organization.getOrganizationId(),organization.getTeacherId());
        if (flag ==1)
        return new RequestResult(1,"创建组织成功");
        else return new RequestResult(0,"创建组织失败");
    }

    /***
     * 修改组织
     * @return
     */
    public RequestResult alterOrganization(Organization organization){
        organizationDao.updateOrganization(organization);
        return new RequestResult(1,"修改组织成功");
    }

    /***
     * 删除组织
     * @param organizationId
     * @param userId
     * @return
     */
    public RequestResult deleteOrganization(int organizationId,int userId){
        Organization o = organizationDao.getById(organizationId);
        if (o == null) throw new OrganizationException("组织不存在");
        if (o.getTeacherId()!=userId) throw new OrganizationException("没有删除权限");
        int flag = organizationDao.deleteOrganization(organizationId);

        if (flag == 1)  return new RequestResult(1,"删除组织成功");
        else return new RequestResult(0,"删除组织失败");
    }

    /***
     * 获取我创建过的组织列表
     * @param userId
     * @return
     */
    public RequestResult<List<Organization>> getMyOrganization(int userId){
        List<Organization> organizations =organizationDao.getMyOrganization(userId);
        for (Organization o : organizations){
            o.setCount(organizationDao.getOrganizationCount(o.getOrganizationId()));
        }
        return new RequestResult<List<Organization>>(1,"获取成功",organizations);
    }

    /**
     * 获取组织下的成员列表
     * @param organizationId
     * @return
     */
    public RequestResult<List<User>> getOrganizationPeople(int organizationId){
        if (organizationDao.getById(organizationId)==null) throw new OrganizationException("该组织不存在");
        return new RequestResult<List<User>>(1,"获取成功",organizationDao.getOrganizationPeople(organizationId));
    }




}
