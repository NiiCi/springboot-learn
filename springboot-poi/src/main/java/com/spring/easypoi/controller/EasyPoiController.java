package com.spring.easypoi.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.spring.easypoi.bean.User;
import com.spring.easypoi.service.UserService;
import com.spring.easypoi.utils.EasyPoiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class EasyPoiController {

    @Autowired
    private UserService userService;

    /**
     * 导出excel
     * @param response
     * @return
     */
    @GetMapping("/export")
    public ResponseEntity<Void> export(HttpServletResponse response){
        List<User> userList = userService.selectAllUser();
        userList.forEach(System.out::println);
        if (CollectionUtils.isEmpty(userList)){
            return ResponseEntity.notFound().build();
        }
        // 通过工具类导出
        EasyPoiUtil.exportExcel(userList,"测试导出用户列表","sheet",User.class,"userTest.xls",response);
        return ResponseEntity.ok().build();
    }

    /**
     * 导入 excel 保存数据到数据库
     * @param file
     * @return
     */
    @PostMapping("/importExcel")
    public ResponseEntity<Void> importExcel(@RequestParam("file") MultipartFile file){
        ImportParams importParams = new ImportParams();
        // 设置标题行数 , 头行数
        List<User> userList = EasyPoiUtil.importExcel(file,1,1,User.class);
        userList.forEach(System.out::println);
        userService.saveUserList(userList);
        return ResponseEntity.ok().build();
    }
}
