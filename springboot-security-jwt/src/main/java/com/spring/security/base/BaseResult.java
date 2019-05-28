package com.spring.security.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回类类型POJO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult {
    /**
     * 返回内容 (JSON格式)
     */
    private Object conent;
}
