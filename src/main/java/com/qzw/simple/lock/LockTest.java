package com.qzw.simple.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: 屈子威
 * @create: 2020-08-13
 * @description: 加锁测试类
 **/
public class LockTest {
    public static int count = 0;
    public static SimpleReentrantLock lock = new SimpleReentrantLock();

    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j <= 100; j++) {
                        lock.lock();
                        try {

                            ++count;

                        } finally {
                            lock.unlock();
                        }
                    }
                }
            }).start();
        }

        System.out.println(count);
    }
}

class AtomicTest {

    public static AtomicInteger count = new AtomicInteger(0);
    public static Object o = new Object();

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    synchronized (o) {
                        for (int j = 0; j <= 1000; j++) {
                            count.addAndGet(1);
                        }
                    }
                }
            }).start();
        }
        System.out.println(count.get());
    }
}

