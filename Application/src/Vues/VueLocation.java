package src.Vues;

import src.Models.Location;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class VueLocation extends JFrame {
    private JButton buttonAccueil;
    private JPanel locationPanel;
    private JTable tableLocation;
    private JComboBox comboBoxClient;
    private JComboBox comboBoxVehicule;
    private JTextField txtDateLoc;
    private JTextField txtDateFin;
    private JButton créerButton;
    private JButton annulerLaLocationButton;
    private JButton modifierButton;
    private JComboBox comboNom;
    private JButton ajouterLeClientButton;
    private JTextField txtNumClient;
    private JTextField txtPrenom;
    private JTextField txtMatricule;
    private JTextField txtNumLoc;
    private JButton actualiserLeTableauButton;
    private JLabel labNumClient;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private ArrayList<Location> locations;
    private DefaultTableModel model;
    String [] nomColonne = {"NUMÉRO", "NOM CLIENT", "PRÉNOM CLIENT", "VÉHICULE", "DATE DEBUT", "DATE FIN", "ÉTAT"};

    // Mes fonctions
    public void Connecter(){
        final String URL = "jdbc:mysql://localhost:8889/location";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        try {
            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateComboC(){
        String sql = "SELECT nom FROM CLIENT ORDER BY 'nom'";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                comboNom.addItem(rs.getString("nom"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void GetInfosClient(){
        String nom = comboNom.getSelectedItem().toString();

        String sql = "SELECT numClient, prenom FROM CLIENT WHERE nom = ? ";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String numClient = rs.getString("numClient");
                String prenom = rs.getString("prenom");
                txtNumClient.setText(numClient);
                txtPrenom.setText(prenom);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void updateComboV(){
        String sql = "SELECT nomVoiture FROM VOITURE ORDER BY `nomVoiture`";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                comboBoxVehicule.addItem(rs.getString("nomVoiture"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void GetInfosV(){
        String nomVoiture = comboBoxVehicule.getSelectedItem().toString();
        String sql = "SELECT matriculeVoiture FROM VOITURE WHERE nomVoiture = ?";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            pst.setString(1, nomVoiture);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
               if (comboBoxVehicule.getSelectedItem().toString() != "Selectionner"){
                   String matricule = rs.getString("matriculeVoiture");
                   txtMatricule.setText(matricule);
               }else {
                   JOptionPane.showMessageDialog(null, "Vous devez selectionner un véhicule");
               }

            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void viderChamps(){
        txtNumLoc.setText("");
        txtNumClient.setText("");
        txtPrenom.setText("");
        txtMatricule.setText("");
        txtDateLoc.setText("//");
        txtDateFin.setText("//");
        comboNom.setSelectedItem("Selectionner le nom du client");
        comboBoxVehicule.setSelectedItem("Selectionner le véhicule");
    }




    public VueLocation(){
        locations = new ArrayList();
        model = new DefaultTableModel(nomColonne, 0);
        tableLocation.setModel(model);
        add(locationPanel);
        setTitle("Gestion Location");
        setSize(650, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Table();
        txtNumClient.enable(false);
        txtPrenom.enable(false);
        txtMatricule.enable(false);
        comboNom.addItem("Selectionner le nom du client");
        comboBoxVehicule.addItem("Selectionner le véhicule");
        txtNumClient.hide();
        labNumClient.hide();
        updateComboV();
        updateComboC();




        buttonAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueLocation.super.dispose();
                Accueil ac = new Accueil();
            }
        });
        ajouterLeClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueLocation.super.dispose();
                VueClient cl = new VueClient();
            }
        });
        créerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        comboNom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboNom.getSelectedItem().toString() != "Selectionner le nom du client"){
                    GetInfosClient();
                }else {

                }
            }
        });

        comboBoxVehicule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetInfosV();
            }
        });


        créerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (comboNom.getSelectedItem().toString() != "Selectionner le nom du client"&& comboBoxVehicule.getSelectedItem().toString() != "Selectionner le véhicule")
                {
                    String num = txtNumLoc.getText();
                    String nom = comboNom.getSelectedItem().toString();
                    String nomVoiture = comboBoxVehicule.getSelectedItem().toString();
                    String dateDbt = txtDateLoc.getText();
                    String dateFn = txtDateFin.getText();

                    boolean locExistant = false;
                    boolean numExistant = false;

                    for (int i = 0; i<locations.size();i++){
                        Location location = locations.get(i);
                        if(dateDbt.equals(location.getDateDebut()) && dateFn.equals(location.getDateFin()) && nom.equals(location.getNomClient()) && nomVoiture.equals(location.getNomVoiture())){
                            locExistant = true;

                        } else if (num.equals(location.getNumLocation())){
                            numExistant = true;
                        }
                    }

                    if (locExistant){
                        JOptionPane.showMessageDialog(null,"Tu as deja crée la meme location pour ce client...", "Location existante", JOptionPane.ERROR_MESSAGE);
                    } else if (numExistant) {
                        JOptionPane.showMessageDialog(null,"Tu as deja attribué ce numéro a une location...", "numéro existant", JOptionPane.ERROR_MESSAGE);
                    } else{
                        String sql = "INSERT INTO LOCATION(numLocation, dateDebut, dateFin, etatLocation, numClient	, matriculeVoiture)" + "VALUES(?, ?, ?, ?, ?,?)" ;
                        try{

                            Connecter();
                            String numLocation, dateDebut, dateFin, etatLocation, numClient, matriculeVoiture;

                            numLocation = txtNumLoc.getText();
                            dateDebut = txtDateLoc.getText();
                            dateFin = txtDateFin.getText();
                            etatLocation = "En attente de validation";
                            numClient = txtNumClient.getText();
                            matriculeVoiture = txtMatricule.getText();

                            pst = con.prepareStatement(sql);

                            pst.setString(1, numLocation);
                            pst.setString(2, dateDebut);
                            pst.setString(3, dateFin);
                            pst.setString(4, etatLocation);
                            pst.setString(5, numClient);
                            pst.setString(6, matriculeVoiture);
                            pst.executeUpdate();
                            con.close();
                            JOptionPane.showMessageDialog(null, "La location à été enregistrer...", "Création réussie ", JOptionPane.INFORMATION_MESSAGE);
                            VueLocation.super.dispose();
                            VueLocation location = new VueLocation();

                        }catch (Exception e1){
                            e1.printStackTrace();
                        }

                    }

                }else {
                        JOptionPane.showMessageDialog(null, "Le nom du client ou le véhicule n'as pas été sélectionner ", "Formulaire incomplet", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tableLocation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1){
                    int indice = tableLocation.getSelectedRow();
                    if(indice != -1){
                        txtNumLoc.setText(model.getValueAt(indice,0).toString());
                        txtNumLoc.enable(false);
                        txtDateLoc.setText(model.getValueAt(indice,4).toString());
                        comboNom.setSelectedItem(model.getValueAt(indice, 1).toString());
                        comboNom.enable(false);
                        txtDateLoc.enable(false);
                        txtDateFin.setText(model.getValueAt(indice,5).toString());
                        comboBoxVehicule.setSelectedItem(model.getValueAt(indice,3).toString());

                    }
                } else if (e.getButton()== MouseEvent.BUTTON3) {
                    viderChamps();
                    comboNom.enable(true);
                    txtNumLoc.enable(true);
                    txtDateLoc.enable(true);
                    tableLocation.clearSelection();
                }
            }
        });


        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBoxVehicule.getSelectedItem().toString() != "Selectionner le véhicule" && txtDateFin.getText() != "//"){
                    String num = txtNumLoc.getText();
                    String dateFin = txtDateFin.getText();
                    String matricule = txtMatricule.getText();
                    String sql = "UPDATE LOCATION SET dateFin = ?, matriculeVoiture = ? WHERE numLocation = ?";
                    int indice = tableLocation.getSelectedRow();
                      if(indice != -1){
                          int reponse = JOptionPane.showConfirmDialog(null, "Es tu sur de vouloir modifier cette location?", "Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                           if (reponse == JOptionPane.YES_OPTION){
                               try
                               {
                                  Connecter();
                                  pst = con.prepareStatement(sql);
                                  pst.setString(1, dateFin);
                                  pst.setString(2,matricule);
                                  pst.setString(3,num);

                                  pst.executeUpdate();
                                  con.close();
                                  JOptionPane.showMessageDialog(null, "Les infos ont bien été modifier...", "Modification réussie ", JOptionPane.INFORMATION_MESSAGE);
                                  viderChamps();
                                  VueLocation.super.dispose();
                                  VueLocation location = new VueLocation();
                               }catch (Exception e1)
                               {
                                  e1.printStackTrace();
                               }
                           }else {
                               VueLocation.super.dispose();
                               VueLocation loc = new VueLocation();
                           }
                      }

                }else {
                    JOptionPane.showMessageDialog(null , "Le formulaire est incomplet...");
                }
            }
        });

        actualiserLeTableauButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueLocation.super.dispose();
                VueLocation location = new VueLocation();
            }
        });

        annulerLaLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indice = tableLocation.getSelectedRow();
                      if(indice != -1){
                           int reponse = JOptionPane.showConfirmDialog(null, "Es tu sur de vouloir annuler cette location?", "Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                           if (reponse == JOptionPane.YES_OPTION){
                               String num = txtNumLoc.getText();
                               String sql = "DELETE FROM LOCATION WHERE numLocation = ?";
                               try {
                                   Connecter();
                                   pst = con.prepareStatement(sql);

                                   pst.setString(1,num);

                                   pst.executeUpdate();
                                   con.close();
                                   JOptionPane.showMessageDialog(null, "La location à bien été annulée...", "Modification réussie ", JOptionPane.INFORMATION_MESSAGE);
                                   viderChamps();
                               }catch (Exception e1){
                                   e1.printStackTrace();
                               }

                           }else {
                               VueLocation location = new VueLocation();
                           }

                      }
            }
        });
    }

    public void Table(){
        try{
            Connecter();
            Statement st = con.createStatement();

            rs = st.executeQuery("SELECT numLocation, dateDebut, dateFin, etatLocation, nom, prenom, nomVoiture FROM LOCATION INNER JOIN CLIENT ON LOCATION.numClient = CLIENT.numClient INNER JOIN VOITURE ON VOITURE.matriculeVoiture = LOCATION.matriculeVoiture");

            while (rs.next()){
                String numLocation, nom,prenom, dateDebut, dateFin, etaLocation, nomVoiture;

                numLocation =rs.getString("numLocation");
                nom = rs.getString("nom");
                prenom = rs.getString("prenom");
                dateDebut = rs.getString("dateDebut");
                dateFin = rs.getString("dateFin");
                etaLocation = rs.getString("etatLocation");
                nomVoiture = rs.getString("nomVoiture");

                Location l = new Location(numLocation, nom, prenom, nomVoiture, dateDebut, dateFin, etaLocation);
                locations.add(l);
                model.insertRow(model.getRowCount(), new Object[]{
                        numLocation, nom, prenom, nomVoiture, dateDebut, dateFin, etaLocation
                });

            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
