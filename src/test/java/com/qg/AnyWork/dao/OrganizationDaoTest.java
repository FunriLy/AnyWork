package com.qg.AnyWork.dao;

import com.qg.AnyWork.model.Organization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by logan on 2017/7/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationDaoTest {
    @Resource
    OrganizationDao organizationDao;

    @Test
    public void getByKeyWords() throws Exception {
        List<Organization> organizations = organizationDao.getByKeyWords("%软件%");
        int i = organizations.size();
//        while (i!=0){
//            System.out.println(organizations.get(--i));
//        }
        System.out.println(i);
        System.out.println(organizations.get(1));
    }

    @Test
    public void getByUserId() throws Exception {
        List<Organization> organizations = organizationDao.getByUserId(1);
        int i = organizations.size();
        while (i!=0){
            System.out.println(organizations.get(--i));
        }
    }

    @Test
    public void getById() throws Exception {
        System.out.println(organizationDao.getById(1));
    }

    @Test
    public void joinOrganization() throws Exception {
        System.out.println(organizationDao.joinOrganization(1,5));
    }

}