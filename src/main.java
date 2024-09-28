import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        //  des éléments dans le menu "File" avec des icônes redimensionnées
        JMenuItem newFileItem = new JMenuItem("New File", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/new-file.png", 22, 22));
        JMenuItem openFileItem = new JMenuItem("Open File", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/open.png", 22, 22));
        JMenuItem saveFileItem = new JMenuItem("Save File", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/code.png", 22, 22));
        fileMenu.add(newFileItem);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);

        //  menu "Edit" avec des icônes
        JMenuItem undoItem = new JMenuItem("Undo", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/undo.png", 22, 22));
        JMenuItem redoItem = new JMenuItem("Redo", resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/redo.png", 22, 22));
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        //  les menus à la barre de menu
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        //   panneau principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        //  zone de texte
        JTextArea editorArea = new JTextArea();
        editorArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane editorScrollPane = new JScrollPane(editorArea);

        // une console en bas de la fenêtre
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

        //  une barre d'icônes (JToolBar)
        JToolBar toolBar = new JToolBar();
        JButton newFileButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/new-file.png", 22, 22));
        newFileButton.setToolTipText("New File");
        JButton openFileButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/open.png", 22, 22));
        openFileButton.setToolTipText("Open File");
        JButton saveFileButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/save.png", 22, 22));
        saveFileButton.setToolTipText("Save File");
        JButton runButton = new JButton(resizeIcon("C:/Users/HP/IdeaProjects/Projet VM/src/icon/code.png", 22, 22));
        runButton.setToolTipText("Run Code");

        //  des actions pour les boutons de la barre d'icônes
        newFileButton.addActionListener(e -> editorArea.setText(""));  // Nouveau fichier, vide l'éditeur
        openFileButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Open File Clicked"));  // Action pour ouvrir un fichier
        saveFileButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Save File Clicked"));  // Action pour sauvegarder un fichier
        runButton.addActionListener(e -> executeButton.doClick());  // Exécuter le code via le bouton "Run Code"

        //  les boutons à la barre d'outils
        toolBar.add(newFileButton);
        toolBar.add(openFileButton);
        toolBar.add(saveFileButton);
        toolBar.add(runButton);

        //  un panneau pour la console et le bouton "Run Code"
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.add(executeButton, BorderLayout.NORTH);  // Ajouter le bouton "Run Code" au-dessus de la console
        consolePanel.add(consoleScrollPane, BorderLayout.CENTER);  // Ajouter la console en dessous

        //  l'éditeur de texte à la partie centrale
        mainPanel.add(editorScrollPane, BorderLayout.CENTER);

        //  le panneau de la console en bas
        mainPanel.add(consolePanel, BorderLayout.SOUTH);

        //  la barre de menu à la fenêtre
        frame.setJMenuBar(menuBar);

        //  la barre d'outils en haut
        frame.add(toolBar, BorderLayout.NORTH);

        //  le panneau principal à la fenêtre
        frame.add(mainPanel);

        // Afficher la fenêtre
        frame.setVisible(true);
    }

    //  redimensionner une icône
    static ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
