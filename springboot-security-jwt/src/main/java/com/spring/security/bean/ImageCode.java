package com.spring.security.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码实体类
 */
@Data
@NoArgsConstructor
public class ImageCode {

    /**
     * 验证码 BufferedImage 对象
     */
    private BufferedImage image;

    /**
     * 验证码内容
     */
    private String code;

    /**
     * 验证码过期时间
     */
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image,String code,LocalDateTime expireTime){
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }
    public ImageCode(BufferedImage image,String code,int expireIn){
        this.image=image;
        this.code=code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 判断是否过期
     * @return
     */
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
