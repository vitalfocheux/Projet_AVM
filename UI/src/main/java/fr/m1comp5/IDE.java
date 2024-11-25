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
    private TabPane editorsPane;
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
        editorsPane = new TabPane();

        console = new TextArea();
        console.setEditable(false);
        console.setWrapText(true);

        folderTreeOpened = false;
        jjcWindowOpened = false;
        setStyle();
    }

    public void setStyle() {
        borderPane.setStyle("-fx-background-color: #3e3e42;");
        editorsPane.setStyle("-fx-background-color: #3e3e42;");
        console.setStyle("-fx-control-inner-background: #3e3e42; -fx-text-fill: white;");

        menuBar.setStyle("-fx-background-color: #F0EBE4B3;");
        toolBar.setStyle("-fx-background-color: #252526;");
    }

    public void mainScreen(Stage primaryStage) {
        setTop();
        setCenter();

        shortcutAndAction();

        // Creates the window
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
        openFolder = new MenuItem("Open Folder (Ctrl+D)");
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
            runMjj();
        });

        ImageView undoIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/undo.png"))));
        undoIcon.setFitHeight(16);
        undoIcon.setFitWidth(16);
        Button undoButton = new Button();
        undoButton.setGraphic(undoIcon);
        undoButton.setOnAction(e -> {
            undoMjj();
        });

        ImageView redoIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/redo.png"))));
        redoIcon.setFitHeight(16);
        redoIcon.setFitWidth(16);
        Button redoButton = new Button();
        redoButton.setGraphic(redoIcon);
        redoButton.setOnAction(e -> {
            redoMjj();
        });

        ImageView folderIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon/folder.png"))));
        folderIcon.setFitHeight(16);
        folderIcon.setFitWidth(16);
        Button folderButton = new Button();
        folderButton.setGraphic(folderIcon);
        folderButton.setOnAction(e -> {
            if (folderTreeOpened) {
                closeFolder();
            } else {
                openFolderDialog();
            }
        });

        runButton.setStyle("-fx-background-color: transparent;");
        undoButton.setStyle("-fx-background-color: transparent;");
        redoButton.setStyle("-fx-background-color: transparent;");
        folderButton.setStyle("-fx-background-color: transparent;");

        toolBar.getItems().addAll(runButton,undoButton,redoButton,folderButton);
    }
    /**
     * Set elements at the top of the window (editor + console)
     */
    private void setTop() {
        createMenu();
        createToolBar();
        VBox topBar = new VBox(menuBar, toolBar);
        borderPane.setTop(topBar);
    }
    /**
     * Set elements at the center of the window (editor + console)
     */
    private void setCenter() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(editorsPane, console);
        splitPane.setDividerPositions(0.75);
        splitPane.setStyle("-fx-background-color: #3e3e42;");
        //Adding elements to borderpane
        borderPane.setCenter(splitPane);
    }

    private void addTab(String title, String content, File file) {
        TextArea editor = new TextArea(content);
        Tab tab = new Tab(title, editor);
        tab.setClosable(true);

        editorsPane.getTabs().add(tab);
        editorsPane.getSelectionModel().select(tab);
        editor.setStyle("-fx-control-inner-background: #3e3e42; -fx-text-fill: white;");
        editorsPane.setStyle("-fx-control-inner-background: #3e3e42; -fx-text-fill: white;");
        fileMap.put(tab,file);
    }
    private TextArea getActiveEditor() {
        Tab selectedTab = editorsPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null && selectedTab.getContent() instanceof TextArea) {
            return (TextArea) selectedTab.getContent();
        }
        return null;
    }

    private void shortcutAndAction() {
        // Set button action
        newFile.setOnAction(e -> {newFile();});
        openFile.setOnAction(e -> {openFile();});
        saveFile.setOnAction(e -> {saveFile();});
        openFolder.setOnAction(e -> {openFolderDialog();});
        undo.setOnAction(event -> {
            TextArea editor = getActiveEditor();
            if (editor != null) {undoMjj();}});
        redo.setOnAction(event -> {
            TextArea editor = getActiveEditor();
            if (editor != null) {redoMjj();}});
        run.setOnAction(event -> {runMjj();});

        if (getActiveEditor() != null) {
            keyBindingsEditor(getActiveEditor());
        }
        keyBindingsWindow();
    }
    private void keyBindingsWindow()  {
        borderPane.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                switch (e.getCode()) {
                    case S: saveFile();break;
                    case O: openFile();break;
                    case N: newFile();break;
                    case TAB: switchTab();break;
                    case D: openFolderDialog();break;
                }
            }
        });
    }
    private void keyBindingsEditor(TextArea editor)  {
        editor.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.Z) {
                undoMjj();
                e.consume(); // Avoid propagation of event
            }
            else if (e.isControlDown() && e.getCode() == KeyCode.Y) {
                redoMjj();
                e.consume();
            }
            else if (e.isShiftDown() && e.getCode() == KeyCode.F10) {
                System.out.println("zouzou");
                runMjj();
            }
        });
    }

    private void switchTab() {
        int ctr = editorsPane.getTabs().size();
        if (ctr > 1) {
            int id = editorsPane.getSelectionModel().getSelectedIndex();
            int next = (id+1) % ctr;
            editorsPane.getSelectionModel().select(next);
        }
    }

    private void newFile() {
        addTab("New file", "", null);
    }
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj"),
                new FileChooser.ExtensionFilter("Jajacode Files", "*.jjc"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(editorsPane.getScene().getWindow()); // Pass correct context
        if (file != null) {
            try (FileReader reader = new FileReader(file)) {
                StringBuilder content = new StringBuilder();
                int str;
                while ((str = reader.read()) != -1) {
                    content.append((char) str);
                }
                addTab(file.getName(), content.toString(), file);
            } catch (IOException ex) {
                console.appendText("Error opening file: " + ex.getMessage() + "\n");
            }
        }
    }
    private void saveFile(){
        Tab activeTab = editorsPane.getSelectionModel().getSelectedItem();
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

    private void openFolderDialog() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            TreeView<String> folderTreeView = createFolderTreeView(selectedDirectory);
            folderTreeView.setStyle("-fx-control-inner-background: #3e3e42; -fx-text-fill: white;");

            SplitPane innerSplitPane = new SplitPane();
            innerSplitPane.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

            // Creates resizable view for text,folder tree and if needed jjc view
            if (jjcWindowOpened) {
                TextArea rightPanel = new TextArea();
                rightPanel.setText("Right Panel in Center");
                rightPanel.setEditable(false);
                innerSplitPane.getItems().addAll(folderTreeView, editorsPane, rightPanel);
                innerSplitPane.setDividerPositions(0.15,0.7);
            }
            else {
                innerSplitPane.getItems().addAll(folderTreeView, editorsPane);
                innerSplitPane.setDividerPositions(0.15);
            }
            // Creates new center layout
            SplitPane splitPane = new SplitPane();
            splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
            splitPane.getItems().addAll(innerSplitPane, console);
            splitPane.setDividerPositions(0.75);
            // Updates view
            borderPane.setCenter(splitPane);

            folderTreeOpened = true;
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
    private void closeFolder() {
        SplitPane innerSplitPane = new SplitPane();
        innerSplitPane.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        if (jjcWindowOpened) {
            TextArea rightPanel = new TextArea();
            rightPanel.setText("Right Panel in Center");
            rightPanel.setEditable(false);
            innerSplitPane.getItems().addAll(editorsPane, rightPanel);
            innerSplitPane.setDividerPositions(0.7);
        }
        else {
            innerSplitPane.getItems().addAll(editorsPane);
        }
        // Creates new center layout without folder tree view
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(innerSplitPane, console);
        splitPane.setDividerPositions(0.75);
        // Updates view
        borderPane.setCenter(splitPane);
        folderTreeOpened = false;
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
}
