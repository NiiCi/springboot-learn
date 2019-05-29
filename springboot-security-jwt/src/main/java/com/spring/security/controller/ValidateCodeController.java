package com.spring.security.controller;

import com.spring.security.bean.ImageCode;
import com.spring.security.dao.ValidateCodeGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 3.映射获取验证码的请求
 */
@RestController
@Log4j2
public class ValidateCodeController {
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    /**
     * 引入 session
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response){
        ImageCode imageCode = imageCodeGenerator.createCode(new ServletWebRequest(request));
        // 将随机数 放到session 中
        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,imageCode);
        log.info(imageCode.getCode());
        // 写给response 响应
        try {
            ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
}
