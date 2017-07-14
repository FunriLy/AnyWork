package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.question.ExcelReadException;
import com.qg.AnyWork.exception.question.RedisNotExitException;
import com.qg.AnyWork.model.Question;
import com.qg.AnyWork.model.Testpaper;
import com.qg.AnyWork.model.User;
import com.qg.AnyWork.service.QuestionService;
import com.qg.AnyWork.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
                if (filename.endsWith(".xlsx")){
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

    @RequestMapping(value = "/{organizationId}/release", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<Integer> releaseTestpaper(HttpServletRequest request, @RequestBody Map map, @PathVariable int organizationId){
        User user = (User) request.getSession().getAttribute("user");
        try {
            //没有权限处理
            if (user.getMark() != 1){
                return new RequestResult<Integer>(StatEnum.NOT_HAVE_POWER, 0);
            }

            Testpaper testpaper = new Testpaper();
            testpaper.setAuthorId(user.getUserId());
            testpaper.setOrganizationId(organizationId);
            testpaper.setTestpaperTitle((String) map.get("testpaperTitle"));
            testpaper.setChapterId((int) map.get("chapterId"));
            testpaper.setCreateTime((Date) map.get("createTime"));
            testpaper.setEndingTime((Date) map.get("endingTime"));
            testpaper.setTestpaperType((int) map.get("testpaperType"));
            //将试卷插入数据库
            testService.addTestpaper(testpaper);
            int testpaperId = testpaper.getTestpaperId();

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
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, 0);
        }
    }
}
