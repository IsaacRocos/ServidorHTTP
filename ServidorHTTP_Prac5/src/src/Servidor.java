/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author IVAN
 */
public class Servidor extends JFrame implements Runnable {

    public ServerSocket servidor;
    public static final Logger log = Logger.getLogger(Servidor.class.getName());

    public Servidor() {
        super("Servidor HTTP");
        this.init();
        this.drawInterfaz();
    }

    public final void init() {
        ImageIcon ii = new ImageIcon(getClass().getResource("/Img/port.png"));
        jlblIcono = new JLabel(new ImageIcon(ii.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH)));
        jlblTitulo = new JLabel("Servidor HTTP");

        jlblPuerto = new JLabel("Puerto del Servicio: ");
        jlblDirectorio = new JLabel("Directorio Raíz de la APP WEB: ");
        jlblIndex = new JLabel("Pagina Home: ");

        jtfPuerto = new JTextField("8000", 15);
        jtfPuerto.setHorizontalAlignment(javax.swing.JTextField.CENTER);


        jtfIndex = new JTextField("Intext.html", 15);
        jtfIndex.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jtfDirectorio = new JTextField("\\", 15);
        jtfDirectorio.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jtaInfo = new JTextArea(10, 30);
        jtaInfo.setEditable(false);

        jscpInfo = new JScrollPane(jtaInfo);

        jbtnStart = new JButton("Escuchar una petición");
        jbtnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbtnStartActionPerformed(e);
            }
        });

        jpCenter = new JPanel(new java.awt.GridLayout(3, 1, 0, 0));
        jpWest = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        jpSouth = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jpNorth = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));

        jpCenterFila1 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jpCenterFila2 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jpCenterFila3 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
    }

    public final void drawInterfaz() {
        this.add("North", jpNorth);
        this.add("South", jpSouth);
        this.add("East", jscpInfo);
        this.add("West", jpWest);
        this.add("Center", jpCenter);

        jpNorth.add(jlblTitulo);
        jpSouth.add(jbtnStart);
        jpWest.add(jlblIcono);
        jpCenter.add(jpCenterFila1);
        jpCenter.add(jpCenterFila2);
        jpCenter.add(jpCenterFila3);

        jpCenterFila1.add(jlblPuerto);
        jpCenterFila1.add(jtfPuerto);
        jpCenterFila2.add(jlblIndex);
        jpCenterFila2.add(jtfIndex);
        jpCenterFila3.add(jlblDirectorio);
        jpCenterFila3.add(jtfDirectorio);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setVisible(true);
        this.pack();
        this.setResizable(false);
    }

    public static void main(String args[]) {
        Servidor servidor1 = new Servidor();
    }

    public void jbtnStartActionPerformed(ActionEvent e) {
        Thread hilo = new Thread(this);
        hilo.start();
        jbtnStart.setEnabled(false);
    }

    @Override
    public void run() {
        try {
            this.addMensajeInfo("Iniciando Servicio...");

            this.servidor = new ServerSocket(Integer.parseInt(this.jtfPuerto.getText()));
            ExecutorService executor = Executors.newFixedThreadPool(100);
            this.addMensajeInfo("Servicio Levantado Ok.");
            this.addMensajeInfo("Esperando cliente...");
            for (;;) {
                //Manejador manejador = new Manejador(cl, this);
                this.addMensajeInfo("Esperando peticion");
                Socket cl = servidor.accept();
                this.addMensajeInfo("Peticion recibida");
//                Thread manejador = new Thread(new Manejador(cl, this));
//                manejador.start();
                executor.execute(new Manejador(cl, this));
            }
            //this.addMensajeInfo("Servicio Terminado");
        } catch (IOException ex) {
            this.addMensajeError("Error al iniciar al conectarse al puerto:" + ex.getMessage());
            jbtnStart.setEnabled(true);
        }
    }

    public void addMensajeInfo(String mensaje) {
        mensaje = ">>" + mensaje;
        log.info(mensaje);
        jtaInfo.append(mensaje + "\n");
        jtaInfo.setCaretPosition(jtaInfo.getText().length() - 1);
    }

    public void addMensajeError(String mensaje) {
        log.severe(mensaje);
        jtaInfo.append("**********ERROR**********\n");
        jtaInfo.append(mensaje + "\n");
        jtaInfo.append("*************************\n");
        jtaInfo.setCaretPosition(jtaInfo.getText().length() - 1);
    }

    public void addMensajeWarning(String mensaje) {
        log.warning(mensaje);
        jtaInfo.append("*********PELIGRO*********\n");
        jtaInfo.append(mensaje + "\n");
        jtaInfo.append("*************************\n");
        jtaInfo.setCaretPosition(jtaInfo.getText().length() - 1);
    }
    /**
     * ************** Componentes JFrame ***********************
     */
    public JLabel jlblIcono;
    public JLabel jlblTitulo;
    public JLabel jlblIndex;
    public JLabel jlblPuerto;
    public JLabel jlblDirectorio;
    public JTextField jtfPuerto;
    public JTextField jtfIndex;
    public JTextField jtfDirectorio;
    public JTextArea jtaInfo;
    public JScrollPane jscpInfo;
    public JButton jbtnStart;
    public JPanel jpCenter;
    public JPanel jpWest;
    public JPanel jpSouth;
    public JPanel jpNorth;
    public JPanel jpCenterFila1;
    public JPanel jpCenterFila2;
    public JPanel jpCenterFila3;
    /**
     * *********************************************************
     */
}
