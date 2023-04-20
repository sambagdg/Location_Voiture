package src.Vues;

import src.Models.Employe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Accueil extends JFrame {
    private JPanel accueilPanel;
    private JButton buttonQuitter;
    private JButton btnClient;
    private JButton btnLocation;
    private JButton btnVoiture;
    private JLabel titreAdmin;
    private JLabel titreOp;
    private JButton btnValidationLoc;
    private JLabel quitter;

    public Accueil(){
        add(accueilPanel);
        setTitle("Accueil");
        setSize(550, 530);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


        Employe emp = Connexion.employe;
        String role = emp.role;
        String role1 = "admin";
        String role2 = "operateur";
        if (Objects.equals(role, role2)){
            btnVoiture.hide();
            titreAdmin.hide();
            btnValidationLoc.hide();
        }else {
            System.out.println("ça marche");
            titreOp.hide();
            btnClient.hide();
            btnLocation.hide();
        }




        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accueil.super.dispose();
                Connexion con = new Connexion();
            }
        });


        btnClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accueil.super.dispose();
                VueClient cl = new VueClient();
            }
        });
        btnLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accueil.super.dispose();
                VueLocation loc = new VueLocation();
            }
        });
        btnVoiture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accueil.super.dispose();
                VueVoiture voi = new VueVoiture();
            }
        });
        btnValidationLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Accueil.super.dispose();
                VueValidLocation lc = new VueValidLocation();
            }
        });
    }
}
