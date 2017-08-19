package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.exception.TestException;
import com.qg.AnyWork.model.*;
import com.qg.AnyWork.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by logan on 2017/7/31.
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TestService testService;

    /***
     * 获取某套题组织内成员的完成情况
     * @param map
     * @return
     */
    @RequestMapping(value = "/lookTest", method = RequestMethod.POST)
    public RequestResult<List<CheckResult>> studentTest(@RequestBody Map map){
        int testpaperId = (int) map.get("testpaperId");
        int organizationId = (int) map.get("organizationId");
        return testService.getCheckResultByTestpaperId(organizationId,testpaperId);
    }


    /***
     * 老师改卷
     * @param teacherSubmit
     * @param request
     * @return
     */
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    public RequestResult<StudentTestResult> submit(@RequestBody TeacherSubmit teacherSubmit, HttpServletRequest request){
                User user = (User) request.getSession().getAttribute("user");
        if(user.getMark()==0) return new RequestResult(0,"权限不足");
        try {
            testService.updateStudentTest(teacherSubmit);
            return testService.getDetail(teacherSubmit.getTestpaperId(),teacherSubmit.getStudentId());
        }catch (TestException e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }
    }
}
