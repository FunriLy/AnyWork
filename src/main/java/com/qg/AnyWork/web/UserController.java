package com.qg.AnyWork.web;

import com.qg.AnyWork.dto.RequestResult;
import com.qg.AnyWork.enums.StatEnum;
import com.qg.AnyWork.exception.ValcodeWrongException;
import com.qg.AnyWork.exception.user.*;
import com.qg.AnyWork.model.User;
import com.qg.AnyWork.service.UserService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     *
     * @param request
     * @param map
     * @param file
     * @return 用户id
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public RequestResult<Integer> register(HttpServletRequest request,
                                           @RequestBody MultiValueMap<String, String> map,
                                           @RequestParam("file") MultipartFile file){
        User user = new User();
        user.setEmail(map.get("email").get(0));
        user.setPassword(map.get("password").get(0));
        user.setUserName(map.get("userName").get(0));
        user.setPhone(map.get("phone").get(0));
        try {
            user.setMark(Integer.valueOf(map.get("mark").get(0)));
        } catch (Exception e){
            user.setMark(0);
        }
        //注册用户
        try {
            RequestResult<Integer> result = userService.register(user);
            //上传图片
            if (null != file && !file.isEmpty()){
                String filename = file.getOriginalFilename();
                if (filename.endsWith(".jpg") || filename.endsWith(".JPG") || filename.endsWith(".png") || filename.endsWith(".PNG")) {
                    logger.info("用户上传头像：" + result.getData());
                    //文件上传
                    FileUtils.copyInputStreamToFile(file.getInputStream(),
                            new File(request.getServletContext().getRealPath("/picture"), result.getData()+".jpg"));
                } else {
                    return new RequestResult<Integer>(StatEnum.FILE_FORMAT_ERROR, 0);
                }
                return result;
            } else {
                return null;
            }
        } catch (FormatterFaultException e){
            logger.warn(e.getMessage(), e);
            return new RequestResult<Integer>(StatEnum.REGISTER_FAMMTER_FAULT, 0);
        } catch (EmptyUserException e){
            logger.warn(e.getMessage(), e);
            return new RequestResult<Integer>(StatEnum.REGISTER_EMPTY_USER, 0);
        } catch (IOException e) {
            logger.error("用户上传图片发送异常！", e);
            return new RequestResult<Integer>(StatEnum.FILE_UPLOAD_FAIL, 0);
        } catch (Exception e){
            logger.warn("未知异常：", e);
            return new RequestResult<Integer>(StatEnum.DEFAULT_WRONG, 0);
        }
    }

    /**
     * 用户登录
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "'/login", method = RequestMethod.POST)
    @ResponseBody
    public RequestResult<User> login(HttpServletRequest request, @RequestBody MultiValueMap<String, String> map) {
        String email = map.get("email").get(0);
        String password = map.get("password").get(0);
        String valcode = map.get("valcode").get(0);
        //检查字段
        if (email == null || password == null || valcode == null)
            return new RequestResult<User>(StatEnum.ERROR_PARAM, null);
        try {
            //验证码
            verify(request, valcode);
            RequestResult<User> result = userService.login(email, password);
            //存入Session
            User user = result.getData();
            request.getSession().setAttribute("user", user);
            return result;
        } catch (ValcodeWrongException e){
            logger.warn("用户登录验证码错误");
            return new RequestResult<User>(StatEnum.VALCODE_WRONG, null);
        } catch (FormatterFaultException e){
            logger.warn("空用户对象");
            return new RequestResult<User>(StatEnum.REGISTER_EMPTY_USER, null);
        } catch (UserNotExitException e){
            logger.warn("不存在的用户");
            return new RequestResult<User>(StatEnum.LOGIN_NOT_EXIT_USER, null);
        } catch (UserLoginFailException e){
            logger.warn("错误的用户名或密码");
            return new RequestResult<User>(StatEnum.LOGIN_USER_MISMATCH, null);
        } catch (Exception e){
            logger.warn("未知异常: ", e);
            return new RequestResult<User>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    public RequestResult<User> passwordChange(HttpServletRequest request, @RequestBody MultiValueMap<String, String> map){
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null)
                throw new UserNotLogin("用户还未登录");
            String newPassword = map.get("password").get(0);
            User rUser = new User();
            rUser.setUserId(user.getUserId());
            rUser.setPassword(newPassword);

            RequestResult<User> result = userService.updateUser(rUser);
            return result;
        } catch (UserNotLogin e){
            logger.warn("用户还未登录");
            return new RequestResult<User>(null);
        } catch (FormatterFaultException e){
            logger.warn("修改信息格式错误");
            return new RequestResult<User>(null);
        } catch (Exception e){
            logger.warn("未知异常: ", e);
            return new RequestResult<User>(StatEnum.DEFAULT_WRONG, null);
        }

    }


    /**
     * 验证码验证
     * @param request
     * @param valcode
     * @return
     */
    private boolean verify(HttpServletRequest request, String valcode){
        // TODO: 2017/7/10 预留验证码
        boolean flag = false;
        if(valcode.equals("0")) return true;
        String code = (String) request.getSession().getAttribute("valcode");
        if (code == null || !code.equalsIgnoreCase(valcode)) {
            throw new ValcodeWrongException("验证码错误");
        } else {
            flag = true;
            logger.info("用户验证码通过验证！");
        }
        return flag;
    }

    /**
     * 将用户信息存到cookie
     * @param response
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    private void setCookie(HttpServletResponse response, User user) throws UnsupportedEncodingException {
        Cookie userNameCookie = new Cookie("userName", URLEncoder.encode(user.getUserName(), "utf-8"));
        userNameCookie.setMaxAge(60*60*24);
        userNameCookie.setPath("/");
        response.addCookie(userNameCookie);
        Cookie emailCookie = new Cookie("email", user.getEmail());
        emailCookie.setMaxAge(60 * 60 * 24);
        emailCookie.setPath("/");
        response.addCookie(emailCookie);
        Cookie phoneCookie = new Cookie("phone", user.getPhone());
        phoneCookie.setMaxAge(60 * 60 * 24);
        phoneCookie.setPath("/");
        response.addCookie(phoneCookie);
    }

    /**
     * 清空cookie
     * @param request
     * @param response
     */
    private void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie: cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
