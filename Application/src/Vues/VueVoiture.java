package src.Vues;

import com.toedter.calendar.JDateChooser;
import src.Models.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

public class VueVoiture extends JFrame {
    private JPanel panelVoiture;
    private JButton buttonAccueil;
    private JButton créerUneLocationButton;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JButton btnActualiser;
    private JTextField txtMatricule;
    private JTextField txtNom;
    private JTextField txtMarque;
    private JTable tableVoiture;
    private JComboBox comboBoxModele;
    private JTextField txtNumModele;
    private JTextField txtNumMarque;
    private JTextField txtPrix;


    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private ArrayList<Voiture> voitures;
    private DefaultTableModel model;
    String [] nomColonne = {"MATRICULE", "NOM", "MODEL", "MARQUE", "PRIX/JOURS"};

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
    private void updateComboModele(){
        String sql = "SELECT nomModele FROM MODElE ORDER BY `nomModele`";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                comboBoxModele.addItem(rs.getString("nomModele"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void getNumModel(){
        String nomModele = comboBoxModele.getSelectedItem().toString();
        String sql = "SELECT numModele, numMarque FROM MODELE WHERE nomModele = ?";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            pst.setString(1, nomModele);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
               if (comboBoxModele.getSelectedItem().toString() != "Selectionner"){
                   String numModele = rs.getString("numModele");
                   String numMarque = rs.getString("numMarque");
                   txtNumMarque.setText(numMarque);
                   txtNumModele.setText(numModele);
               }else {
                   JOptionPane.showMessageDialog(null, "Vous devez selectionner un model");
               }

            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void getMarque(){
        String numMarque = txtNumMarque.getText();
        String sql = "SELECT nomMarque FROM MARQUE WHERE numMarque = ?";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            pst.setString(1, numMarque);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                   String nomMarque = rs.getString("nomMarque");
                   txtMarque.setText(nomMarque);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void viderChamps(){
        txtMatricule.setText("");
        txtNom.setText("");
        txtPrix.setText("");
        comboBoxModele.setSelectedItem("Selectionner");
        txtMatricule.enable(true);
        txtNom.enable(true);
        txtPrix.enable(true);
        txtMarque.setText("");
        comboBoxModele.enable(true);
    }



    public VueVoiture(){
        voitures = new ArrayList();
        model = new DefaultTableModel(nomColonne, 0);
        tableVoiture.setModel(model);
        Table();
        comboBoxModele.addItem("Selectionner");
        updateComboModele();
        txtNumModele.enable(false);
        txtMarque.enable(false);
        txtNumMarque.enable(false);
        txtNumMarque.hide();
        txtNumModele.hide();

        add(panelVoiture);
        setTitle("Voiture");
        setSize(650, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        buttonAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueVoiture.super.dispose();
                Accueil home = new Accueil();
            }
        });
        créerUneLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueVoiture.super.dispose();
                VueLocation loc = new VueLocation();
            }
        });
        comboBoxModele.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getNumModel();
                getMarque();
            }
        });
        btnActualiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueVoiture voiture = new VueVoiture();
            }
        });
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMatricule.getText().isEmpty() || txtNom.getText().isEmpty() || comboBoxModele.getSelectedItem().toString() == "Selectionner"){
                    JOptionPane.showMessageDialog(null, "Rempli tous les champs et n'oublie pas de choisir le modele de la voiture....", "Champ(s) vide", JOptionPane.ERROR_MESSAGE);

                }else {
                    String matricule = txtMatricule.getText();
                    String nom = txtNom.getText();
                    String prix = txtPrix.getText();
                    boolean voitureExistant = false;
                    boolean numExistant = false;
                    for (int i = 0; i<voitures.size();i++){
                        Voiture voiture = voitures.get(i);
                        if( nom.equals(voiture.getNomVoiture()) && prix.equals(voiture.getPrixLocVoiture())){
                            voitureExistant = true;

                        } else if (matricule.equals(voiture.getMatricule())){
                            numExistant = true;
                        }
                    }
                    if (numExistant) {
                        JOptionPane.showMessageDialog(null,"Ce matricule appartient deja à une voiture...", "Matricule existant", JOptionPane.ERROR_MESSAGE);
                    }

                    else if (voitureExistant){
                        JOptionPane.showMessageDialog(null,"Cette voiture existe deja...", "Voiture existante", JOptionPane.ERROR_MESSAGE);
                    } else{

                        try {
                        Connecter();
                        pst = con.prepareStatement("INSERT INTO VOITURE(matriculeVoiture, nomVoiture, prixLocVoiture, numModele)" + "VALUES(?, ?, ?, ?) ");
                        pst.setString(1, txtMatricule.getText());
                        pst.setString(2, txtNom.getText());
                        pst.setString(3, txtPrix.getText());
                        pst.setString(4, txtNumModele.getText());
                        pst.executeUpdate();
                        con.close();

                        JOptionPane.showMessageDialog(null,"La voiture à été enregistrer avec succées", "Ajout Réussie",JOptionPane.INFORMATION_MESSAGE );
                        VueVoiture.super.dispose();
                        VueVoiture voiture = new VueVoiture();

                }catch (Exception e1){
                    e1.printStackTrace();
                }

                    }

                }

            }
        });

        tableVoiture.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1){
                    int indice = tableVoiture.getSelectedRow();
                    if(indice != -1){
                        txtMatricule.setText(model.getValueAt(indice,0).toString());
                        txtMatricule.enable(false);
                        txtNom.setText(model.getValueAt(indice, 1).toString());
                        txtNom.enable(false);
                        comboBoxModele.setSelectedItem(model.getValueAt(indice, 2).toString());
                        comboBoxModele.enable(false);
                        txtPrix.setText(model.getValueAt(indice, 4).toString());
                        txtPrix.enable(false);

                    }
                } else if (e.getButton()== MouseEvent.BUTTON3) {
                    viderChamps();
                    tableVoiture.clearSelection();
                }
            }
        });



        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indice = tableVoiture.getSelectedRow();
                if(indice != -1){
                    int reponse = JOptionPane.showConfirmDialog(null, "Es-tu sur de vouloir supprimer cette voiture?", "Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (reponse == JOptionPane.YES_OPTION){
                        String num = txtMatricule.getText();
                        String sql = "DELETE FROM VOITURE WHERE matriculeVoiture = ?";
                        try {
                            Connecter();
                            pst = con.prepareStatement(sql);

                            pst.setString(1,num);

                            pst.executeUpdate();
                            con.close();
                            JOptionPane.showMessageDialog(null, "La voiture à bien été supprimer...", "Suppression réussie ", JOptionPane.INFORMATION_MESSAGE);
                            viderChamps();
                            VueVoiture.super.dispose();
                            VueVoiture voiture = new VueVoiture();
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }

                    }else {
                        VueVoiture.super.dispose();
                        VueVoiture voiture = new VueVoiture();
                    }

                }
            }
        });
    }

    public void Table(){
        try{
            Connecter();
            Statement st = con.createStatement();

            rs = st.executeQuery("SELECT matriculeVoiture, nomVoiture, prixLocVoiture, nomModele, nomMarque " +
                                        "FROM VOITURE " +
                                        "INNER JOIN MODELE ON VOITURE.numModele = MODELE.numModele " +
                                        "INNER JOIN MARQUE ON MODELE.numMarque = MARQUE.numMarque;");

            while (rs.next()){
                String matricule, nom, prix, modele, marque;


                matricule =rs.getString("matriculeVoiture");
                nom = rs.getString("nomVoiture");
                prix = rs.getString("prixLocVoiture") + "€";
                modele = rs.getString("nomModele");
                marque = rs.getString("nomMarque");

                Voiture v = new Voiture(matricule, nom, prix, modele, marque);
                voitures.add(v);
                model.insertRow(model.getRowCount(), new Object[]{
                        matricule, nom, modele, marque, prix
                });

            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}



