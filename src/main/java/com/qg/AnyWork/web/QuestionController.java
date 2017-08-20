package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.question.ExcelReadException;
import com.qg.AnyWork.exception.question.RedisNotExitException;
import com.qg.AnyWork.exception.testpaper.NotPowerException;
import com.qg.AnyWork.exception.testpaper.TestpaperIsNoExit;
import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.model.Testpaper;
import com.qg.AnyWork.model.User;
import com.qg.AnyWork.service.QuestionService;
import com.qg.AnyWork.service.TestService;
import com.qg.AnyWork.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by FunriLy on 2017/7/13.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/quest")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestService testService;

    /**
     * 用户上传并预览试卷/练习
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public RequestResult<List<Question>> excelUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        User user = (User) request.getSession().getAttribute("user");
        try {
            if (null != file && !file.isEmpty()){
                String filename = file.getOriginalFilename();
                if (filename.endsWith(".xlsx") || filename.endsWith(".xls")){
//                    //文件上传
//                    FileUtils.copyInputStreamToFile(file.getInputStream(),
//                            new File(request.getServletContext().getRealPath("/excel"), user.getUserId() +".xlsx"));
//
//
//                    //读取文件
//                    String path = request.getServletContext().getRealPath("/excel"+"/"+user.getUserId()+".xlsx");
//                    System.out.println(path);
                    RequestResult<List<Question>> result = questionService.addQuestionList(file.getInputStream(), user.getUserId());
                    return result;
                }
            }
            return new RequestResult<List<Question>>(StatEnum.FILE_UPLOAD_FAIL, null);
        } catch (IOException e){
            logger.warn("用户上传Excel文件发生异常", e);
            return new RequestResult<List<Question>>(StatEnum.FILE_UPLOAD_FAIL, null);
        } catch (ExcelReadException e){
            logger.warn("用户上传Excel格式出错", e);
            return new RequestResult<List<Question>>(StatEnum.FILE_READ_FAIL, null);
        } catch (Exception e){
            logger.warn("未知异常: ", e);
            return new RequestResult<List<Question>>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    /**
     * 用户发布已经上传的试卷
     * @param request
     * @param map
     * @param organizationId
     * @return
     */
    @RequestMapping(value = "/{organizationId}/release", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<Integer> releaseTestpaper(HttpServletRequest request, @RequestBody Map map, @PathVariable int organizationId){
        User user = (User) request.getSession().getAttribute("user");
        int testpaperId = 0;
        try {
            //没有权限处理
            // TODO: 2017/7/17 可以检查一下权限
            if (user.getMark() != 1){
                return new RequestResult<Integer>(StatEnum.NOT_HAVE_POWER, 0);
            }

            Testpaper testpaper = new Testpaper();
            testpaper.setAuthorId(user.getUserId());
            testpaper.setOrganizationId(organizationId);
            testpaper.setTestpaperTitle((String) map.get("testpaperTitle"));
            testpaper.setChapterId((int) map.get("chapterId"));
            testpaper.setCreateTime(DateUtil.longToDate((Long) map.get("createTime")));
            testpaper.setEndingTime(DateUtil.longToDate((Long) map.get("endingTime")));
            testpaper.setTestpaperType((int) map.get("testpaperType"));
            //将试卷插入数据库
            testService.addTestpaper(testpaper);
            testpaperId = testpaper.getTestpaperId();   //获得试卷ID

            int socre = questionService.addTestpaper(user.getUserId(), testpaperId);    //插入数据库并获得总分
            if (testService.updateTextpaper(testpaperId, socre)) {  //更新总分
                return new RequestResult<Integer>(StatEnum.TEST_RELEASE_SUCESS, testpaperId);
            }
            return new RequestResult<Integer>(StatEnum.TEST_RELEASE_FAIL, 0);
        } catch (RedisNotExitException e){
            logger.warn("用户未上传Excel文件");
            return new RequestResult<Integer>(StatEnum.FILE_NOT_EXIT, 0);
        } catch (NullPointerException e){
            logger.warn("参数不正确");
            return new RequestResult<Integer>(StatEnum.ERROR_PARAM, 0);
        } catch (Exception e){
            logger.warn("未知异常: ", e);
            if (testpaperId != 0) {
                questionService.deleteTestpaper(testpaperId);  //删除错误插入
            }
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, 0);
        }
    }

    /**
     * 在线发布试卷
     * @param request
     * @param testpaper
     * @param organizationId
     * @return
     */
    @RequestMapping(value = "/{organizationId}/submit", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<Integer> submitTestpaper(HttpServletRequest request,  @RequestBody Testpaper testpaper,
                                                  @PathVariable int organizationId){
        User user = (User) request.getSession().getAttribute("user");
        int testpaperId = 0;
        try {
            if (user.getMark() != 1){
                return new RequestResult<Integer>(StatEnum.NOT_HAVE_POWER, 0);
            }

            testpaper.setOrganizationId(organizationId);
            testpaper.setAuthorId(user.getUserId());
            //将试卷插入数据库
            testService.addTestpaper(testpaper);
            testpaperId = testpaper.getTestpaperId();

            int socre = questionService.addTestpaper(user.getUserId(), testpaperId, testpaper.getQuestions());
            if (testService.updateTextpaper(testpaperId, socre)) {  //更新总分
                return new RequestResult<Integer>(StatEnum.TEST_RELEASE_SUCESS, testpaperId);
            }
            return new RequestResult<Integer>(StatEnum.TEST_RELEASE_FAIL, 0);
        } catch (NullPointerException e){
            logger.warn("参数不正确");
            return new RequestResult<Integer>(StatEnum.ERROR_PARAM, 0);
        } catch (Exception e){
            logger.warn("未知异常: ", e);
            if (testpaperId != 0) {
                questionService.deleteTestpaper(testpaperId);   //删除错误插入
            }
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, 0);
        }

    }

    /**
     * 教师导出Excel
     * @param request
     * @param response
     * @param organizationId
     * @param testpaperId
     * @return
     */
    @RequestMapping(value = "/{organizationId}/export/{testpaperId}", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<Integer> exportExcel(HttpServletRequest request, HttpServletResponse response,
                                              @PathVariable("organizationId") int organizationId, @PathVariable int testpaperId){
        try {
            User user =  (User) request.getSession().getAttribute("user");
            if (user == null){
                return new RequestResult<Integer>(StatEnum.USER_NOT_LOGIN, 0);
            }
            Testpaper testpaper = questionService.findTestpaperById(testpaperId);
            //权限验证
            if (user.getMark() != 1 || user.getUserId() != testpaper.getAuthorId()){
                throw new NotPowerException("没有相应的处理权限！");
            }
            OutputStream out = new FileOutputStream(request.getServletContext().getRealPath("/excel")+"/"+user.getUserId()+".xls");
            questionService.exportExcel(testpaperId, user.getUserId(), out);
            return new RequestResult<Integer>(StatEnum.FILE_EXPORT_SUCCESS, testpaperId);
        } catch (NotPowerException e){
            //没有相应的权限
            logger.warn("非法用户调用老师权限！");
            return new RequestResult<Integer>(StatEnum.NOT_HAVE_POWER, 0);
        } catch (ExcelReadException e) {
            //Excel 导出失败
            logger.warn("Excel 导出失败！");
            return new RequestResult<Integer>(StatEnum.FILE_EXPORT_FAIL, 0);
        } catch (Exception e) {
            logger.warn("未知异常: ", e);
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, 0);
        }
    }


    /**
     * 老师删除试卷
     * @param request
     * @param testpaperId
     * @return
     */
    @RequestMapping(value = "/{testpaperId}/delete", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<?> delete(HttpServletRequest request, @PathVariable("testpaperId") int testpaperId){
        try {
            User user = (User) request.getSession().getAttribute("user");
            //用户未登录
            if (user == null){
                return new RequestResult<Object>(StatEnum.USER_NOT_LOGIN, null);
            }
            deleteTestpaper(testpaperId, user);
            return new RequestResult<Object>(StatEnum.DELETE_TEST_SUCCESS);
        } catch (TestpaperIsNoExit te) {
            logger.warn("不存在的试卷/练习！");
            return new RequestResult<Object>(StatEnum.TEST_IS_NOT_EXIT, null);
        } catch (NotPowerException ne) {
            //没有相应的权限
            logger.warn("非法用户调用老师权限！");
            return new RequestResult<Object>(StatEnum.NOT_HAVE_POWER, null);
        } catch (Exception e) {
            logger.warn("未知异常: ", e);
            return new RequestResult<Object>(StatEnum.DEFAULT_WRONG, null);
        }
}

    /**
     * 当发生操作失败时，需要清除试卷和题目
     * @param testpaperId 试卷Id
     */
    private void deleteTestpaper (int  testpaperId, User user) throws Exception{
        Testpaper testpaper = questionService.findTestpaperById(testpaperId);
        //权限验证
        if (user.getMark() != 1 || user.getUserId() != testpaper.getAuthorId()){
            throw new NotPowerException("没有相应的处理权限！");
        }
        questionService.deleteTestpaper(testpaperId);
    }
}
