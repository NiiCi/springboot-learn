package com.spring.security.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCodeProperties {
   private int length;
   private int height;
   private Set<String> url;
}
