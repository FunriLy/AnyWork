package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.OrganizationException;
import com.qg.AnyWork.model.Chapter;
import com.qg.AnyWork.model.Organization;
import com.qg.AnyWork.model.User;
import com.qg.AnyWork.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by logan on 2017/7/11.
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    /***
     * 根据组织关键字模糊查找
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public RequestResult<List<Organization>> search(HttpServletRequest request, @RequestBody Map map){
        try {
//            User user = (User) request.getSession().getAttribute("user");
            User user = new User(); user.setUserId(1);
            String organizationName = (String) map.get("organizationName");
            return organizationService.search(organizationName,user.getUserId());
        } catch (OrganizationException e) {
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.ORGAN_SEARCH_FAIL);
        } catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.ORGAN_SEARCH_FAIL);
        }
    }

    /***
     * 加入组织
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public RequestResult<?> join(HttpServletRequest request, @RequestBody Map map){
        try {
//            User user = (User) request.getSession().getAttribute("user");
            User user = new User(); user.setUserId(1);
            String organizationId = (String) map.get("organizationId");
            String token = (String) map.get("token");
            return organizationService.join(Integer.parseInt(organizationId),Long.parseLong(token),user.getUserId());
        }catch (OrganizationException e) {
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        } catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.ORGAN_JOIN_FAIL);
        }
    }

    /***
     * 获取我的组织列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public RequestResult<List<Organization>> myOrganization(HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");

        User user = new User(); user.setUserId(1);
        if (user == null)  return new RequestResult(StatEnum.ORGAN_SEARCH_FAIL);
        return organizationService.searchByUserId(user.getUserId());
    }
}
