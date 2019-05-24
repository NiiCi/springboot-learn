package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeliveryMode {
    /**
     * border :
     * color : 2395FF
     * gradient : {"rgb_from":"00AAFF","rgb_to":"0085FF"}
     * icon_hash : b9b45d2f6ff0dbeef3a78ef0e4e90abapng
     * id : 1
     * is_solid : true
     * text : 蜂鸟专送
     * text_color : FFFFFF
     */

    public String border;
    public String color;
    public Gradient gradient;
    public String iconHash;
    public int id;
    public boolean isSolid;
    public String text;
    public String textColor;
}
