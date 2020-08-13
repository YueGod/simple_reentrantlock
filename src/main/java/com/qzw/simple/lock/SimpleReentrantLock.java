package com.qzw.simple.lock;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: 屈子威
 * @create: 2020-08-13
 * @description: 实现一个简单的ReentrantLock
 **/
public class SimpleReentrantLock {

    /**
     * 定义一个阻塞队列，当发现锁被占有的时候则进入阻塞队列
     */
    private static ConcurrentLinkedQueue<Thread> threadQueue = new ConcurrentLinkedQueue<Thread>();

    /**
     * 定义一个静态变量，用来记录当前线程
     */
    private volatile static Thread executorThread;

    /**
     * 定义一个锁状态，根据锁状态来判断是否已被加锁
     */
    private volatile int state;

    /**
     * 是否为公平锁
     */
    private boolean isFair;

    public SimpleReentrantLock() {
        this.isFair = true;
    }

    public SimpleReentrantLock(boolean isFair) {
        this.isFair = isFair;
    }

    /**
     * 定义一个加锁的方法
     */
    public void lock() {
        enqueueOrLock(Thread.currentThread());
    }


    /**
     * 入队或拿锁
     */
    public void enqueueOrLock(Thread thread) {
        /**
         * 判断如果state == 0说明当前没有线程持有锁，则对当前线程进行加锁操作
         */
        if (state == 0) {
            ++state;
            executorThread = thread;

        } else if (state > 0) {
            /**
             * 首先判断是否被加锁，如果>0则被加锁，进而判断进来的线程是否为当前线程，是当前线程则重入
             * 并将state + 1；
             * 如果不等于当前线程，则让进入的线程进入阻塞状态
             */
            if (thread == executorThread) {
                ++state;
            } else {
                threadQueue.add(thread);
                while (true) {
                    if (state == 0) {
                        ++state;
                        Thread next = threadQueue.poll();
                        executorThread = next;
                    }
                }
            }
        }
    }

    /**
     * 释放锁
     */
    public void unlock() {
        if (state > 0) {
            --state;
        }
    }

}
