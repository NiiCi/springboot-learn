package com.spring.swagger.controller;

import com.spring.swagger.bean.User;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/user")
@Api("user 服务接口")
public class UserController {
    /**
     @Api: 修改整个类，描述Controller的作用
     @ApiOperation: 描述一个类的一个方法，或者说一个接口
     @ApiParam: 单个参数描述
     @ApiModel: 用对象来接受参数
     @ApiModelProperty: 用对象来接受参数时，描述对象的一个字段
     @ApiResponse: HTTP响应的其中一个描述
     @ApiResponses: HTTP响应的整体描述
     @ApiIgnore: 使用该注解忽略这个Api
     @ApiImplicitPa ram: 一个请求参数
     @ApiImplicitParams: 多个请求参数
     */

    @PostMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    /**
     * 定义参数说明
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", required = true, value = "用户名或者手机号", paramType = "String"),
            @ApiImplicitParam(name = "passWord", required = true, value = "登录密码", paramType = "String")
    })
    /**
     * 定义返回类型
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "操作成功", response = User.class),
            @ApiResponse(code = 500, message = "操作失败", response = Error.class)
    })
    public ResponseEntity<Void> getUserInfo(
            String userName,
            String passWord){
        return null;
    }
}
