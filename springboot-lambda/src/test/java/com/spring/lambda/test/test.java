package com.spring.lambda.test;

import com.alibaba.fastjson.JSONObject;
import com.spring.lambda.bean.User;
import com.spring.lambda.dao.*;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        // 在并行流 froeach 时 不能保证元素是按顺序处理的
        // 可以调用 foreachOrdered 方法保证按顺序处理
        longList.parallelStream().forEachOrdered(System.out::println);
        //acc 代表累加器, element 代表当前元素
        //lambda 表达式的返回值是 最新的 acc ,是 上一轮 acc 和 element 处理后的结果
        // reduce 的参数接口类型 为 BinaryOperator 为 (x,y) -> x+y 类型的形式
        long sum =  longList.stream().reduce((acc,element) -> acc + element).get();
        System.out.println(sum);

        // 入参为两个参数时, 第一个参数 代表初始值
        long sum2 =  longList.stream().reduce(0L,(acc,element) -> acc + element);
    }

    // 高阶函数开始

    /**
     * 对基本类型做处理
     * summaryStatistics 方法 负责做出一些统计
     */
    @Test
    public void highTest(){
        Collection<String> stringList = Stream.of("1","2","3").collect(Collectors.toCollection(HashSet::new));
        // peek 方法 可以用来记录日志
        IntSummaryStatistics intSummaryStatistics = stringList.stream().mapToInt(Integer::parseInt).peek(logger ->{
            log.info("-------"+logger);
        }).summaryStatistics();
        log.info("getMax ---: "+intSummaryStatistics.getMax());
        log.info("getMin ---: "+intSummaryStatistics.getMin());
        log.info("getAverage ---: "+intSummaryStatistics.getAverage());
        log.info("getCount ---: "+intSummaryStatistics.getCount());
        log.info("getSum ---: "+intSummaryStatistics.getSum());
    }

    /**
     * 默认方法
     * 由 default 方法修饰,当子类或者实现类没有重写该方法时,调用的为父接口的方法
     * 当有实现类 重写了默认方法时,则优先调用实现类中的默认方法
     */
    @Test
    public void defaultMethodTest(){
        DefalutParent parent = new DefalutParentImpl();
        parent.welcome();

        DefaultChild child = new DefaultChildImpl();
        child.welcome();
    }

    /**
     * 多重继承
     * 当接口继承的两个接口中包含签名相同的情况,应该继承哪一个方法?
     * 在实现类中使用 接口.super.method 来指定具体实现哪个方法
     * 这里使用了 增强 super 的用法,指明使用 哪个接口中的默认方法
     *
     */
    @Test
    public void multiExtendsTest(){
        MusicalCarriage musicalCarriage = new MusicalCarriage();
        log.info(musicalCarriage.rock());
    }

    /**
     *  Optional 对象 是一个 用来 代替 null值的 对象,来规避空指针异常,如 user.getUsername() 可能会出现空指针
     *  通常情况下,Optional 对象都是与 stream 流一起进行 链式操作,因为 stream中的一些方法返回的是 Optional 类型的对象
     *  如果 是判断 值是否为空 并进行转换,则做不到
     *  通过 of 方法 来创建出一个Optional对象,此时 这个对象相当于值的容器
     */
    @Test
    public void optionalTest(){
        Optional<String> a = Optional.of("a");
        // 创建一个空的optional对象
        Optional emptyOptional = Optional.empty();
        // 将 空值 转换为一个 Optional 对象
        Optional alseEmpty = Optional.ofNullable("1");
        // 断言
        Assert.assertEquals("a",a.get());
        // isPresent 方法用于判断 optional 对象 是否不为空
        Assert.assertFalse(emptyOptional.isPresent());

        Assert.assertTrue(alseEmpty.isPresent());
        // ifPresent 用于判断值是否为空,并且执行一个lambda表达式,接口类型为 Consumer ,即 void 没有返回值
        alseEmpty.ifPresent(u-> log.info(a.get()));
        alseEmpty.isPresent();
        // orElse 表示在 optional 对象为空,给 optional 对象一个默认值
        Assert.assertEquals("b",emptyOptional.orElse("b"));

        // orElseGet 在 optional 对象为空时,进行赋值 接口类型为 Supplier
        Assert.assertEquals("c",emptyOptional.orElseGet(()->{return "c";}));
    }

    /**
     * 数据分块
     * partitioningBy 收集器,将流 分解为两个集合
     */
    @Test
    public void partitioningByTest(){
        List<User> userList = Stream.of(
                new User(1,18,"user1"),
                new User(2,19,"user2"),
                new User(3,20,"User3")).collect(Collectors.toList());
        // 将 name 不是 user2 的 用户,放入 true 集合 ,其他的放入 false 集合
        Map<Boolean,List<User>> newList = userList.stream().collect(Collectors.partitioningBy(user -> {return ("user2").equals(user.getName());}));
        log.info(JSONObject.toJSONString(newList.get(true)));
        log.info(JSONObject.toJSONString(newList.get(false)));
    }

    /**
     * 数据分组
     * partitioningBy 收集器,将流 分解为两个集合
     */
    @Test
    public void groupingByTest(){
        List<User> userList = Stream.of(
                new User(1,18,"user1"),
                new User(2,19,"user2"),
                new User(3,20,"user3"),
                new User(3,20,"user3")).collect(Collectors.toList());
        // 根据 name 对 userList 进行数据分组 Map 中的 key 为 GroupingBy 中的 数据的类型
        Map<String,List<User>> newList = userList.stream().collect(Collectors.groupingBy(User::getName));
        log.info(JSONObject.toJSONString(newList.get("user1")));
        log.info(JSONObject.toJSONString(newList.get("user2")));
        log.info(JSONObject.toJSONString(newList.get("user3")));

        // 根据 name 对 userList 进行数据分组, 并统计 每个分组中的元素个数
        Map<String,Long> numList = userList.stream().collect(Collectors.groupingBy(User::getName,Collectors.counting()));
        log.info(JSONObject.toJSONString(numList.get("user1")));
        log.info(JSONObject.toJSONString(numList.get("user2")));
        log.info(JSONObject.toJSONString(numList.get("user3")));

        // 根据 name 对 userList 进行数据分组,并统计出 每个分组中的用户名
        Map<String,List<String>> nameList = userList.stream().collect(Collectors.groupingBy(User::getName,Collectors.mapping(User::getName,Collectors.toList())));
        log.info(JSONObject.toJSONString(nameList.get("user1")));
        log.info(JSONObject.toJSONString(nameList.get("user2")));
        log.info(JSONObject.toJSONString(nameList.get("user3")));
    }

    /**
     * stream 中的 字段穿操作
     */
    @Test
    public void charTest(){
        List<User> userList = Stream.of(
                new User(1,18,"user1"),
                new User(2,19,"user2"),
                new User(3,20,"User3")).collect(Collectors.toList());
        // 将 用户名 组合 成一个新的字符串 joining 中的参数分别 代表 分隔符,前缀,后缀
        long time1 = System.currentTimeMillis();
       String result =  userList.stream().map(User::getName).collect(Collectors.joining(",","[","]"));
       log.info("执行时长 ---- :" + (System.currentTimeMillis()-time1));

        long time2 = System.currentTimeMillis();
        String result2 =  userList.parallelStream().map(User::getName).collect(Collectors.joining(",","[","]"));
        log.info("执行时长 ---- :" + (System.currentTimeMillis()-time2));
        log.info(result);
    }



}
