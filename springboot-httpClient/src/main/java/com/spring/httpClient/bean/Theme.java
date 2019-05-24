package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Theme {
    /**
     * default_color : 2395ff
     * header_style : 0
     * hongbao_style : 0
     * price_color : ff5339
     * third_tab_name : 商家
     * vanish_fields : []
     */

    public String defaultColor;
    public int headerStyle;
    public int hongbaoStyle;
    public String priceColor;
    public String thirdTabName;
    public List<?> vanishFields;
}
