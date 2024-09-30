package fr.m1comp5;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // Main frame
        JFrame frame = new JFrame("MJJ EDITOR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // barre menu
        JMenuBar menuBar = new JMenuBar();

        // slots menu
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        // Ajt slot a barre menu
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);


        // zone texte
        JTextArea textArea = new JTextArea("Ecrivez votre programme...");

        // cree les boutons
        JButton buttonParcourir = new JButton("Parcourir");
        JButton buttonExec = new JButton("Exécuter");

        // Panel contenant les boutons aligné verticalement
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(buttonParcourir);
        buttonPanel.add(Box.createVerticalStrut(10));  // Add some space between buttons
        buttonPanel.add(buttonExec);

        // ajt les componants a la main frame
        frame.setJMenuBar(menuBar);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
