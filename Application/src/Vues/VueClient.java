package src.Vues;

import src.Models.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

public class VueClient extends JFrame {
    private JTextField txtNum;
    private JTextField txtNom;
    private JTextField txtAdresse;
    private JTextField txtTel;
    private JButton Enregistrer;
    private JTable tableClients;
    private JPanel clientPanel;
    private JTextField txtPrenom;
    private JTextField txtEmail;
    private JButton buttonAccueil;
    private JButton btnActualiser;
    private JButton btnSupprimer;
    private JButton btnModifier;
    private JButton créerUneLocationButton;
    private JComboBox comboBoxVille;
    private JTextField txtNumVille;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private ArrayList<Client> clients;
    private DefaultTableModel model;
    String [] nomColonne = {"NUMÉRO", "NOM", "PRÉNOM", "EMAIL", "ADRESSE", "TÉLÉPHONE"};



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
    private void updateComboVille(){
        String sql = "SELECT nomVille FROM VILLE ORDER BY `nomVille`";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                comboBoxVille.addItem(rs.getString("nomVille"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void GetNumVille(){
        String nomVille = comboBoxVille.getSelectedItem().toString();
        String sql = "SELECT numVille FROM VILLE  WHERE nomVille = ?";
        try {
            Connecter();
            pst = con.prepareStatement(sql);
            pst.setString(1, nomVille);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String numVille = rs.getString("numVille");
                txtNumVille.setText(numVille);
                txtNumVille.enable(false);
            }
            con.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    private void viderChamps(){
        txtNum.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtAdresse.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        comboBoxVille.setSelectedItem("Selectionner");
    }

    // Constructeur
    public VueClient(){
        clients = new ArrayList();
        model = new DefaultTableModel(nomColonne, 0);
        tableClients.setModel(model);

        add(clientPanel);
        setTitle("client");
        setSize(700, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Table();
//        updateCombo()
        comboBoxVille.addItem("Selectionner");
        txtNumVille.hide();
        updateComboVille();

        Enregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (txtNum.getText().isEmpty() || txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() || txtAdresse.getText().isEmpty() || txtTel.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Remplissez tous les champs ....", "Champ(s) vide", JOptionPane.ERROR_MESSAGE);

                }else {
                    String num = txtNum.getText();
                    String nom = txtNom.getText();
                    String prenom = txtPrenom.getText();
                    boolean clientExistant = false;
                    boolean numExistant = false;
                    for (int i = 0; i<clients.size();i++){
                        Client client = clients.get(i);
                        if( nom.equals(client.getNom()) && prenom.equals(client.getPrenom())){
                            clientExistant = true;

                        } else if (num.equals(client.getNumClient())){
                            numExistant = true;
                        }
                    }
                    if (numExistant) {
                        JOptionPane.showMessageDialog(null,"Ce numéro appartient deja à un client...", "numéro existant", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (clientExistant){
                        JOptionPane.showMessageDialog(null,"Ce client existe deja...", "Client existant", JOptionPane.ERROR_MESSAGE);
                    } else{
                        try {
                        Connecter();
                        pst = con.prepareStatement("INSERT INTO CLIENT(numClient, nom, prenom, email, adresse, tel, numVille)" + "VALUES(?, ?, ?, ?, ?,?,?) ");
                        pst.setString(1, txtNum.getText());
                        pst.setString(2, txtNom.getText());
                        pst.setString(3, txtPrenom.getText());
                        pst.setString(4, txtEmail.getText());
                        pst.setString(5, txtAdresse.getText());
                        pst.setString(6, txtTel.getText());
                        pst.setString(7,txtNumVille.getText());
                        pst.executeUpdate();
                        con.close();
                        JOptionPane.showMessageDialog(null,"Le client à été enregistrer avec succées", "Ajout Réussie",JOptionPane.INFORMATION_MESSAGE );
                        VueClient.super.dispose();
                        VueClient vue = new VueClient();
                }catch (Exception e1){
                    e1.printStackTrace();
                }

                    }

                }
            }
        });

        buttonAccueil.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                VueClient.super.dispose();
                Accueil accueil = new Accueil();
                accueil.setVisible(true);
            }
        });


        btnActualiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueClient.super.dispose();
                VueClient cl = new VueClient();
            }
        });

        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNum.getText().isEmpty() || txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() || txtAdresse.getText().isEmpty() || txtTel.getText().isEmpty()){
                      JOptionPane.showMessageDialog(null, "Tu n'as pas sélectionner de client ....", "Aucun client", JOptionPane.ERROR_MESSAGE);
                  }
                  else {
                      int reponse = JOptionPane.showConfirmDialog(null, "Es tu sur de vouloir supprimer ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                      if (reponse == JOptionPane.YES_OPTION){
                          int indice = tableClients.getSelectedRow();
                          if(indice != -1){
                              try {
                                  Connecter();
                                  pst = con.prepareStatement("DELETE FROM CLIENT WHERE numClient = ?");
                                  pst.setString(1, txtNum.getText());
                                  pst.executeUpdate();
                                  con.close();
    
                                  JOptionPane.showMessageDialog(null, "Le client à bien été supprimer...", "Suppression réussie ", JOptionPane.INFORMATION_MESSAGE);
                                  viderChamps();
                                  VueClient.super.dispose();
                                  VueClient vue = new VueClient();
                              }catch (Exception e1){
                                  e1.printStackTrace();
                              }
                          }
                          
                      } else {
                          VueClient vc = new VueClient();
                      }

                }
            }
        });



        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  if (txtNum.getText().isEmpty() || txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtEmail.getText().isEmpty() || txtAdresse.getText().isEmpty() || txtTel.getText().isEmpty()){
                      JOptionPane.showMessageDialog(null, "Rempli tous les champs....", "Champ(s) vide", JOptionPane.ERROR_MESSAGE);
                  }
                  else {
                      int indice = tableClients.getSelectedRow();
                      if(indice != -1){
                          try {
                              Connecter();
                              pst = con.prepareStatement("UPDATE CLIENT SET nom = ?, prenom = ? , email = ?, adresse = ?, tel = ? WHERE numClient = ?");
                              pst.setString(6, txtNum.getText());
                              pst.setString(1, txtNom.getText());
                              pst.setString(2, txtPrenom.getText());
                              pst.setString(3, txtEmail.getText());
                              pst.setString(4, txtAdresse.getText());
                              pst.setString(5, txtTel.getText());
                              pst.executeUpdate();
                              con.close();
                              JOptionPane.showMessageDialog(null, "Les infos ont bien été modifier...", "Modification réussie ", JOptionPane.INFORMATION_MESSAGE);
                              viderChamps();
                              VueClient.super.dispose();
                              VueClient vue = new VueClient();
                          }catch (Exception e1){
                              e1.printStackTrace();
                          }
                      }
                  }
            }
        });

        tableClients.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1){
                    int indice = tableClients.getSelectedRow();
                    if(indice != -1){
                        txtNum.setText(model.getValueAt(indice,0).toString());
                        txtNum.enable(false);
                        txtNom.setText(model.getValueAt(indice,1).toString());
                        txtPrenom.setText(model.getValueAt(indice,2).toString());
                        txtEmail.setText(model.getValueAt(indice,3).toString());
                        txtAdresse.setText(model.getValueAt(indice,4).toString());
                        txtTel.setText(model.getValueAt(indice,5).toString());
                    }
                } else if (e.getButton()== MouseEvent.BUTTON3) {
                    viderChamps();
                    tableClients.clearSelection();
                }
            }
        });

        créerUneLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VueClient.super.dispose();
                VueLocation loc = new VueLocation();
            }
        });
        comboBoxVille.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetNumVille();
            }
        });
    }

    public void Table(){
        try{
            Connecter();
            Statement st = con.createStatement();

            rs = st.executeQuery("SELECT numClient, nom, prenom, email, adresse, tel, nomVille, cpVille " +
                                      "FROM CLIENT " +
                                      "INNER JOIN VILLE ON CLIENT.numVille = VILLE.numVille");

            while (rs.next()){
                String numClient, nom, prenom, email, adresse, tel;

                numClient =rs.getString("numClient");
                nom = rs.getString("nom");
                prenom = rs.getString("prenom");
                email = rs.getString("email");
                adresse = rs.getString("adresse") +" "+ rs.getString("cpVille") + " " + rs.getString("nomVille");
                tel = rs.getString("tel");

                Client c = new Client(numClient, nom, prenom, email, adresse, tel);
                clients.add(c);
                model.insertRow(model.getRowCount(), new Object[]{
                        numClient, nom, prenom, email, adresse, tel
                });

            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//
}
