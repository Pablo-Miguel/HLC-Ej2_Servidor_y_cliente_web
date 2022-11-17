/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.hlc.ej2_servidor_y_cliente_web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dam
 */
public class HiloServidorNavegador extends Thread {

    private Socket conexionCliente;
    private InputStream flujoEntrada;
    private OutputStream flujoSalida;

    public HiloServidorNavegador(Socket conexionCliente) throws IOException {
        this.conexionCliente = conexionCliente;
        this.flujoEntrada = conexionCliente.getInputStream();
        this.flujoSalida = conexionCliente.getOutputStream();
    }

    @Override
    public void run() {

        try {

            InputStreamReader entrada = new InputStreamReader(flujoEntrada);
            BufferedReader reader = new BufferedReader(entrada);
            String line = reader.readLine();
            System.out.println(line);
            String[] lineas = line.split("/");
            
            line = "";
            for(int i = 1; i < lineas.length; i++){
                
                if(lineas[i].contains(" HTTP")){
                    line += "\\" + lineas[i].substring(0, lineas[i].length() - 5);
                    break;
                } else {
                    line += "\\" + lineas[i];
                }
                
            }
            
            if(line.equals("")){
                line = "index.html";
            }

            File file = new File("C:\\Users\\Dam\\Desktop" + line);
            
            System.out.println("C:\\Users\\Dam\\Desktop" + line);

            if (file.exists()) {

                Scanner scanner = new Scanner(file);
                scanner.useDelimiter(",\\s*");

                StringBuilder linea = new StringBuilder();
                while (scanner.hasNextLine()) {
                    linea.append(scanner.nextLine() + "\r\n");
                }
                
                String tipoMime = "";
                if(line.endsWith(".html")){
                    tipoMime = "text/html";
                } else if(line.endsWith(".css")){
                    tipoMime = "text/css";
                } else if(line.endsWith(".js")){
                    tipoMime = "application/javascript";
                } else if(line.endsWith(".jpg") || line.endsWith(".jpeg")){
                    tipoMime = "image/jpeg";
                } else if(line.endsWith(".png")){
                    tipoMime = "image/png";
                } else if(line.endsWith(".gif")){
                    tipoMime = "image/gif";
                } else if(line.endsWith(".pdf")){
                    tipoMime = "application/pdf";
                }
                
                String httpResponse
                        = "HTTP/1.1 200 OK\r\n"
                        + "Content-Length: " + linea.toString().getBytes().length
                        + "\r\nContent-Type: " + tipoMime + "; charset=utf-8"
                        + "Server: ServidorWebPropio\r\n"
                        + "Date: " + new Date()
                        + "\r\n\r\n" + linea.toString() + "\r\n\r\n";
                
                System.out.println(httpResponse);

                flujoSalida.write(httpResponse.getBytes("UTF-8"));
                flujoSalida.flush();
            } else {
                String linea = "<h1>Error: 404</h1>";
                String httpResponse
                        = "HTTP/1.1 404 NOT OK\r\n"
                        + "Content-Length: " + linea.toString().getBytes().length
                        + "\r\nContent-Type: text/html; charset=utf-8"
                        + "Server: ServidorWebPropio\r\n"
                        + "Date: " + new Date()
                        + "\r\n\r\n" + linea.toString() + "\r\n\r\n";

                conexionCliente.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                flujoSalida.flush();
            }
            
            reader.close();
            entrada.close();
            flujoEntrada.close();
            flujoSalida.close();
            conexionCliente.close();

        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
