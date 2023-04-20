package src.Models;

import java.time.LocalDateTime;

public class Location
{
    private String numLocation;
    private String dateDebut;
    private String dateFin;
    private String etatLocation;
    private String nomClient;
    private String prenomClient;
    private String nomVoiture;

    public Location(String numLocation, String nomClient,String prenomClient ,String nomVoiture, String dateDebut, String dateFin, String etatLocation)
    {
        this.numLocation = numLocation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.etatLocation = etatLocation;
        this.nomClient = nomClient;
        this.nomVoiture = nomVoiture;
    }

    // les méthodes gets
    public String getNumLocation() {return numLocation;}
    public String getDateDebut() {return dateDebut;}
    public String getDateFin() {return dateFin;}
    public String getEtatLocation() {return etatLocation;}
    public String getNomClient() {return nomClient;}
    public String getNomVoiture() {return nomVoiture;}

    //les méthodes sets
    public void setNumLocation(String numLocation) {this.numLocation = numLocation;}
    public void setDateDebut(String dateDebut) {this.dateDebut = dateDebut;}
    public void setDateFin(String dateFin) {this.dateFin = dateFin;}
    public void setEtatLocation(String etatLocation) {this.etatLocation = etatLocation;}
    public void setNumClient(String nomClient) {this.nomClient = nomClient;}
}




