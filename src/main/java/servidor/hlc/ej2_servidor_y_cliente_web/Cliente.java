/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.hlc.ej2_servidor_y_cliente_web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Dam
 */
public class Cliente {
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        String url = "GET ";
        
        System.out.println("Introduzca url: ");
        url += scanner.nextLine();
        
        String[] listaUrl = url.trim().split("/");
        
        String host = listaUrl[2].split(":")[0];
        
        int puerto = Integer.parseInt(listaUrl[2].split(":")[1]);
        
        System.out.println("HOST: " + host + "\nPUERTO: " + puerto);
        
        Socket cliente = new Socket(host, puerto);
        
        //PrintWriter flujoSalida = new PrintWriter(cliente.getOutputStream(), true);
        DataOutputStream flujoSalida = new DataOutputStream(cliente.getOutputStream());
        
        //flujoSalida.write(url);
        flujoSalida.writeUTF(url);
        
        //BufferedReader flujoEntrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        DataInputStream flujoEntrada = new DataInputStream(cliente.getInputStream());
        
        //String respuesta = flujoEntrada.readLine();
        int codError = flujoEntrada.readInt();
        if(codError == 200){
            System.out.println(flujoEntrada.readUTF());
        }
        else{
            System.out.println(codError);
        }
        
        flujoSalida.close();
        flujoEntrada.close();
        cliente.close();
        
    }
    
}
