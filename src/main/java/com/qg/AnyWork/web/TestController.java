package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.TestException;
import com.qg.AnyWork.model.*;
import com.qg.AnyWork.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by logan on 2017/7/12.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    /***
     * 获取试题集合
     * @param map
     * @return
     */
    @RequestMapping(value = "/testList", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> search(@RequestBody Map map){
        String organizationId = (String) map.get("organizationId");
        if(organizationId==null||organizationId.equals("")) return new RequestResult(StatEnum.REQUEST_ERROR);

        try {
            return testService.getTestList(Integer.parseInt(organizationId));
        } catch (TestException e) {
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }   catch (Exception e) {
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.GET_TEST_FAIL);
        }
    }

    /***
     * 获取练习集合
     * @param map
     * @return
     */
    @RequestMapping(value = "/practiceList", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> searchPractice(@RequestBody Map map){
        String organizationId = (String) map.get("organizationId");
        if(organizationId==null||organizationId.equals("")) return new RequestResult(StatEnum.REQUEST_ERROR);

        try {
            return testService.getPracticeList(Integer.parseInt(organizationId));
        } catch (TestException e) {
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }   catch (Exception e) {
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.GET_TEST_FAIL);
        }
    }

    /***
     * 获取问题集合
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public RequestResult<List<Question>> getQuestion(@RequestBody Map map){
        try {
            String testpaperId = (String) map.get("testpaperId");
            return testService.getQuestion(Integer.parseInt(testpaperId));
        } catch (TestException e) {
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        } catch (Exception e){
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.GET_TEST_FAIL);
        }
    }

    /***
     * 提交试卷
     * @param studentPaper
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public RequestResult<StudentTestResult> submit(@RequestBody StudentPaper studentPaper,HttpServletRequest request){
        if(studentPaper==null) return new RequestResult(StatEnum.REQUEST_ERROR);

        RequestResult<StudentTestResult> studentTestResult = null;

        try {
            studentTestResult = testService.submit(studentPaper);
            request.getSession().setAttribute("studentTestResult",studentTestResult.getData());
            return studentTestResult;
        } catch (TestException e) {
            logger.warn(e.getMessage());
            return new RequestResult(0,e.getMessage());
        }catch (Exception e){
           e.printStackTrace();
            logger.warn(e.getMessage());
            return new RequestResult(StatEnum.SUBMIT_TEST_FAIL);
        }


    }

    /***
     * 获取做题的详情信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RequestResult<StudentAnswerAnalysis> detail(@RequestBody Map map,HttpServletRequest request){
        String questionId = (String) map.get("questionId");
        if(questionId==null||questionId.equals("")) return new RequestResult(StatEnum.REQUEST_ERROR);

        StudentTestResult studentTestResult = (StudentTestResult) request.getSession().getAttribute("studentTestResult");
        List<StudentAnswerAnalysis> studentAnswerAnalysises =studentTestResult.getStudentAnswerAnalysis();

        for (StudentAnswerAnalysis s : studentAnswerAnalysises){
            if (Integer.parseInt(questionId) == s.getQuestion().getQuestionId())
                return new RequestResult<StudentAnswerAnalysis>(StatEnum.GET_TEST_SUCCESS,s);
        }
        return new RequestResult(StatEnum.GET_TEST_FAIL);
    }
}
