package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FoldingRestaurants {
    /**
     * distance : 5612
     * id : E12161281331343552285
     * name : 叉子汤勺健康沙拉轻食(西溪谷店)
     * order_lead_time : 39
     * scheme : https://h5.ele.me/shop/#id=E12161281331343552285
     * type : 0
     */

    public int distance;
    public String id;
    public String name;
    public int orderLeadTime;
    public String scheme;
    public int type;
}
