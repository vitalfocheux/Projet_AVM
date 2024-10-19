package fr.m1comp5.UI;

import fr.m1comp5.Analyzer.mjj.MiniJaja;
import fr.m1comp5.Analyzer.mjj.ParseException;
import fr.m1comp5.Analyzer.mjj.SimpleNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MiniJajaWindow
{
    JFrame mainFrame;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu viewMenu;
    JMenu helpMenu;

    public MiniJajaWindow()
    {
        // Créer la fenêtre principale
        mainFrame = new JFrame("Minijaja Compiler");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Créer les menus
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        // Ajouter des éléments dans le menu "File" avec des icônes redimensionnées
        JMenuItem newFileItem = new JMenuItem("New File", resizeIcon("/icon/new-file.png", 22, 22));
        JMenuItem openFileItem = new JMenuItem("Open File", resizeIcon("/icon/open.png", 22, 22));
        JMenuItem saveFileItem = new JMenuItem("Save File", resizeIcon("/icon/save.png", 22, 22));
        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);

        // Ajouter des éléments dans le menu "Edit" avec des icônes
        JMenuItem undoItem = new JMenuItem("Undo", resizeIcon("/icon/undo.png", 22, 22));
        JMenuItem redoItem = new JMenuItem("Redo", resizeIcon("/icon/redo.png", 22, 22));
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        // Ajouter les menus à la barre de menu
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // Créer un panneau principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Créer une zone de texte
        JTextArea editorArea = new JTextArea();
        editorArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane editorScrollPane = new JScrollPane(editorArea);

        // Créer une console en bas de la fenêtre
        JTextArea consoleArea = new JTextArea(8, 40);  // Console avec 8 lignes visibles
        consoleArea.setEditable(false);  // La console ne doit pas être modifiable
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);

        // Bouton pour exécuter le code avec une icône
        ImageIcon runIcon = resizeIcon("", 32, 32);
        JButton executeButton = new JButton("Run Code", runIcon);

        // Action du bouton "Run Code"
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = editorArea.getText(); // Récupérer le code de l'éditeur
                MiniJaja mjj = new MiniJaja(new ByteArrayInputStream(code.getBytes()));
                consoleArea.setText("");  // Vider la console avant d'exécuter
                try
                {
                    SimpleNode n = mjj.start();
                    consoleArea.append("Compilation successfull");
                    n.dump("");
                }
                catch (ParseException pe)
                {
                    consoleArea.append("Error syntax in the code");
                }
                catch (Exception exp)
                {
                    consoleArea.append(exp.getMessage());
                }

            }
        });

        // Créer une barre d'icônes (JToolBar)
        JToolBar toolBar = new JToolBar();
        JButton newFileButton = new JButton(resizeIcon("/icon/new-file.png", 22, 22));
        newFileButton.setToolTipText("New File");
        JButton openFileButton = new JButton(resizeIcon("/icon/open.png", 22, 22));
        openFileButton.setToolTipText("Open File");
        JButton saveFileButton = new JButton(resizeIcon("/icon/save.png", 22, 22));
        saveFileButton.setToolTipText("Save File");
        JButton runButton = new JButton(resizeIcon("/icon/code.png", 22, 22));
        runButton.setToolTipText("Run Code");

        // Ajouter des actions pour les boutons de la barre d'icônes
        newFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Utiliser JFileChooser pour permettre à l'utilisateur de créer un nouveau fichier
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create a New File");
                int userSelection = fileChooser.showSaveDialog(mainFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File newFile = fileChooser.getSelectedFile();

                    try {
                        if (newFile.createNewFile()) {
                            JOptionPane.showMessageDialog(mainFrame, "New file created: " + newFile.getName());
                        } else {
                            JOptionPane.showMessageDialog(mainFrame, "File already exists.");
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Error creating file.");
                    }
                }
            }
        });

        // Action pour ouvrir un fichier avec l'extension .mjj
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open File");

                // Filtre pour les fichiers avec l'extension .mjj
                FileNameExtensionFilter filter = new FileNameExtensionFilter("MiniJaja Files", "mjj");
                fileChooser.setFileFilter(filter);

                int userSelection = fileChooser.showOpenDialog(mainFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = fileChooser.getSelectedFile();

                    try (FileReader reader = new FileReader(fileToOpen)) {
                        editorArea.read(reader, fileToOpen);
                        JOptionPane.showMessageDialog(mainFrame, "Opened file: " + fileToOpen.getName());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Error opening file.");
                    }
                }
            }
        });

        // Action pour enregistrer un fichier
        saveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save File");

                // Filtre pour enregistrer avec l'extension .mjj
                FileNameExtensionFilter filter = new FileNameExtensionFilter("MiniJaja Files", "mjj");
                fileChooser.setFileFilter(filter);

                int userSelection = fileChooser.showSaveDialog(mainFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String path = fileToSave.getAbsolutePath();

                    // Vérifie si le fichier a l'extension .mjj, sinon l'ajoute
                    if (!path.endsWith(".mjj")) {
                        fileToSave = new File(path + ".mjj");
                    }

                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        editorArea.write(writer);
                        /* JOptionPane.showMessageDialog(mainFrame, "File saved: " + fileToSave.getName()); */
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Error saving file.");
                    }
                }
            }
        });

        runButton.addActionListener(e -> executeButton.doClick());  // Exécuter le code via le bouton "Run Code"

        // Ajouter les boutons à la barre d'outils
        toolBar.add(newFileButton);
        toolBar.add(openFileButton);
        toolBar.add(saveFileButton);
        toolBar.add(runButton);

        // Créer un panneau pour la console et le bouton "Run Code"
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.add(executeButton, BorderLayout.NORTH);  // Ajouter le bouton "Run Code" au-dessus de la console
        consolePanel.add(consoleScrollPane, BorderLayout.CENTER);  // Ajouter la console en dessous

        // Ajouter l'éditeur de texte à la partie centrale
        mainPanel.add(editorScrollPane, BorderLayout.CENTER);

        // Ajouter le panneau de la console en bas
        mainPanel.add(consolePanel, BorderLayout.SOUTH);

        // Ajouter la barre d'icônes en haut
        mainFrame.add(toolBar, BorderLayout.NORTH);

        // Ajouter le panneau principal à la fenêtre
        mainFrame.add(mainPanel);

        // Ajouter la barre de menu
        mainFrame.setJMenuBar(menuBar);
    }

    public void showWindow()
    {
        mainFrame.setVisible(true);
    }

    public void hideWindow()
    {
        mainFrame.setVisible(false);
    }

    private ImageIcon resizeIcon(String iconPath, int width, int height)
    {
        System.out.println(iconPath);
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath).getPath());
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
