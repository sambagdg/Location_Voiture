package src.Models;

import java.util.concurrent.TimeoutException;

public class Client {

    private String numClient;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String tel;

    // Le constructeur

     public Client(String numClient, String nom, String prenom, String email, String adresse, String tel){
        this.numClient = numClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.tel = tel;
    }

    //    les méthodes gets
    public String getNumClient() {return numClient;}
    public String getNom() {return nom;}
    public String getPrenom() {return prenom;}
    public String getEmail() {return email;}
    public String getAdresse() {return adresse;}
    public String getTel() {return tel;}

    //    les méthodes sets
    public void setNumClient(String numClient) {this.numClient = numClient;}
    public void setNom(String nom) {this.nom = nom;}
    public void setPrenom(String prenom) {this.prenom = prenom;}
    public void setEmail(String email) {this.email = email;}
    public void setAdresse(String adresse) {this.adresse = adresse;}
    public void setTel(String tel) {this.tel = tel;}

}
