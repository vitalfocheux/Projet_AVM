package fr.m1comp5;

import fr.m1comp5.mjj.InterpreterMjj;
import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.ParseException;
import fr.m1comp5.mjj.generated.SimpleNode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IDE {
    private BorderPane borderPane;
    private MenuBar menuBar;
    private ToolBar toolBar;
    private MenuItem newFile;
    private MenuItem openFile;
    private MenuItem saveFile;
    private MenuItem openFolder;
    private MenuItem undo;
    private MenuItem redo;
    private MenuItem run;
    private TabPane tabPane;
    private Map<Tab, File> fileMap = new HashMap<>();
    private String jjc;
    private TextArea console;
    private String mjj;
    private boolean folderTreeOpened;
    private boolean jjcWindowOpened;

    public IDE() {
        borderPane = new BorderPane();
        menuBar = new MenuBar();
        toolBar = new ToolBar();
        tabPane = new TabPane();
        console = new TextArea();
        folderTreeOpened = false;
        jjcWindowOpened = false;
    }

    public void mainScreen(Stage primaryStage) {
        setTop();

//        terminal.setStyle("-fx-background-color: black; -fx-text-fill: black;");
        console.setEditable(false);
        console.setWrapText(true);

        shortcutAndAction();

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(tabPane, console);
        splitPane.setDividerPositions(0.75);

        //Adding elements to borderpane
        borderPane.setCenter(splitPane);

        //Styling borderpane
        borderPane.setStyle("-fx-background-color: #3e3e42;");
//        BorderPane.setMargin(editor, new javafx.geometry.Insets(10));
//        BorderPane.setMargin(console, new javafx.geometry.Insets(10));

        Scene scene = new Scene(borderPane, 1200, 800);

        primaryStage.setTitle("AVM IDE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMenu() {
        Menu fileMenu = new Menu("File");
        newFile = new MenuItem("New (Ctrl+N)");
        openFile = new MenuItem("Open File (Ctrl+O)");
        saveFile = new MenuItem("Save File (Ctrl+S)");
        openFolder = new MenuItem("Open Folder");
        openFolder.setOnAction(event -> openFolderDialog());
        fileMenu.getItems().addAll(newFile, openFile, saveFile, openFolder);

        Menu editMenu = new Menu("Edit");
        undo = new MenuItem("Undo (Ctrl+Z)");
        redo = new MenuItem("Redo (Ctrl+Y)");
        run = new MenuItem("Run (Shift+F10)");
        editMenu.getItems().addAll(undo, redo, run);

        menuBar.getMenus().addAll(fileMenu,editMenu);
    }
    private void createToolBar() {
        ImageView runIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/run.png"))));
        runIcon.setFitHeight(16);
        runIcon.setFitWidth(16);
        Button runButton = new Button();
        runButton.setGraphic(runIcon);
        runButton.setOnAction(e -> {
            runMjj();System.out.println("oui");
        });

        ImageView undoIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/undo.png"))));
        undoIcon.setFitHeight(16);
        undoIcon.setFitWidth(16);
        Button undoButton = new Button();
        undoButton.setGraphic(undoIcon);
        undoButton.setOnAction(e -> {
            undoMjj();
            System.out.println("Undo button clicked");
        });

        ImageView redoIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/redo.png"))));
        redoIcon.setFitHeight(20);
        redoIcon.setFitWidth(20);
        Button redoButton = new Button();
        redoButton.setGraphic(redoIcon);
        redoButton.setOnAction(e -> {
            redoMjj();
            System.out.println("Redo button clicked");
        });

        runButton.setStyle("-fx-background-color: transparent;");
        undoButton.setStyle("-fx-background-color: transparent;");
        redoButton.setStyle("-fx-background-color: transparent;");

        toolBar.getItems().addAll(runButton,undoButton,redoButton);
    }
    private void setTop() {
        createMenu();
        createToolBar();
        VBox topBar = new VBox(menuBar, toolBar);
        borderPane.setTop(topBar);
    }

    private void addTab(String title, String content) {
        TextArea editor = new TextArea(content);
        editor.setStyle("-fx-background-color: #3e3e42; -fx-text-fill: black;");
        Tab tab = new Tab(title, editor);
        tab.setClosable(true);

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
    private TextArea getActiveEditor() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null && selectedTab.getContent() instanceof TextArea) {
            return (TextArea) selectedTab.getContent();
        }
        return null;
    }

    private void openFolderDialog() {
        TextArea editor = getActiveEditor();
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            TreeView<String> folderTreeView = createFolderTreeView(selectedDirectory); // Creates view for folder tree

            SplitPane innerSplitPane = new SplitPane();
            innerSplitPane.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

            // Creates resizable view for text,folder tree and if needed jjc view
            if (jjcWindowOpened) {
                TextArea rightPanel = new TextArea();
                rightPanel.setText("Right Panel in Center");
                rightPanel.setEditable(false);
                innerSplitPane.getItems().addAll(folderTreeView, editor, rightPanel);
                innerSplitPane.setDividerPositions(0.15,0.7);
            } else {
                innerSplitPane.getItems().addAll(folderTreeView, editor);
                innerSplitPane.setDividerPositions(0.15);
            }
            SplitPane splitPane = new SplitPane();
            splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
            splitPane.getItems().addAll(innerSplitPane, console);
            splitPane.setDividerPositions(0.75);

            borderPane.setCenter(splitPane);
        }
    }

    /**
     * Creates the view for the folder tree
     * @param directory the directory to display
     * @return the tree view of the folder
     */
    private TreeView<String> createFolderTreeView(File directory) {
        TreeItem<String> rootItem = createDirectoryTree(directory);
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setPrefWidth(250);
        return treeView;
    }

    /**
     * Creates the tree of folder/files that is to be used in createFolderTreeView
     * @param directory the directory to search through
     * @return A tree of item (files and folders here) to display
     */
    private TreeItem<String> createDirectoryTree(File directory) {
        TreeItem<String> rootItem = new TreeItem<>(directory.getName());
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    TreeItem<String> folderItem = createDirectoryTree(file);
                    rootItem.getChildren().add(folderItem);
                } else {
                    rootItem.getChildren().add(new TreeItem<>(file.getName()));
                }
            }
        }
        return rootItem;
    }

    private void shortcutAndAction() {
        TextArea editor = getActiveEditor();
        if (editor == null) {
            return;
        }
        // Set button action
        newFile.setOnAction(e -> {
            newFile();
        });
        openFile.setOnAction(e -> {
            openFile();
        });
        saveFile.setOnAction(e -> {
            saveFile();
        });
        undo.setOnAction(event -> {
            if (editor.isUndoable()) {
                undoMjj();
            }
        });
        redo.setOnAction(event -> {
            if (editor.isRedoable()) {
                redoMjj();
            }
        });
        run.setOnAction(event -> {
            runMjj();
        });

        // Set keyboard shortcut
        editor.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                if (editor.isUndoable()) {
                    undoMjj();
                }
                event.consume(); // Consume the event to prevent it from propagating
            }
            else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                if (editor.isRedoable()) {
                    redoMjj();
                }
                event.consume();
            }
            else if (event.isShiftDown() && event.getCode() == KeyCode.F10) {
                runMjj();
            }
            else if (event.isControlDown() && event.getCode() == KeyCode.S) {
                saveFile();
            }
            else if (event.isControlDown() && event.getCode() == KeyCode.O) {
                openFile();
            }
            else if (event.isControlDown() && event.getCode() == KeyCode.N) {
                newFile();
            }
        });
    }
    private void runMjj() {
        TextArea editor = getActiveEditor();
        if (editor == null) {return;}
        mjj = editor.getText();
        MiniJaja mjjTree = new MiniJaja(new ByteArrayInputStream(mjj.getBytes()));
        console.clear();  // Vider la console avant d'exécuter
        try
        {
            SimpleNode n = mjjTree.start();
            InterpreterMjj interpreter = new InterpreterMjj(n);
            console.appendText("Compilation successfull");
            console.appendText(interpreter.interpret());
            n.dump("");
        }
        catch (ParseException pe)
        {
            console.appendText("Syntax error in code");
        }
        catch (Exception exp)
        {
            console.appendText(exp.getMessage());
        }
    }
    private void undoMjj() {
        TextArea editor = getActiveEditor();
        if (editor == null) {return;}
        editor.undo();
    }
    private void redoMjj() {
        TextArea editor = getActiveEditor();
        if (editor == null) {return;}
        editor.redo();
    }
    private void newFile() {
        Tab newTab = new Tab("New file");
        TextArea editor = new TextArea();
        editor.setStyle("-fx-background-color: #3e3e42; -fx-text-fill: black;");
        newTab.setContent(editor);
        newTab.setClosable(true);

        tabPane.getTabs().add(newTab);
    }
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj"),
                new FileChooser.ExtensionFilter("Jajacode Files", "*.jjc"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (FileReader reader = new FileReader(file)) {
                StringBuilder content = new StringBuilder();
                int str;
                while ((str = reader.read()) != -1) {
                    content.append((char) str);
                }

                Tab newTab = new Tab(file.getName());
                TextArea editor = new TextArea(content.toString());
                editor.setStyle("-fx-background-color: #3e3e42; -fx-text-fill: black;");
                newTab.setContent(editor);
                newTab.setClosable(true);

                tabPane.getTabs().add(newTab);
                tabPane.getSelectionModel().select(newTab);

                fileMap.put(newTab, file); // Link the file to the tab
                console.appendText("File opened: " + file.getAbsolutePath() + "\n");
            } catch (IOException ex) {
                console.appendText("Error opening file: " + ex.getMessage() + "\n");
            }
        }
    }
    private void saveFile(){
        Tab activeTab = tabPane.getSelectionModel().getSelectedItem();
        if (activeTab == null) {
            console.appendText("No active tab to save.\n");
            return;
        }

        TextArea activeEditor = (TextArea) activeTab.getContent();
        File file = fileMap.get(activeTab);

        if (file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            file = fileChooser.showSaveDialog(null);
            if (file != null) {
                fileMap.put(activeTab, file); // Link the file to the tab
            } else {
                return;
            }
        }

        // Save the file
        writeToFile(file, activeEditor.getText());
        console.appendText("File saved: " + file.getAbsolutePath() + "\n");
    }
    private void writeToFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            console.appendText("Error saving file: " + e.getMessage() + "\n");
        }
    }
}
