package com.learning.demo.completable_future;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-08-22
 */
public class CompletableFutureTest {

    private static final int SLEEP_TIME = 3000;




    /**
     * 用Callable交给FutureTask，然后使用Thread承接FutureTask，其中FutureTask的作用是可以主线程获取到子线程的返回值和异常。
     */
    public static void callableTest() {
        Callable<String> callable = () -> {
            System.out.println("我是子任务");
            return "sub task done";
        };
        FutureTask<String> task = new FutureTask<>(callable);
        Thread thread = new Thread(task);
        thread.start();
        System.out.println("子线程启动");
        try {
            String subResult = task.get(5, TimeUnit.MINUTES);
            System.out.println("子线程返回值" + subResult);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        System.out.println("main 结束");
    }

    /**
     * 用Callable交给Executors，获取到子线程的返回值和异常。
     */
    public static void executorCallable() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

//        Runnable runnableTask = () -> {
//            System.out.println("我是子任务");
//        };
//        将Runnable转为callable
//        Callable<Object> callable = Executors.callable(runnableTask);

        Callable<String> callable = () -> {
            System.out.println("我是子任务");
            return "sub task done";
        };

        Future<String> submit = threadPool.submit(callable);

        try {
            String result = submit.get();
            System.out.println("子线程返回值" + result);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("main 结束");
        threadPool.shutdown();
    }

    public static void executorPool() {
        //------------------- 固定线程数量的线程池 -------------
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> {
            System.out.println("线程一");
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executor.submit(() -> System.out.println("线程二"));
        executor.shutdown();

        //-------------- 定时任务线程池 ------------------------
        ScheduledExecutorService executorScheduled = Executors.newScheduledThreadPool(2);
        //延迟1s 执行
        executorScheduled.schedule(() -> System.out.println("线程一"), 1, TimeUnit.SECONDS);
        //延迟2s 执行  3s执行一次 不能shutdown
        executorScheduled.scheduleAtFixedRate(() -> {
            System.out.println("Task 2 executed after 2 seconds and every 3 seconds thereafter");
        }, 2, 3, TimeUnit.SECONDS);
        executorScheduled.shutdown();

        //------------- 自动调整线程数量的线程池 -----------------
        ExecutorService cachedExecutor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int taskNumber = i;
            cachedExecutor.execute(() -> {
                System.out.println("Task " + taskNumber + " executed");
            });
        }

        // 关闭线程池
        cachedExecutor.shutdown();

        //------------- 单个线程的线程池 ---------------------
        ExecutorService executorSingle = Executors.newSingleThreadExecutor();

        executorSingle.submit(() -> {
            System.out.println("线程一");
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorSingle.submit(() -> System.out.println("线程二"));

        executorSingle.shutdown();


        //------------- 基于工作窃取算法的线程池 ---------------------
        //工作窃取算法是一种用于多线程任务调度的算法，它允许空闲的线程从其他线程的任务队列中窃取任务来执行，从而提高了任务的并行性和效率。
        ExecutorService workStealingPool = Executors.newWorkStealingPool();
        for (int i = 0; i < 5; i++) {
            final int taskNumber = i;
            workStealingPool.execute(() -> {
                System.out.println("Task " + taskNumber + " executed by thread " + Thread.currentThread().getName());
            });
        }

        // 关闭线程池
        workStealingPool.shutdown();
    }

    public static void completableFutureTest() {
        // 异步任务1：获取用户信息   开启任务 有返回值
        CompletableFuture<String> getUserInfo = CompletableFuture.supplyAsync(() -> {
            // 模拟异步获取用户信息
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "User123";
        });

        // 异步任务2：根据用户信息获取订单信息 连接两个任务
        CompletableFuture<String> getOrderInfo = getUserInfo.thenCompose(userInfo -> CompletableFuture.supplyAsync(() -> {
            // 模拟异步获取订单信息
            return "Order456 for " + userInfo;
        }));

        // 异步任务3：处理订单信息 接收上个任务的返回值，没有返回值
        CompletableFuture<Void> processOrder = getOrderInfo.thenAccept(orderInfo -> {
            System.out.println("Processing order: " + orderInfo);
        });

        // 异常处理 ：出现异常 解决异常
        CompletableFuture<Void> exceptionHandling = processOrder.exceptionally(ex -> {
            System.out.println("An error occurred: " + ex.getMessage());
            return null; // 返回默认值
        });

        // 等待所有任务完成  返回的CompletableFuture是多个任务都执行完成后才会执行，只有有一个任务执行异常，则返回的CompletableFuture执行get方法时会抛出异常，如果都是正常执行，则get返回null。
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(getUserInfo, getOrderInfo, processOrder);

        try {
            allTasks.get(); // 阻塞直到所有任务完成
            System.out.println("All tasks completed.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}