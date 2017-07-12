package com.qg.AnyWork.service;

import com.qg.AnyWork.dao.OrganizationDao;
import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.OrganizationException;
import com.qg.AnyWork.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            if (myOrganizationsId.contains(o.getOrganizationId()))
                o.setIsJoin(1);
        }
        return new RequestResult(StatEnum.ORGAN_SEARCH_SUCCESS,organizations);
    }

    /***
     * 加入组织
     * @param organizationId
     * @param token
     * @param userId
     * @return
     */
    public RequestResult<?> join(int organizationId,long token,int userId){
        if (organizationDao.isJoin(organizationId,userId) > 0) throw new OrganizationException("用户已加入该组织");
        Organization organization =organizationDao.getById(organizationId);
        if (organization==null) throw new OrganizationException("组织不存在");
        if (organization.getToken()!=token)throw new OrganizationException("口令错误");
        organizationDao.joinOrganization(organizationId,userId);
        return new RequestResult(StatEnum.ORGAN_JOIN_SUCCESS);
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
        }
        return new RequestResult(StatEnum.ORGAN_SEARCH_SUCCESS,organizations);
    }
}
