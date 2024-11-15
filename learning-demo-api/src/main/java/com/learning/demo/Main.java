package com.learning.demo;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-09-14
 */
public class Main {
//    public static void main(String[] args) {
//        // forEach
//        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
//        features.forEach(n -> System.out.println(n));
//
//        // 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
//        features.forEach(System.out::println);
//
//
//        List<String> list = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.", "Canada");
//        String G7Countries = list.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
//        System.out.println(G7Countries);
//
//        List<Integer> result = Stream.of(Arrays.asList(1, 3), Arrays.asList(5, 6)).flatMap(a -> a.stream()).collect(Collectors.toList());
//
//        System.out.println(result);
//
//        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
//        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x).summaryStatistics();
//        System.out.println("Highest prime number in List : " + stats.getMax());
//        System.out.println("Lowest prime number in List : " + stats.getMin());
//        System.out.println("Sum of all prime numbers : " + stats.getSum());
//        System.out.println("Average of all prime numbers : " + stats.getAverage());
//
//        IMyInterface iMyInterface = () -> System.out.println("I like study");
//        iMyInterface.study();
//
//
//    }

    @FunctionalInterface
    public interface IMyInterface {
        void study();
    }


}
