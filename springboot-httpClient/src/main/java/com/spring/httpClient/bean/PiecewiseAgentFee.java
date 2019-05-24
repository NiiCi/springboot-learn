package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PiecewiseAgentFee {
    /**
     * description : 配送费¥0.5
     * extra_fee : -2
     * is_extra : true
     * no_subsidy_fee : ¥3
     * rules : [{"fee":0.5,"price":20}]
     * tips : 配送费¥0.5
     */

    public String description;
    public int extraFee;
    public boolean isExtra;
    public String noSubsidyFee;
    public String tips;
    public List<Rules> rules;
}
