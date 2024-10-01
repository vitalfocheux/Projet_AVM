import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Minijaja Compiler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Créer les menus
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");

        // Ajouter des éléments dans le menu "File" avec des icônes redimensionnées
        JMenuItem newFileItem = new JMenuItem("New File", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/new-file.png", 22, 22));
        JMenuItem openFileItem = new JMenuItem("Open File", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/open.png", 22, 22));
        JMenuItem saveFileItem = new JMenuItem("Save File", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/save.png", 22, 22));
        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);

        // Ajouter des éléments dans le menu "Edit" avec des icônes
        JMenuItem undoItem = new JMenuItem("Undo", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/undo.png", 22, 22));
        JMenuItem redoItem = new JMenuItem("Redo", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/redo.png", 22, 22));
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
        ImageIcon runIcon = resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/code.png", 32, 32);
        JButton executeButton = new JButton("Run Code", runIcon);

        // Action du bouton "Run Code"
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = editorArea.getText();  // Récupérer le code de l'éditeur
                consoleArea.setText("");  // Vider la console avant d'exécuter

                // Simuler l'exécution et afficher le résultat dans la console
                consoleArea.append("Executing the following code:\n");
                consoleArea.append(code + "\n\n");
                consoleArea.append("Compilation successful!\n");
                consoleArea.append("Output:\nHello World!\n");
            }
        });

        // Créer une barre d'icônes (JToolBar)
        JToolBar toolBar = new JToolBar();
        JButton newFileButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/new-file.png", 22, 22));
        newFileButton.setToolTipText("New File");
        JButton openFileButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/open.png", 22, 22));
        openFileButton.setToolTipText("Open File");
        JButton saveFileButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/save.png", 22, 22));
        saveFileButton.setToolTipText("Save File");
        JButton runButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/code.png", 22, 22));
        runButton.setToolTipText("Run Code");

        // Ajouter des actions pour les boutons de la barre d'icônes
        newFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Utiliser JFileChooser pour permettre à l'utilisateur de créer un nouveau fichier
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Create a New File");
                int userSelection = fileChooser.showSaveDialog(frame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File newFile = fileChooser.getSelectedFile();

                    try {
                        if (newFile.createNewFile()) {
                            JOptionPane.showMessageDialog(frame, "New file created: " + newFile.getName());
                        } else {
                            JOptionPane.showMessageDialog(frame, "File already exists.");
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error creating file.");
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

                int userSelection = fileChooser.showOpenDialog(frame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = fileChooser.getSelectedFile();

                    try (FileReader reader = new FileReader(fileToOpen)) {
                        editorArea.read(reader, fileToOpen);
                        JOptionPane.showMessageDialog(frame, "Opened file: " + fileToOpen.getName());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error opening file.");
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

                int userSelection = fileChooser.showSaveDialog(frame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String path = fileToSave.getAbsolutePath();

                    // Vérifie si le fichier a l'extension .mjj, sinon l'ajoute
                    if (!path.endsWith(".mjj")) {
                        fileToSave = new File(path + ".mjj");
                    }

                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        editorArea.write(writer);
                       /* JOptionPane.showMessageDialog(frame, "File saved: " + fileToSave.getName()); */
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error saving file.");
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
        frame.add(toolBar, BorderLayout.NORTH);

        // Ajouter le panneau principal à la fenêtre
        frame.add(mainPanel);

        // Ajouter la barre de menu
        frame.setJMenuBar(menuBar);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }

    // Méthode pour redimensionner une icône
    public static ImageIcon resizeIcon(String iconPath, int width, int height) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
