package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.OrganizationException;
import com.qg.AnyWork.model.*;
import com.qg.AnyWork.service.OrganizationService;
import com.qg.AnyWork.service.TestService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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

    @Autowired
    private TestService testService;

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

    /***
     * 退出组织接口
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public RequestResult leave(HttpServletRequest request,@RequestBody Map map){
//        User user = (User) request.getSession().getAttribute("user");
        try {
            User user = new User(); user.setUserId(1);
            if (user == null)  return new RequestResult(StatEnum.ORGAN_SEARCH_FAIL);
            int organizationId = (int) map.get("organizationId");
            return organizationService.exitOrganization(organizationId,user.getUserId());
        }catch (OrganizationException e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }
    }

    /***
     * 创建组织
     * @param file
     * @param organizationName
     * @param description
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RequestResult<List<User>> addOrganization(@RequestParam(value = "file", required = false) MultipartFile file,
                                                   @RequestParam(value = "organizationName", required = false) String organizationName,
                                                   @RequestParam(value = "description", required = false) String description,
                                                   HttpServletRequest request) throws IOException {
        if (description==null || description.equals("")) return new RequestResult(0,"描述为空");
        if (organizationName==null || organizationName.equals("")) return new RequestResult(0,"组织名为空");
//        User user = (User)request.getSession().getAttribute("user");
//        if (user.getMark()==0)return new RequestResult(0,"没有权限");
        User user = new User(); user.setUserId(0); user.setMark(1);
        if (user.getMark()==0) return new RequestResult(0,"无此权限");

        Organization o = new Organization();
        o.setTeacherName(user.getUserName());
        o.setTeacherId(user.getUserId());
        o.setDescription(description);
        o.setOrganizationName(organizationName);
        organizationService.addOrganization(o);

        if (file!=null && !file.isEmpty()){
            String filename = file.getOriginalFilename();
            if (!(filename.endsWith(".jpg") || filename.endsWith(".JPG") || filename.endsWith(".png") || filename.endsWith(".PNG")))
            return new RequestResult(0,"上传的头像不合法");

            String photoName = o.getOrganizationId() + ".jpg";
            FileUtils.copyInputStreamToFile(file.getInputStream(),
                    new File(request.getServletContext().getRealPath("/picture/organization"), photoName));
        }
        return new RequestResult(1,"创建成功",o);
    }


    /***
     * 修改组织信息
     * @param file
     * @param organizationName
     * @param organizationId
     * @param description
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/alter", method = RequestMethod.POST)
    public RequestResult<List<User>> alter(@RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam(value = "organizationId", required = true) int organizationId,
                                                     @RequestParam(value = "organizationName", required = false) String organizationName,
                                                     @RequestParam(value = "description", required = false) String description,
                                                     HttpServletRequest request) throws IOException {
//        User user = (User)request.getSession().getAttribute("user");
//        if (user.getMark()==0)return new RequestResult(0,"没有权限");
        User user = new User(); user.setUserId(0); user.setMark(1);
        if (user.getMark()==0) return new RequestResult(0,"无此权限");

        Organization o = organizationService.getById(organizationId);
        if (o.getTeacherId()!=user.getUserId()) return new RequestResult(0,"您不是此组织的创建人");

        if ((organizationName!=null && !organizationName.equals("")) || (description!=null && !description.equals(""))) {
            if ((description!=null && !description.equals("")))
                o.setDescription(description);
            if ((organizationName!=null && !organizationName.equals("")) )
                o.setOrganizationName(organizationName);

            organizationService.alterOrganization(o);
        }

        if (file!=null && !file.isEmpty()){
            String filename = file.getOriginalFilename();
            if (!(filename.endsWith(".jpg") || filename.endsWith(".JPG") || filename.endsWith(".png") || filename.endsWith(".PNG")))
                return new RequestResult(0,"上传的头像不合法");

            String photoName = organizationId + ".jpg";
            FileUtils.copyInputStreamToFile(file.getInputStream(),
                    new File(request.getServletContext().getRealPath("/picture/organization"), photoName));
        }

        return new RequestResult(1,"修改成功",o);
    }

    /***
     * 删除组织
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RequestResult delete(HttpServletRequest request,@RequestBody Map map){
        int organizationId = (int) map.get("organizationId");
//        User user = (User)request.getSession().getAttribute("user");
        User user = new User(); user.setUserId(0); user.setMark(1);
        if (user.getMark()==0) return new RequestResult(0,"无此权限");
        try {
            return organizationService.deleteOrganization(organizationId, user.getUserId());
        }catch (OrganizationException e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }
    }

    /***
     * 查看我创建过的组织
     * @param request
     * @return
     */
    @RequestMapping(value = "/myOrganization", method = RequestMethod.POST)
    public RequestResult<List<Organization>> Organization(HttpServletRequest request){
        //        User user = (User)request.getSession().getAttribute("user");
        User user = new User(); user.setUserId(0);  user.setMark(1);
        if (user.getMark()==0) return new RequestResult(0,"无此权限");
        return organizationService.getMyOrganization(user.getUserId());
    }


    /***
     * 获得该组织下的成员
     * @param map
     * @return
     */
    @RequestMapping(value = "/student",method = RequestMethod.POST)
    public RequestResult<List<User>> getStudentOfOrganization(@RequestBody Map map){
        int organizationId = (int) map.get("organizationId");
        try {
            return organizationService.getOrganizationPeople(organizationId);
        }catch (OrganizationException e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }

    }

    /***
     * 获取试卷详情
     * @param map
     * @return
     */
    @RequestMapping(value = "/studentTestDetail", method = RequestMethod.POST)
    public RequestResult<StudentTestResult> getDetail(@RequestBody Map map){
        int testpaperId = (int) map.get("testpaperId");
        int userId = (int) map.get("userId");
        return testService.getDetail(testpaperId,userId);
    }

    /***
     * 获取组织下某学生的考试列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/studentTest", method = RequestMethod.POST)
    public RequestResult<List<CheckResult>> studentTest(@RequestBody Map map){
        int userId = (int) map.get("userId");
        int organizationId = (int) map.get("organizationId");
        return testService.getCheckResultByUser(organizationId,userId);
    }
}
