package com.spring.security.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.RowSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCodeProperties {
   /**
    * 图形验证码 配置属性
    */
   private ImageCodeProperties image = new ImageCodeProperties();
}
