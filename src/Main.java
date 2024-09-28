import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {

        // Main frame
        JFrame frame = new JFrame("Simple Swing Layout");
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

        // crée les boutons
        JButton buttonParcourir = new JButton("Parcourir");

        // Chargement de l'icône pour le bouton "Exécuter"
        ImageIcon iconExec = new ImageIcon("icon/run.png");

        // Redimensionner l'icône
        Image image = iconExec.getImage();  // Récupérer l'image de l'icône
        Image resizedImage = image.getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Créer le bouton "Exécuter" avec l'icône redimensionnée
        JButton buttonExec = new JButton(resizedIcon);

        // Panel contenant les boutons alignés verticalement
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(buttonParcourir);
        buttonPanel.add(Box.createVerticalStrut(10));  // Ajoute un espace entre les boutons
        buttonPanel.add(buttonExec);

        // Ajout d'une action pour le bouton "Exécuter"
        buttonExec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Afficher la console dans un JDialog
                showConsoleDialog(frame);
            }
        });

        // Ajout des composants à la frame principale
        frame.setJMenuBar(menuBar);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    // Méthode pour afficher la console dans un JDialog
    private static void showConsoleDialog(JFrame parentFrame) {
        // Créer un JDialog modal attaché à la fenêtre principale
        JDialog consoleDialog = new JDialog(parentFrame, "Console", true);
        consoleDialog.setSize(400, 300);  // Définir la taille de la console

        // Ajouter une zone de texte pour simuler la sortie console
        JTextArea consoleTextArea = new JTextArea("Résultat de l'exécution...\n");
        consoleTextArea.setEditable(false);  // Empêcher l'édition du texte
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);

        // Simuler l'ajout d'un résultat d'exécution
        consoleTextArea.append("Votre programme a été exécuté avec succès.\n");

        // Ajout de la zone de texte à la fenêtre de la console
        consoleDialog.add(scrollPane, BorderLayout.CENTER);

        // Rendre la console visible
        consoleDialog.setVisible(true);
    }
}
