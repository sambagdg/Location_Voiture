package src.Models;

public class Voiture
{
    private String matricule;
    private String nomVoiture;
    private String prixLocVoiture;
    private String modele;
    private String marque;

    public Voiture(String matricule, String nomVoiture, String prixLocVoiture, String modele, String marque)
    {
        this.matricule = matricule;
        this.nomVoiture = nomVoiture;
        this.prixLocVoiture  =prixLocVoiture;
        this.modele = modele;
        this.marque = marque;
    }

    //Les méthodes gets
    public String getMatricule() {return matricule;}
    public String getNomVoiture() {return nomVoiture;}
    public String getPrixLocVoiture() {return prixLocVoiture;}
    public String getModele() {return modele;}
    public String getMarque() {return marque;}

    // Les méthodes sets
    public void setMatricule(String matricule) {this.matricule = matricule;}
    public void setNomVoiture(String nomVoiture) {this.nomVoiture = nomVoiture;}
    public void setPrixLocVoiture(String prixLocVoiture) {this.prixLocVoiture = prixLocVoiture;}
    public void setModele(String modele) {this.modele = modele;}
    public void setMarque(String marque) {this.marque = marque;}
}
