/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.hlc.ej2_servidor_y_cliente_web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dam
 */
public class HiloServidor extends Thread {
    
    private Socket conexionCliente;
    //private BufferedReader flujoEntrada;
    //private PrintWriter flujoSalida;
    private DataInputStream flujoEntrada;
    private DataOutputStream flujoSalida;
    private String urlCliente;

    public HiloServidor(Socket conexionCliente) throws IOException {
        this.conexionCliente = conexionCliente;
        //this.flujoEntrada = new BufferedReader(new InputStreamReader(conexionCliente.getInputStream()));
        //this.flujoSalida =  new PrintWriter(conexionCliente.getOutputStream(), true);
        this.flujoEntrada = new DataInputStream(conexionCliente.getInputStream());
        this.flujoSalida =  new DataOutputStream(conexionCliente.getOutputStream());
    }
    
    @Override
    public void run(){
        
        try {
            
            
            //urlCliente = flujoEntrada.readLine();
            urlCliente = flujoEntrada.readUTF();
            
            String[] listaUrl = urlCliente.trim().split("/");
            
            String archivo = "";
            
            for(int i = 3; i < listaUrl.length; i++){
                archivo += "\\" + listaUrl[i];
            }
            
            
            if(urlCliente.contains("GET") && (urlCliente.contains("http://") || urlCliente.contains("https://"))){
                
            
                File file = new File("C:\\Users\\Dam\\Desktop" + archivo);
                System.out.println("ENTRA");
                if(file.exists()){
                    
                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(file);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    scanner.useDelimiter(",\\s*");
                    
                
                    //flujoSalida.println("200");
                
                    flujoSalida.writeInt(200);
                    
                    StringBuilder linea = new StringBuilder();
                    while(scanner.hasNextLine()){
                        linea.append(scanner.nextLine() + "\r\n");
                    }
                    
                    flujoSalida.writeUTF(linea.toString());
                    
                } else {
                    flujoSalida.writeInt(404);
                }
               
            }
            else {
                
                //flujoSalida.println("404");
                flujoSalida.writeInt(404);
                
            }
            
            flujoEntrada.close();
            flujoSalida.close();
            conexionCliente.close();
            
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
