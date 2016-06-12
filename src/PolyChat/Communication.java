package PolyChat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mélanie DUBREUIL 3APP
 * 
 */

public class Communication implements Runnable {    
    private final int portCommunication,portClient;
    private DatagramSocket ds;
    private final String ipClient;
    
    public Communication(int pCommunication, String adresseClient, int pClient) {
        portCommunication = pCommunication;
        ipClient = adresseClient;
        portClient = pClient;    
        try {
            ds = new DatagramSocket(portCommunication);
        } catch (SocketException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void envoyer(String s, String ip, int port) throws UnknownHostException{        
        try {
            byte[] data = s.getBytes("UTF-8");
            DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(ip),port);
            ds.send(dp);
        } catch (IOException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean recevoir(){
        byte[] buffer = new byte[500];
        DatagramPacket dpResponse = new DatagramPacket(buffer, buffer.length);
        String s = "";
        
        try {
            ds.receive(dpResponse);
            s = new String(dpResponse.getData(),"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // On vérifie que le client ne souhaite pas arrêter la communication (mot-clé "stop")
        if(s.substring(0,4).equals("stop")){
            // Le cas échéant, il faut signaler que la communication doit s'arrêter
           return true;
        } else {
            // Sinon on continue l'échange
           System.out.println("\nROOM "+ this.portCommunication + ">> Nouveau message : " + s);
           return false; 
        }
    }

    @Override
    public void run() {        
        Scanner scanner = new Scanner(System.in);
        boolean fin = false;
        
        System.out.println("ROOM"+ this.portCommunication + ">> Communication ouverte (port "+portCommunication+")");
        System.out.println("\nROOM"+ this.portCommunication + ">> Message à envoyer au client ?");
        String message = scanner.nextLine();
        
        // On envoie un premier message au client
        try {
            this.envoyer(message,this.ipClient,this.portClient);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Puis tant que le client ne souhaite pas arrêter la communication, on dialogue avec lui
        while(fin == false){
            try {
                fin = this.recevoir();
                // On envoie un message tant que le client souhaite communiquer avec le client
                System.out.println("\nROOM"+ this.portCommunication + ">> Message à envoyer au client ?");
                message = scanner.nextLine();
                this.envoyer(message,this.ipClient,this.portClient);
            }
            catch (UnknownHostException ex) {
                Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Fin de la communication : on signale que la ROOM est fermée
        System.out.println("\nROOM"+ this.portCommunication + " fermée");
        ds.close();
    }
}
