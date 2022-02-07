package com.spring.lambda.bean;

public class Elvis2 {
     private static final Elvis2 INSTANCE = new Elvis2();

     public static Elvis2 getInstance(){
         return INSTANCE;
     }

     private Elvis2(){

     }
}
