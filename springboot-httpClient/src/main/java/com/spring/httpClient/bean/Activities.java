package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Activities {
    /**
     * attribute : {"80": {"1": 37.0, "0": 0}, "25": {"1": 13.0, "0": 0}, "50": {"1": 26.0, "0": 0}, "90": {"1": 46.0, "0": 0}, "150": {"1": 56.0, "0": 0}}
     * description : 满25减13，满50减26，满80减37，满90减46，满150减56
     * icon_color : f07373
     * icon_name : 减
     * id : 21647859114
     * is_exclusive_with_food_activity : true
     * name : 回家吃饭
     * tips : 满25减13，满50减26，满80减37，满90减46，满150减56
     * type : 102
     */

    public String attribute;
    public String description;
    public String iconColor;
    public String iconName;
    public long id;
    public boolean isExclusiveWithFoodActivity;
    public String name;
    public String tips;
    public int type;
}
