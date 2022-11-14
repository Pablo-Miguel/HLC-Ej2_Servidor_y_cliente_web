/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.hlc.ej2_servidor_y_cliente_web;

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
            
            //String url = getHeaderToArray(flujoEntrada).split("\r\n")[11];
            
            //if(url.contains("https://") || url.contains("http://")){
                
                //String[] listaUrl = url.trim().split("/");
            
                //String archivo = "";

                //for(int i = 3; i < listaUrl.length; i++){
                    //archivo += "\\" + listaUrl[i];
                //}
                
                //File file = new File("C:\\Users\\Dam\\Desktop" + archivo);
                
                File file = new File("C:\\Users\\Dam\\Desktop\\index.html");
                
                if (file.exists()) {

                    Scanner scanner = new Scanner(file);
                    scanner.useDelimiter(",\\s*");

                    StringBuilder linea = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        linea.append(scanner.nextLine() + "\r\n");
                    }

                    String httpResponse
                            = "HTTP/1.1 200 OK\r\n"
                            + "Content-Length: " + linea.toString().getBytes().length
                            + "\r\nContent-Type: text/html; charset=utf-8"
                            + "Server: ServidorWebPropio\r\n"
                            + "Date: " + new Date()
                            + "\r\n\r\n" + linea.toString() + "\r\n\r\n";

                    flujoSalida.write(httpResponse.getBytes("UTF-8"));
                } else {
                    String linea = "<h1>Error: 404</h1>";
                    String httpResponse
                            = "HTTP/1.1 200 OK\r\n"
                            + "Content-Length: " + linea.toString().getBytes().length
                            + "\r\nContent-Type: text/html; charset=utf-8"
                            + "Server: ServidorWebPropio\r\n"
                            + "Date: " + new Date()
                            + "\r\n\r\n" + linea.toString() + "\r\n\r\n";

                    conexionCliente.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                }
            //}

            flujoEntrada.close();
            flujoSalida.close();
            conexionCliente.close();

        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static String getHeaderToArray(InputStream inputStream) {

        String headerTempData = "";

        // chain the InputStream to a Reader
        Reader reader = new InputStreamReader(inputStream);
        try {
            int c;
            while ((c = reader.read()) != -1) {
                //System.out.print((char) c);
                headerTempData += (char) c;

                if (headerTempData.contains("\r\n\r\n"))
                    break;
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return headerTempData;
    }
}
