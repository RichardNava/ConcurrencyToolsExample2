
package com.example.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CyclicBarrierExample extends Thread{
    private int duration;
    private CyclicBarrier barrier;
    
    // En este ejemplo usamos un CyclicBarrier simple (solo determinamos el número de hilos que va a esperar la barrera). Los hilos
    // se detienen al llegar al await() esperando a que lleguen los demás hilos. Una vez que han llegado todos, los hilos continuan
    // con su proceso independientemente
    public CyclicBarrierExample(int duration, CyclicBarrier barrier,String name){
        super(name);
        this.duration=duration;
        this.barrier=barrier;
    }
    
    
    @Override
    public void run() {      
        try {
            Thread.sleep(duration);
            // Instrucciones que llevan tiempo
            System.out.println(Thread.currentThread().getName()
            +" is calling await()");
            barrier.await();
            System.out.println(Thread.currentThread().getName()
            +" has started running again.");
                       
        } catch (InterruptedException |BrokenBarrierException ex) {
            Logger.getLogger(CyclicBarrierExample.class.getName()).log(Level.SEVERE, "error", ex.getCause());
        } 
    }
    
    
    
}
