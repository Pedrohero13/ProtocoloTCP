/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temperatura;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author pedro
 */
public class Servidor {
private static final String _IP = "25.66.109.25";
    private static final int _PUERTO = 1234;
    private static final int _BAGLOG = 50;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        // TODO code application logic here
        // TODO code application logic here
        InetAddress ip = InetAddress.getByName(_IP);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        ServerSocket servidor = null;

        try {
            servidor = new ServerSocket(_PUERTO, _BAGLOG, ip);
            System.out.println("Escuchando en: " + servidor.getInetAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                Socket socketPeticion = servidor.accept();
                DataOutputStream datosSalida = new DataOutputStream(socketPeticion.getOutputStream());
                DataInputStream datosEntrada = new DataInputStream(socketPeticion.getInputStream());

                int puertoRemitente = socketPeticion.getPort();
                InetAddress ipRemitente = socketPeticion.getInetAddress();
                char tipo = datosEntrada.readChar();
                int longitud = datosEntrada.readInt();

                String entrada = "";
                String salida = "";
                if (tipo == 's') {
                    byte[] byteDatos = new byte[longitud];
                    datosEntrada.read(byteDatos);
                    entrada = new String(byteDatos).trim();
                    String split[] = entrada.split("/");
                    int temp = Integer.parseInt(split[0]);
                    int hume = Integer.parseInt(split[1]);
                    int co2 = Integer.parseInt(split[2]);
                    DAOLog dao = new DAOLog();

                    if (temp < 15 || temp > 39 || hume > 80 || co2 > 3000) {
                        salida = "Se genero un evento";
                        Log log = guardar(ipRemitente.toString(), puertoRemitente, entrada, true);
                        dao.guardar(log);
                    } else {
                        Log log = guardar(ipRemitente.toString(), puertoRemitente, entrada, false);
                        dao.guardar(log);
                        salida = "No se genero evento";
                    }
                } else {
                    System.out.println("No es tipo String");
                }

                System.out.println(formatter.format(new Date()) + "\tCliente = " + ipRemitente.toString() + ":" + puertoRemitente
                        + "\tEntrada = " + entrada + "\tSalida = " + salida);

                datosSalida.writeUTF(salida);
                datosEntrada.close();
                datosSalida.close();
                socketPeticion.close();
            } catch (Exception ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
    }
    public static Log guardar(String ip, int puerto, String entrada, boolean evento){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
        formatter.format(new Date());
        
        Log log = new Log();
        log.setFecha(formatter.format(new Date()));
        log.setHora(hora.format(new Date()));
        log.setIpRemitente(ip);
        log.setPuerto(puerto);
        log.setEntrada(entrada);
        log.setEvento(evento);
        return log;
        
    }
    
}
