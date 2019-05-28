package com.spring.security.controller;

import com.spring.security.bean.ImageCode;
import com.spring.security.bean.User;
import com.spring.security.config.ImageConstants;
import com.spring.security.dao.UserDao;
import com.spring.security.util.ImageCodeUtil;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
//@PreAuthorize("hasRole('admin')")
/**
 * 在 @PreAuthorize 中我们可以利用内建的 SPEL 表达式：比如 'hasRole()' 来决定哪些用户有权访问。
 * 需注意的一点是 hasRole 表达式认为每个角色名字前都有一个前缀 'ROLE_'。所以这里的 'ADMIN' 其实在
 * 数据库中存储的是 'ROLE_ADMIN' 。这个 @PreAuthorize 可以修饰Controller也可修饰Controller中的方法。
 **/
public class UserController {
    /*@Autowired
    private UserDao userDao;*/

    /* @GetMapping
     public String getUsers() {
         return "1";
     }*/

  /*  @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Hello I'm niici, this is Login test";
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/info")
    @ResponseBody
    public Object getInfo(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 调用工具类,生成一个图形验证码对象
        ImageCode imageCode = ImageCodeUtil.createImageCode();
        SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        // 将 imageCode 存入session中
        sessionStrategy.setAttribute(new ServletWebRequest(request), ImageConstants.SESSION_KEY_CODE,imageCode);
        // 将BufferedImage 对象转换为一个图片格式的文件并写入到 response 响应流中
        ImageIO.write(imageCode.getImage(),ImageConstants.CODE_TYPE,response.getOutputStream());
    }*/
}
