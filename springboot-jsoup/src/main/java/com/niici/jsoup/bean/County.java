package com.niici.jsoup.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class County {
    private long cityId;
    private long countyId;
    private String countyName;
}
