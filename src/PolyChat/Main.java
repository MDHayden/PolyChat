package PolyChat;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mélanie DUBREUIL 3APP
 * 
 */

public class Main {    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String réponse;
        
        System.out.println("**********************************************************");
        System.out.println("************* Bienvenue dans le PolyChat *****************");
        System.out.println("**********************************************************\n");
        System.out.println("Souhaitez-vous créer le serveur du chat ? (O/N) ");
        
        // On va d'abord créer un serveur à la demande de l'utilisateur
        do {
            réponse = scanner.nextLine();
        } while(!réponse.equals("O") && !réponse.equals("N"));
        
        if(réponse.equals("O")){
            réponse="";
            Serveur s = initServeur();
            Thread thread_serveur = new Thread(s);
            thread_serveur.start();
        }       
        
        // Puis on va créer un client à la demande de l'utilisateur
        System.out.println("\nSouhaitez-vous vous connecter au serveur ? (O/N) ");
        do {
            réponse = scanner.nextLine();
        } while(!réponse.equals("O") && !réponse.equals("N"));
        
        if(réponse.equals("O")){
            Client c = nouveauClient();        
            Thread thread_client = new Thread(c);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            thread_client.start();
        }
    }
    
    public static Serveur initServeur(){
        Scanner scanner = new Scanner(System.in);
        Serveur s = null;
        
        System.out.println("\n\n**********************************************************");
        System.out.println("************** Mise en place du serveur ******************");
        System.out.println("**********************************************************");
                        
        System.out.println("\nVeuillez renseigner l'adresse IP du serveur :");              
        String ipServeur = scanner.nextLine();
        
        System.out.println("\nVeuillez renseigner le port du serveur :");              
        String message= scanner.nextLine();
        int portServeur = Integer.parseInt(message);
        
        System.out.println("\nVeuillez renseigner le début de la plage de ports :");              
        message= scanner.nextLine();
        int débutPlage = Integer.parseInt(message);
        
        System.out.println("\nVeuillez renseigner la fin de la plage de ports :");              
        message= scanner.nextLine();
        int finPlage = Integer.parseInt(message);
        
        s = new Serveur(portServeur,ipServeur,débutPlage,finPlage);
        return s;
    }
    
    public static Client nouveauClient() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n\n**********************************************************");
        System.out.println("***************** Votre configuration ********************");
        System.out.println("**********************************************************");
        
        System.out.println("\nVeuillez renseigner l'adresse IP du serveur sur lequel vous souhaitez vous connecter:");
        String ipServeur = scanner.nextLine();
        
        System.out.println("\nVeuillez renseigner le port du serveur :");              
        String buffer= scanner.nextLine();
        int portServeur = Integer.parseInt(buffer);
        
        System.out.println("\nVeuillez patienter...");
        
        Client c = new Client(portServeur,ipServeur);
        
        return c;
    }
}
