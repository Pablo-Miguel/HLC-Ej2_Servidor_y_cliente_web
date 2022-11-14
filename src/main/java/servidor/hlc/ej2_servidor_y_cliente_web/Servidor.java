/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.hlc.ej2_servidor_y_cliente_web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Dam
 */
public class Servidor {
    
    public static void main(String[] args) throws IOException {
        int puerto = 6070;
        
        ServerSocket socketServidor = new ServerSocket(puerto);
        
        while(true){
            System.out.println("Esperando peticion...");
            
            Socket conexionCliente = socketServidor.accept();
            
            HiloServidorNavegador hiloServidor = new HiloServidorNavegador(conexionCliente);
            
            hiloServidor.start();
            
        }
        
    }
    
}
