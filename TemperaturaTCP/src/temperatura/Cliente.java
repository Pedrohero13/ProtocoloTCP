/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temperatura;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 */
public class Cliente {

    private static final int _PUERTO = 1234;
    private static final String _IP = "25.66.109.25";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        // TODO code application logic here
        InetAddress ipServidor = InetAddress.getByName(_IP);
        DatagramSocket dgmSocket = null;

        try {

            dgmSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {

            try {
                Random random = new Random();

                int temp = 10 + random.nextInt((43 - 10) + 1);
                int humedad = 10 + random.nextInt((90 - 10) + 1);
                int co2 = 2950 + random.nextInt((3030 - 2950) + 1);

                //ENVIAR DATOS
                String cadena = temp + "/" + humedad + "/" + co2;

                byte[] mensaje_bytes = new byte[10];
                mensaje_bytes = cadena.getBytes();

                DatagramPacket dgmPaquete = new DatagramPacket(mensaje_bytes, cadena.length(), ipServidor, _PUERTO);
                dgmSocket.send(dgmPaquete);

                //LEER RESPUESTA 
                byte bufferEntrada[] = new byte[256];
                dgmPaquete = new DatagramPacket(bufferEntrada, 256);
                dgmSocket.receive(dgmPaquete);
                String entrada = new String(bufferEntrada).trim();
                System.out.println("Respuesta: " + entrada);

                Thread.sleep(3000);

            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
