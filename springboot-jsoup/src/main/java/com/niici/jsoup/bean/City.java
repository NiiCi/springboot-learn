package com.niici.jsoup.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private long provinceId;
    private long cityId;
    private String cityName;
}
