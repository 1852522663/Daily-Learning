package com.learning.demo.model;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-09-18
 */

public class CommonUtils {

    public static <T, R> Optional<R> processLogic(T input, Function<Optional<T>, R> logicFunction) {
        // 检查输入参数是否为空
        Optional<T> optional = Optional.ofNullable(input);
        // 执行逻辑处理
        R processedOutput = logicFunction.apply(optional);
        // 返回处理后的值包装在Optional中
        return Optional.ofNullable(processedOutput);
    }
//    public static void main(String[] args) {
//
//
////        String input = "Hello, World!";
////
////        // 定义一个逻辑函数，将输入字符串转换为大写
////        Function<Optional<String>, String> logicFunction = optional -> optional.map(String::toUpperCase).orElse("Input is null");
////
////        Optional<String> result = processLogic(input, logicFunction);
////        System.out.println(result);  // 输出: HELLO, WORLD!
////
////        Optional<String> nullResult = processLogic(null, logicFunction);
////        System.out.println(nullResult);  // 输出: Input is null
//
//        Map<String, Integer> inputMap = Map.of("A", 1, "B", 2, "C", 3);
//
//        // 定义一个逻辑函数，将输入的Map中的值加倍
//        Function<Optional<Map<String, Integer>>, List<Map<String, Integer>>> logicFunction = optional -> optional.map(map ->
//                map.entrySet().stream()
//                        .map(entry -> Map.of(entry.getKey(), entry.getValue() * 2))
//                        .collect(Collectors.toList())
//        ).orElse(List.of());
//
//        Optional<List<Map<String, Integer>>> result = processLogic(inputMap, logicFunction);
//        System.out.println(result);  // 输出: [{A=2}, {B=4}, {C=6}]
//
//        Optional<List<Map<String, Integer>>> nullResult = processLogic(null, logicFunction);
//        System.out.println(nullResult);  // 输出: []
//    }

}
