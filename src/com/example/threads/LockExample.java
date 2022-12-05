package com.example.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;


// Un bloqueo se adquiere mediante lock()y se libera mediante unlock().
// Es importante envolver su código en un try/finallybloque para garantizar el 
// desbloqueo en caso de excepciones.

public class LockExample {

    Lock lock = new ReentrantLock();
    int count = 0;

    public void lockExample() {
        lock.lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName()+" "+count);
        } finally {
            lock.unlock();
        }
    }

    public void practiceLock() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
       
        IntStream.range(0, 1_000_000)
                .forEach(i -> executor.submit(this::lockExample));
        stop(executor);
        System.out.println(count);
    }
    
    
    // A veces es útil convertir un bloqueo de lectura en un bloqueo de escritura sin desbloquear y bloquear de nuevo. 
    // StampedLock proporciona el método tryConvertToWriteLock()
    public void stampedeLockExample() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.readLock();
            try {
                if (count == 0) {
                    stamp = lock.tryConvertToWriteLock(stamp);
                    if (stamp == 0L) {
                        System.out.println("Could not convert to write lock");
                        stamp = lock.writeLock();
                    }
                    count = 23;
                }
                System.out.println(count);
            } finally {
                lock.unlock(stamp);
            }
        });

        stop(executor);
    }

    public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
