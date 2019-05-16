package com.spring.lambda.test;

import com.spring.lambda.User;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class test {
    /**
     *  什么是 惰性求值，什么是 及早求值
     *  1. 惰性求值 返回值类型为 Stream 都是 惰性求值方法，惰性求值方法 不是一个 终止操作流的方法
     *  2. 及早求值 返回值类型为 void 或者 其他类型，及早求值方法 是一个 终止操作流的方法 如 下面的 count() 方法
     */
    @Test
    public void testStream(){
        // 通过 惰性求值 filter , 求出 满足 大于2的 个数
        List<Long> intList = Arrays.asList(1L,2L,3L);
        long count = intList.stream().filter(i -> {
            System.out.println(i);
            return i >= 2;
        }).count();

        // 通过 惰性求值 of 方法从 stream 流中 生成一个 list
        List<String> stringList = Stream.of("test1","test2","test3").collect(Collectors.toList());
    }

    /**
     *  stream 中的 map
     *  map 的作用 是将一种类型的值 转换为另一种类型
     */
    @Test
    public void mapTest(){
        // 将 long 转为 string 类型 并加上 test 拼接 遍历打印
       List<Long> longList = Stream.of(1L,2L,3L).collect(Collectors.toList());
       longList.stream().map(l -> "test ----: " + String.valueOf(l)).collect(Collectors.toList()).forEach(System.out::println);
    }

    /**
     * stream 中的 flatMap
     * flagMap 的作用是用于合并两个集合
     */
    @Test
    public void FlatMapTest(){
         // 调用 stream 方法,将 list 转为 Stream 对象,之后有 flapMap 方法来实现
         // flapMap 方法 本身不能实现去重
         Stream.of(Arrays.asList(1L,2L,3L),Arrays.asList(3L,5L,6L))
                .flatMap(number -> number.stream())
                 //调用 distinct 方法 实现去重
                 .distinct()
                .collect(Collectors.toList())
                .forEach(System.out::println);

    }

    /**
     * stream 中的 max 和 min
     * max 和 min 用于求 以元素的某个属性进行排序 所得到的 最大 或者 最小元素
     */
    @Test
    public void MaxAndMinTest(){
        List<User> userList = Stream.of(
                new User(1,18,"user1"),
                new User(2,19,"user2"),
                new User(3,20,"User3")).collect(Collectors.toList());
        // Comparator 对象是 jdk8 提供的一个对象,其中 comparing 是一个静态方法,可以方便的实现一个比较器
        // max 和 min 方法 返回的 都是一个 optional 对象
        // optional 对象代表了 一个可能存在 也可能不存在的值,调用 get 方法 获取其中的值
        User user1 = userList.stream().max(Comparator.comparing(User::getAge)).get();
        User user2 = userList.stream().min(Comparator.comparing(user -> user.getAge())).get();
        log.info(user1.getName());
        log.info(user2.getName());
    }

    /**
     * stream 中的 reduce
     * reduce 可以实现从一组值中生成一个值, 常用的 count,max,min 都是 reduce 操作
     */
    @Test
    public void reduceTest(){
        List<Long> longList = Stream.of(1L,2L,3L).collect(Collectors.toList());

        //acc 代表累加器, element 代表当前元素
        //lambda 表达式的返回值是 最新的 acc ,是 上一轮 acc 和 element 处理后的结果
        // reduce 的参数接口类型 为 BinaryOperator 为 (x,y) -> x+y 类型的形式
        long sum =  longList.stream().reduce((acc,element) -> acc + element).get();
        System.out.println(sum);

        // 入参为两个参数时, 第一个参数 代表初始值
        long sum2 =  longList.stream().reduce(0L,(acc,element) -> acc + element);
        System.out.println(sum2);
    }


}
