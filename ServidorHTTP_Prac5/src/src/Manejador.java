/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

import Resource.Utileria;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 *
 * @author IVAN
 */
public class Manejador extends Utileria implements Runnable{
        
    public final static Logger log = Logger.getLogger(Manejador.class.getName());
    
    /**
     * Constructor del Manejador
     * @param cliente Al cual se le asignaran los flujos de lectura y escritura para la comunicación
     * @param servidor
     * @throws java.io.IOException
     */
    public Manejador(Socket cliente, Servidor servidor) throws IOException{
        this.cl = cliente;
        log.info("Construyendo flujos de lectura y escritura...");
        br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        bos = new BufferedOutputStream(new BufferedOutputStream(cl.getOutputStream()));
        pw = new PrintWriter(new OutputStreamWriter(bos));
        log.info("Flujos de lectura y escritura construidos Ok.");
        this.serv = servidor;
        
        FileServerPage = System.getProperty("")+
                System.getProperty("System.line")+
                "Resource"+
                System.getProperty("System.line")+
                "ServerPage.html";
    }
    
    @Override
    public void run(){
        try {
            String linea = br.readLine();
            serv.addMensajeInfo("Cliente conectado desde: "+ cl.getInetAddress());
            serv.addMensajeInfo("Por el puerto: "+ cl.getPort());
            serv.addMensajeInfo("Datos: "+ linea);
            if(linea == null){
                SendA(FileServerPage);
                cl.close();
                return;
            }
            if(!linea.contains("?")){
                reedireccionar(linea);
            }else if(linea.startsWith("GET")){
                StringTokenizer st = new StringTokenizer(linea,"?");
            }
        } catch (IOException ex) {
            serv.addMensajeInfo("Error al leer la petici\u00f3n: "+ex.getMessage());
        }
    }
    
}
