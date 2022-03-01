/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temperatura;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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
       
        while (true) {

            try {
                Socket socketCliente = null;
                DataInputStream datosRecepcion = null;
                DataOutputStream datosSalida = null;
                Random random = new Random();

                int temp = 10 + random.nextInt((43 - 10) + 1);
                int humedad = 10 + random.nextInt((90 - 10) + 1);
                int co2 = 2950 + random.nextInt((3030 - 2950) + 1);

                //ENVIAR DATOS
                char tipo = 's';
                String data = temp + "/" + humedad + "/" + co2;
                byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);

                socketCliente = new Socket(ipServidor, _PUERTO);
                datosRecepcion = new DataInputStream(socketCliente.getInputStream());
                datosSalida = new DataOutputStream(socketCliente.getOutputStream());

                datosSalida.writeChar(tipo);
                datosSalida.writeInt(dataInBytes.length);
                datosSalida.write(dataInBytes);

                String resultado = datosRecepcion.readUTF();

                System.out.println("Solicitud: " + data + " \tResultado: " + resultado);
                datosRecepcion.close();
                datosSalida.close();

                Thread.sleep(2000);

            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
