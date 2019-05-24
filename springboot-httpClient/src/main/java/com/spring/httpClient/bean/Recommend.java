package com.spring.httpClient.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Recommend {
    /**
     * image_hash : 731111f3f9379e772eedf4855beae8a1jpeg
     * is_ad : false
     * track : {"rankType":"27"}
     */

    public String imageHash;
    public boolean isAd;
    public String track;
}
