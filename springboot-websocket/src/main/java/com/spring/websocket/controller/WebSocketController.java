package com.spring.websocket.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
public class WebSocketController {
    @RequestMapping("/websocket/{name}")
    public String webSocket(@PathVariable("name") String name,Model model){
        try {
            log.info("跳转到websocket的页面上");
            model.addAttribute("username",name);
            return "websocket";
        } catch (Exception e) {
            log.error("跳转到websocket页面上发生异常,异常信息是: "+e.getMessage());
            return "error";
        }
    }
}
