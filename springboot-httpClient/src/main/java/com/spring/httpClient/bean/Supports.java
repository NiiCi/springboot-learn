package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Supports {
    /**
     * border : 90dddddd
     * description : 该商户食品安全已由国泰产险承担，食品安全有保障
     * icon_color : 999999
     * icon_name : 保
     * id : 7
     * name : 食安保
     * text_color : 666666
     * two_characters_icon_name : 保险
     */

    public String border;
    public String description;
    public String iconColor;
    public String iconName;
    public int id;
    public String name;
    public String textColor;
    public String twoCharactersIconName;
}
