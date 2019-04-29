package com.spring.easypoi.bean;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(name = "用户ID",orderNum = "0")
    private Integer id;

    @Column(name = "username")
    @Excel(name = "用户名",orderNum = "1")
    private String username;

    @Column(name = "password")
    @Excel(name = "密码",orderNum = "2")
    private String password;

    @Column(name = "phone")
    @Excel(name="手机号",orderNum = "3")
    private String phone;

    @Column(name = "created")
    @Excel(name = "创建日期",orderNum = "4",importFormat = "yyyy-MM-dd HH:mm:ss",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date created;
}
