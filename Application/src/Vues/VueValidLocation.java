package src.Vues;

import src.Models.Location;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;



public class VueValidLocation extends JFrame {
    private JButton buttonAccueil;
    private JTable tableLocation;
    private JComboBox comboBoxVehicule;
    private JTextField txtDateLoc;
    private JTextField txtDateFin;
    private JComboBox comboNom;
    private JLabel labNumClient;
    private JTextField txtNumClient;
    private JTextField txtPrenom;
    private JTextField txtMatricule;
    private JTextField txtNumLoc;
    private JPanel locationPanel;
    private JTextField txtNom;
    private JTextField txtVoiture;
    private JButton btnConfirmer;
    private JButton annulerButton;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private ArrayList<Location> locations;
    private DefaultTableModel model;
    String [] nomColonne = {"NUMÉRO", "NOM CLIENT", "PRÉNOM CLIENT", "VÉHICULE", "DATE DEBUT", "DATE FIN", "ÉTAT"};

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

    public VueValidLocation(){

         locations = new ArrayList();
        model = new DefaultTableModel(nomColonne, 0);
        tableLocation.setModel(model);
        add(locationPanel);
        setTitle("Gestion Location");
        setSize(750, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Table();

        txtPrenom.enable(false);




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
                        txtNom.setText(model.getValueAt(indice, 1).toString());
                        txtNom.enable(false);
                        txtPrenom.setText(model.getValueAt(indice, 2).toString());
                        txtPrenom.enable(false);
                        txtDateLoc.enable(false);
                        txtDateFin.setText(model.getValueAt(indice,5).toString());
                        txtDateFin.enable(false);
                        txtVoiture.setText(model.getValueAt(indice,3).toString());
                        txtVoiture.enable(false);

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
        buttonAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueValidLocation.super.dispose();
                Accueil ac = new Accueil();
            }
        });
        btnConfirmer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numLocation = txtNumLoc.getText();
                String etat = "En cours";
                String sql = "UPDATE LOCATION SET etatLocation = ? WHERE numLocation = ?";
                try{
                    int indice = tableLocation.getSelectedRow();
                      if(indice != -1) {
                          Connecter();
                          pst = con.prepareStatement(sql);
                          pst.setString(1, etat);
                          pst.setString(2, numLocation);
                          pst.executeUpdate();
                          con.close();
                          JOptionPane.showMessageDialog(null, "La location à bien été confirmer", "Validation location", JOptionPane.INFORMATION_MESSAGE);
                          VueValidLocation.super.dispose();
                          VueValidLocation loc = new VueValidLocation();
                      }else {
                          JOptionPane.showMessageDialog(null, "Aucune location sélectionner", "Erreur", JOptionPane.ERROR_MESSAGE);
                      }
                }catch (Exception e1){
                   e1.printStackTrace();
                }
            }
        });
    }

   public void Table(){
        try{
            Connecter();
            String sql = "SELECT numLocation, dateDebut, dateFin, etatLocation, nom, prenom, nomVoiture FROM LOCATION " +
                    "INNER JOIN CLIENT ON LOCATION.numClient = CLIENT.numClient " +
                    "INNER JOIN VOITURE ON VOITURE.matriculeVoiture = LOCATION.matriculeVoiture WHERE etatLocation = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, "En attente de validation");
            rs = pst.executeQuery();
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
