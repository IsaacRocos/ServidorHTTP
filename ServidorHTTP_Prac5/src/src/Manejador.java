/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import Resource.Utileria;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
public class Manejador extends Utileria implements Runnable {

    public final static Logger log = Logger.getLogger(Manejador.class.getName());

    /**
     * Constructor del Manejador
     *
     * @param cliente Al cual se le asignaran los flujos de lectura y escritura
     * para la comunicación
     * @param servidor
     * @throws java.io.IOException
     */
    public Manejador(Socket cliente, Servidor servidor) throws IOException {
        this.cl = cliente;
        log.info("Construyendo flujos de lectura y escritura...");
        br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        bos = new BufferedOutputStream(new BufferedOutputStream(cl.getOutputStream()));
        pw = new PrintWriter(new OutputStreamWriter(bos));
        log.info("Flujos de lectura y escritura construidos Ok.");
        this.serv = servidor;

        String separador = System.getProperty("file.separator");
        FileServerPage = System.getProperty("user.dir") + separador + "Resource" + separador + "ServerPage.html";

    }

    @Override
    public void run() {
        try {
            serv.addMensajeInfo("Cliente conectado desde: " + cl.getInetAddress());
            serv.addMensajeInfo("Por el puerto: " + cl.getPort());
            String linea = br.readLine();
            serv.addMensajeInfo("Datos: " + linea);
            if ("".equals(linea) || linea == null) {
                SendA(FileServerPage);
            } else if (!linea.contains("?")) {
                reedireccionar(linea);
            } else if (linea.startsWith("GET")) {
                StringTokenizer st = new StringTokenizer(linea, "?");
                String req_a = st.nextToken();
                String req = st.nextToken();
                System.out.println("Token1: " + req_a + "\r\n\r\n");
                System.out.println("Token2: " + req + "\r\n\r\n");
                pw.println("HTTP/1.0 200 Okay");
                pw.flush();
                pw.println();
                pw.flush();
                pw.print("<html><head><title>SERVIDOR WEB");
                pw.flush();
                pw.print("</title></head><body bgcolor=\"#AACCFF\"><h1><br>Parametros Obtenidos..</br></h1>");
                pw.flush();

                String patron = "\\+";
                req = req.replaceAll(patron, " ");
                req = req.replaceAll("%0D%0A", "<br> ");
                req = req.replaceAll("HTTP/1.1", " ");
                req = req.replaceAll("=", "= ");
                StringTokenizer streq = new StringTokenizer(req, "&");
                pw.print("<ul>");
                pw.flush();
                while (streq.hasMoreTokens()) {
                    pw.print("<li>" + streq.nextToken() + "</li>");
                    pw.flush();
                }
                pw.print("</ul>");
                pw.flush();
                pw.print("</body></html>");
                pw.flush();
                pw.println("\n\r");
                pw.flush();
                cl.close();
            } else if (linea.startsWith("POST")) {
                System.out.println("POST");
            } else {
                pw.println("HTTP/1.0 501 Not Implemented");
                cl.close();
                pw.println();
            }
        } catch (IOException ex) {
            serv.addMensajeInfo("Error al leer la petici\u00f3n: " + ex.getMessage());
        }
    }
}
