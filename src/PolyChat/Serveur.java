package PolyChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mélanie DUBREUIL 3APP
 * 
 */

public class Serveur implements Runnable {    
    private final int portServeur,plageDébut,plageFin;
    private final String ipServeur;
    private DatagramSocket ds;
    private final HashMap<Integer, Boolean> plagePorts;
    
    // Crée un objet serveur et lui associe un socket
    public Serveur(int port, String ip, int pDébut, int pFin){
        this.plagePorts = new HashMap<>();
        this.plageDébut = pDébut;
        this.plageFin = pFin;
        this.portServeur = port;
        this.ipServeur = ip;
        
        try {
            ds = new DatagramSocket(portServeur);
        } catch (SocketException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPlagePorts (int port,boolean statut){
        plagePorts.put(port,statut);
    }
    
    public void initialiserPlagePorts(int pDébut, int pFin){
        // Pour chaque port de la plage, on lui affecte un statut : ouvert(true) ou fermé(false)
        for (int i = pDébut; i < pFin; i++){
            try {
                DatagramSocket socket = new DatagramSocket(i);
                plagePorts.put(i, true);
                socket.close();
            } catch (SocketException ex) {
                plagePorts.put(i, false);
                Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
           
    @Override
    public void run() {
        Thread threadCommunicationClient;
        int indicePort = plageDébut;
        
        try {
            initialiserPlagePorts(plageDébut,plageFin);
            DatagramPacket dp = new DatagramPacket(new byte[512], 512);
            // On fait tourner le serveur à l'infini ...
            while(true){
                // ... pour qu'il gère chaque communication client entrante
                ds.receive(dp);
                // Tant que le port n'est pas disponible, on va essayer d'en écouter un autre
                while(plagePorts.get(indicePort) != true){
                    indicePort ++;
                }
                // le port choisi est maintenant occupé : on change son statut
                this.setPlagePorts(indicePort, false);
                System.out.println("\nSERVEUR >> Un nouveau client s'est connecté... affectation à une communication dédiée\n");
                Communication c = new Communication(indicePort,dp.getAddress().toString().substring(1),dp.getPort());
                threadCommunicationClient = new Thread(c);        
                threadCommunicationClient.start();
                indicePort++;
            }
        } catch (IOException ex) {
           Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ds.close();
        }
    } 
}
