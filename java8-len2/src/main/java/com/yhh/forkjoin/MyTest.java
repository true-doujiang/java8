package com.yhh.forkjoin;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

public class MyTest {

    @Test
    public void test1() {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();

        // 意味着线程池将使用 4 个处理器核心。
        ForkJoinPool pool = new ForkJoinPool(4);

        pool.submit(() -> {
            for (int i=0; i<10; i++) {
                System.out.println("submit " + i);
            }
        });

        pool.execute(() -> {
            for (int i=0; i<10; i++) {
                System.out.println("execute " + i);
            }
        });


    }


}
