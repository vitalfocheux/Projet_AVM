package fr.m1comp5.UI;

import fr.m1comp5.Analyzer.MiniJaja;
import fr.m1comp5.Analyzer.ParseException;
import fr.m1comp5.Analyzer.SimpleNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class MiniJajaWindow
{
    private static JFrame mainFrame;
    static JMenu fileMenu,editMenu, viewMenu, helpMenu;
    private static JTabbedPane tabbedPane;
    private static JTextArea editorArea, consoleArea;

    private static HashMap<JTextArea, Boolean> unsavedChanges = new HashMap<>(); // Track unsaved changes
    private static HashMap<JTextArea, File> openFiles = new HashMap<>(); // Map each tab's JTextArea to the open file

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

        tabbedPane = new JTabbedPane();
        // Créer une zone de texte
        editorArea = new JTextArea();
        editorArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane editorScrollPane = new JScrollPane(editorArea);

        // Créer une console en bas de la fenêtre
        consoleArea = new JTextArea(8, 40);  // Console avec 8 lignes visibles
        consoleArea.setEditable(false);  // La console ne doit pas être modifiable
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);

        // Bouton pour exécuter le code avec une icône
        ImageIcon runIcon = resizeIcon("", 32, 32);
        JButton executeButton = new JButton("Run Code", runIcon);


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

        //Action Listener pour ouverture fichier
        ActionListener openFileAction = e -> openFile();
        openFileButton.addActionListener(openFileAction);
        openFileItem.addActionListener(openFileAction);

        //Action Listener pour nouveau fichier
        ActionListener newFileAction = e -> newFile();
        newFileButton.addActionListener((newFileAction));
        newFileItem.addActionListener(newFileAction);

        //Action Listener pour sauvegarder fichier
        ActionListener saveFileAction = e -> saveFile();
        saveFileButton.addActionListener(saveFileAction);
        saveFileItem.addActionListener(saveFileAction);

        //Action Listener pour lancer l'execution
        ActionListener runCodeAction = e -> runCode();
        executeButton.addActionListener(runCodeAction);
        runButton.addActionListener(runCodeAction);
        //runButton.addActionListener(e -> executeButton.doClick());  // Exécuter le code via le bouton "Run Code"

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
        mainFrame.add(consolePanel, BorderLayout.SOUTH);

        // Ajouter la barre d'icônes en haut
        mainFrame.add(toolBar, BorderLayout.NORTH);

        // Ajouter le panneau principal à la fenêtre
        //mainFrame.add(mainPanel);
        tabbedPane.add(mainPanel);
        tabbedPane.setTabComponentAt(0, new JLabel("Sans titre"));
        mainFrame.add(tabbedPane, BorderLayout.CENTER);

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

    //open file function
    private static void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setMultiSelectionEnabled(true); // Enable selecting multiple files
    int option = fileChooser.showOpenDialog(null);

    if (option == JFileChooser.APPROVE_OPTION) {
        File[] files = fileChooser.getSelectedFiles(); // Get all selected files
        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                JTextArea textArea = new JTextArea();
                textArea.read(reader, null);
                JScrollPane scrollPane = new JScrollPane(textArea);
                tabbedPane.addTab(file.getName(), scrollPane);
                int tabIndex = tabbedPane.getTabCount() - 1;

                // Set custom tab with close button
                tabbedPane.setTabComponentAt(tabIndex, createTabHeader(file.getName(), textArea));

                // Track open files and changes
                openFiles.put(textArea, file);
                unsavedChanges.put(textArea, false);

                // Add document listener to track changes in the JTextArea
                textArea.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        unsavedChanges.put(textArea, true);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        unsavedChanges.put(textArea, true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        unsavedChanges.put(textArea, true);
                    }
                });

            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Error opening file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    // Create custom tab header with close button
    private static JPanel createTabHeader(String title, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(title);
        panel.add(label, BorderLayout.CENTER);

        JButton closeButton = new JButton("X");
        closeButton.setMargin(new Insets(0, 0, 0, 0)); // Remove extra padding
        closeButton.setPreferredSize(new Dimension(15,15));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tabIndex = tabbedPane.indexOfComponent(textArea.getParent().getParent());

                if (unsavedChanges.get(textArea)) {
                    int option = JOptionPane.showConfirmDialog(mainFrame, "Do you want to save changes?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        saveFile();
                    } else if (option == JOptionPane.CANCEL_OPTION) {
                        return; // Don't close the tab
                    }
                }

                // Remove the tab and clean up
                tabbedPane.remove(tabIndex);
                unsavedChanges.remove(textArea);
                openFiles.remove(textArea);
            }
        });

        panel.add(closeButton, BorderLayout.EAST);
        return panel;
    }

    public static void newFile() {
        // Use JFileChooser to allow the user to create a new file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Create a New File");
        int userSelection = fileChooser.showSaveDialog(mainFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File newFile = fileChooser.getSelectedFile();

            try {
                if (newFile.createNewFile()) {
                    JOptionPane.showMessageDialog(mainFrame, "New file created: " + newFile.getName());

                    // Open the new file in a new tab after it's created
                    openFileNamed(newFile); // Call the overloaded openFile method

                } else {
                    JOptionPane.showMessageDialog(mainFrame, "File already exists.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error creating file.");
            }
        }
    }

    // Overload the openFile() method to accept a File argument
    public static void openFileNamed(File file) {
        try (FileReader reader = new FileReader(file)) {
            JTextArea textArea = new JTextArea();
            textArea.read(reader, null);
            JScrollPane scrollPane = new JScrollPane(textArea);

            tabbedPane.addTab(file.getName(), scrollPane);
            int tabIndex = tabbedPane.getTabCount() - 1;
            tabbedPane.setTabComponentAt(tabIndex, createTabHeader(file.getName(), textArea));

            // Track the file and unsaved changes
            openFiles.put(textArea, file);
            unsavedChanges.put(textArea, false);

            // Track changes to detect unsaved changes
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    unsavedChanges.put(textArea, true);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    unsavedChanges.put(textArea, true);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    unsavedChanges.put(textArea, true);
                }
            });
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save the current active tab content
    private static void saveFile() {
        if (tabbedPane.getTabCount() == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No open files to save!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedTab = tabbedPane.getSelectedIndex(); // Get the current active tab
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(selectedTab);
        JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();

        File file = openFiles.get(textArea);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(textArea.getText());
                unsavedChanges.put(textArea, false); // Reset the unsaved changes flag
                JOptionPane.showMessageDialog(mainFrame, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                openFiles.put(textArea, file);
                saveFile(); // Recursive call to save to the chosen file
            }
        }
    }

    public void runCode() {
        consoleArea.setText("");
        int selectedIndex= tabbedPane.getSelectedIndex();
        Component tabContent =  tabbedPane.getComponentAt(selectedIndex);
        String code;

        if (tabContent instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) tabContent;
            JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();

            // Return the content of the JTextArea
            code = textArea.getText();
        } else {
            code = editorArea.getText();
        }

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
            consoleArea.append("Syntax error in code");
        }
        catch (Exception exp)
        {
            consoleArea.append(exp.getMessage());
        }

    }
}

