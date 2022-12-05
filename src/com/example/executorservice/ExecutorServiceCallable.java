package com.example.executorservice;

import com.example.app.Test;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutorServiceCallable implements Callable<String> {

    // En este ejemplo, trabajamos con la interfaz Callable(método call()) que es similar a Runnable(método run())
    // pero con las diferencias que su método propaga Exception y devuelve un valor genérico.
    @Override
    public String call() throws Exception {
        Test.Log("Inicio de la tarea");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Test.Log("Finaliza la tarea");
        return "Resultado de la tarea";
    }

    public void executorCall() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            //Future es una clase que monitoriza nuestro hilo y encapsula la devolución de call()
            Future<String> future
                    = exec.submit(new ExecutorServiceCallable());


            int cont = 0;
            String resultado = "";
            Test.Log(future.isDone());
            while (!future.isDone()) { // isDone() nos informa de si el hilo ha terminado su tarea
                TimeUnit.MILLISECONDS.sleep(100);
                cont++;
                Test.Log("Vuelta nº"+cont);                
            }
            if(!future.isCancelled()) { // isCancelled() nos devuelve booleano en función de si nuestro hilo ha sido cancelado o no
                resultado = future.get();// get() espera a que el hilo termine su tarea para devolvernos el valor
                future.cancel(true); // Este método no hace nada aqui pero sirve para cancelar el hilo
            } 
            Test.Log(future.isDone());

            Test.Log(resultado);

            exec.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(ExecutorServiceCallable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
