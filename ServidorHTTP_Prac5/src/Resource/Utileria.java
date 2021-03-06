/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.Servidor;

/**
 *
 * @author IVAN
 */
public class Utileria {

    protected Socket cl;
    protected PrintWriter pw;
    protected BufferedOutputStream bos;
    protected BufferedReader br;
    protected String FileServerPage;
    protected String FileName;
    protected Servidor serv;
    protected int tipoGet_Head;

    public String getArch(String line) {
        int i;
        int f;
        if (line.toUpperCase().startsWith("GET")) {
            i = line.indexOf("/");
            f = line.indexOf(" ", i);
            FileName = line.substring(i + 1, f);
            this.tipoGet_Head = 0;
        }
        if (line.toUpperCase().startsWith("GET")) {
            i = line.indexOf("/");
            f = line.indexOf(" ", i);
            FileName = line.substring(i + 1, f);
            this.tipoGet_Head = 1;
        }
        return FileName;
    }

    public void reedireccionar(String respuesta) {
        getArch(respuesta);
        serv.addMensajeInfo("Reedireccionado...");
        if (FileName.compareTo("") == 0) {
            FileName = System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "src" + System.getProperty("file.separator")
                    + "pages"
                    + System.getProperty("file.separator")
                    + "index.html";
        } else {
            FileName = System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "src" + System.getProperty("file.separator")
                    + "pages"
                    + System.getProperty("file.separator")
                    + FileName;
        }
        SendA(FileName);
        serv.addMensajeInfo("Archivo reedireccionado: " + FileName);
        FileName = "";
    }

    public void SendA(String nombreArch) {
        int tam_archivo = 0;
        try {
            int b_leidos;
            try (BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(nombreArch))) {
                byte[] buf;
                int tam_bloque = 0;
                if (bis2.available() >= 1024) {
                    tam_bloque = 1024;
                } else {
                    tam_bloque = bis2.available();
                }
                tam_archivo = bis2.available();
                buf = new byte[tam_bloque];
                /**
                 * ********************************************
                 */
                System.out.println("Escribiendo cabecera HTTP");
                String sb = "";
                sb = sb + "HTTP/1.0 200 ok\n";
                sb = sb + "Server: II_PR_CE/1.0 \n";
                sb = sb + "Date: " + new Date() + " \n";
                sb = sb + "Content-Type: text/html \n";
                sb = sb + "Content-Length: " + tam_archivo + " \n";
                sb = sb + "\r\n";
                bos.write(sb.getBytes());
                bos.flush();
                /**
                 * ********************************************
                 */
                System.out.println("Escribiendo recurso");
                while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                    bos.write(buf, 0, b_leidos);
                    if (bis2.available() >= 1024) {
                        tam_bloque = 1024;
                    } else {
                        bis2.available();
                    }
                    buf = new byte[tam_bloque];
                    serv.addMensajeInfo("\tBytes leidos: " + buf.length);
                }
                bos.flush();
                serv.addMensajeInfo("Recurso enviado");
            }
        } catch (IOException e) {
            try {
                String archNotFnd = System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "src" + System.getProperty("file.separator")
                    + "pages"
                    + System.getProperty("file.separator")
                    + "NotFound.html";
                int b_leidos;
                try (BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(archNotFnd))) {
                    byte[] buf;
                    int tam_bloque = 0;
                    if (bis2.available() >= 1024) {
                        tam_bloque = 1024;
                    } else {
                        tam_bloque = bis2.available();
                    }
                    tam_archivo = bis2.available();
                    buf = new byte[tam_bloque];
                    System.out.println("Escribiendo cabecera HTTP de error");
                    String sb = "";
                    sb = sb + "HTTP/1.1 404 Not Found\n";
                    sb = sb + "Server: II_PR_CE/1.0 \n";
                    sb = sb + "Date: " + new Date() + " \n";
                    sb = sb + "Content-Type: text/html \n";
                    sb = sb + "Content-Length: " + tam_archivo + " \n";
                    sb = sb + "\r\n";
                    bos.write(sb.getBytes());
                    bos.flush();

                    System.out.println("Escribiendo recurso");
                    while ((b_leidos = bis2.read(buf, 0, buf.length)) != -1) {
                        bos.write(buf, 0, b_leidos);
                        if (bis2.available() >= 1024) {
                            tam_bloque = 1024;
                        } else {
                            bis2.available();
                        }
                        buf = new byte[tam_bloque];
                        serv.addMensajeInfo("\tBytes leidos: " + buf.length);
                    }
                    bos.flush();
                    serv.addMensajeInfo("Recurso enviado");
                }
            } catch (IOException ex) {
                serv.addMensajeError("Error al enviar cabecera de error " + nombreArch + " : " + e.getMessage());
            }
        }
    }

    public void procesarDo(String linea) {
        if (linea.startsWith("GET")) {
            StringTokenizer tokens = new StringTokenizer(linea, "?");
            String req_a = tokens.nextToken();
            String req = tokens.nextToken();
            System.out.println("Token1: " + req_a + "\r\n\r\n");
            System.out.println("Token2: " + req + "\r\n\r\n");
            pw.println("HTTP/1.0 200 Okay");
            pw.flush();
            pw.println();
            pw.flush();
            pw.print("<html><head><title>SERVIDOR WEB");
            pw.flush();
            pw.print("</title></head><body bgcolor=\"#AACCFF\"><center><h1><br>Parametros Obtenidos..</br></h1>");
            pw.flush();
            pw.print("<h3><b>" + req + "</b></h3>");
            pw.flush();
            pw.print("</center></body></html>");
            pw.flush();
        }
    }
}
