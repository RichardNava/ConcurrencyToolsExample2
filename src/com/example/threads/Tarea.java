package com.example.threads;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Tarea implements Runnable{

    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tarea(String nombre) {
        this.nombre = nombre;
    }
      
       
    
    @Override
    public void run() {
        for (int i = 0; i <5; i++) {
            System.out.println("tarea: "+nombre);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }        
    }
    
    
}
