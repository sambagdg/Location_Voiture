package src.Vues;

import src.Models.Employe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Connexion extends JFrame {
    private JTextField txtUser;
    private JTextField txtMdp;
    private JButton meConnecterButton;
    private JButton annulerButton;
    private JPanel ConPanel;


    public Connexion(){
        add(ConPanel);
        setTitle("client");
        setSize(550, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        meConnecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pseudo = txtUser.getText();
                String mdp = String.valueOf(txtMdp.getText());

                employe = verifAuthentification(pseudo, mdp);

                Employe employe = Connexion.employe;
                if (employe != null){
                    Connexion.super.dispose();
                    Accueil accueil = new Accueil();

                } else if (txtUser.getText().isEmpty()|| txtMdp.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Rempli tous les champs mec...");
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Tu t'es trompé mec, réessaye...");
                }
            }

        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }

        });
        setVisible(true);
    }
    public static Employe employe;
    private Employe verifAuthentification(String pseudo , String mdp){
        Employe employe = null;

        final String URL = "jdbc:mysql://localhost:8889/location";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM EMPLOYE WHERE pseudo = ? AND mdp = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, pseudo);
            pst.setString(2, mdp);

            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                employe = new Employe();
                employe.numEmploye = rs.getString("numEmploye");
                employe.pseudo = rs.getString("pseudo");
                employe.mdp = rs.getString("mdp");
                employe.role = rs.getString("role");
            }
            stmt.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return employe;
    }

}
