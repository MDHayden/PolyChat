package PolyChat;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mélanie DUBREUUL 3APP
 * 
 */

public class Client implements Runnable {    
    private int portDestinataire;
    private String ipDestinataire,login;
    private DatagramSocket ds;

    // Crée un objet client et lui associe un socket
    public Client(int port,String ip){
        try {
            ds = new DatagramSocket();
            this.portDestinataire = port;
            this.ipDestinataire = ip;
        } catch (SocketException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getPortDestinataire() {
        return portDestinataire;
    }

    public String getIpDestinataire() {
        return ipDestinataire;
    }

    public String getLogin() {
        return login;
    }
    
    public DatagramSocket getDs() {
        return ds;
    }
    
    public void envoyer(String s, String ipServeur, int portServeur) throws UnknownHostException{               
        try {            
            byte[] data = s.getBytes("UTF-8");
            DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(ipServeur),portServeur); 
            ds.send(dp);
        } catch (IOException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void recevoir(){        
        byte[] buffer = new byte[500];
        DatagramPacket dpResponse = new DatagramPacket(buffer, buffer.length);
        try {
            ds.receive(dpResponse);
            // Il faut prendre en compte le premier retour à la communication et affecter le nouveau couple (@IP;port)
            if(dpResponse.getPort() != this.portDestinataire ||
                    !dpResponse.getAddress().getHostAddress().equals(this.ipDestinataire)){
                this.portDestinataire = dpResponse.getPort();
            }
            String s = new String(dpResponse.getData(),"UTF-8");
            System.out.println("\n"+this.login+" >> Nouveau message :" + s);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);         
            String message="";
            
            // Premier message envoyé : l'utilisateur renseigne son login
            System.out.println("\nCLIENT >> Veuillez entrer votre login");
            message = scanner.nextLine();
            this.login = message;
            this.envoyer(message,ipDestinataire,portDestinataire);
            this.recevoir();
            
            do {
                // On envoie un message tant que le client souhaite communiquer avec le serveur
                System.out.println("\n"+this.login+" >> Message ? (tapez \"stop\" pour quitter l'application)");
                message = scanner.nextLine();
                this.envoyer(message,ipDestinataire,portDestinataire);
                // Et on reçoit une réponse de la communication dédiée
                this.recevoir();
            } while(!message.equals("stop"));
            
            // Fin de la communication
            System.out.println("\n\n**********************************************************");
            System.out.println("**************** Vous êtes déconnecté(e) ******************");
            System.out.println("**********************************************************");            
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            ds.close();
        }
    }
}
