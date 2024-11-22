package fr.m1comp5.ui;

//import fr.m1comp5.mjj.generated.MiniJaja;
//import fr.m1comp5.mjj.generated.ParseException;
//import fr.m1comp5.mjj.generated.SimpleNode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.ByteArrayInputStream;
import java.io.File;

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
    private TextArea editor;
    private String jjc;
    private TextArea console;
    private String mjj;
    private boolean folderTreeOpened;
    private boolean jjcWindowOpened;

    public IDE() {
        borderPane = new BorderPane();
        menuBar = new MenuBar();
        toolBar = new ToolBar();
        editor = new TextArea();
        console = new TextArea();
        folderTreeOpened = false;
        jjcWindowOpened = false;
    }

    public void mainScreen(Stage primaryStage) {
        setTop();

        editor.setStyle("-fx-background-color: #3e3e42; -fx-text-fill: black;");

//        terminal.setStyle("-fx-background-color: black; -fx-text-fill: black;");
        console.setEditable(false);
        console.setWrapText(true);

        shortcutAndAction();

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(editor, console);
        splitPane.setDividerPositions(0.75);

        //Adding elements to borderpane
        borderPane.setCenter(splitPane);

        //Styling borderpane
        borderPane.setStyle("-fx-background-color: #3e3e42;");
        BorderPane.setMargin(editor, new javafx.geometry.Insets(10));
        BorderPane.setMargin(console, new javafx.geometry.Insets(10));

        Scene scene = new Scene(borderPane, 1200, 800);

        primaryStage.setTitle("AVM IDE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMenu() {
        Menu fileMenu = new Menu("File");
        ImageView newFileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/new-file.png")));
        newFileIcon.setFitHeight(16);
        newFileIcon.setFitWidth(16);
        newFile = new MenuItem("New"/*, newFileIcon*/);
        openFile = new MenuItem("Open File");
        saveFile = new MenuItem("Save File");
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
        ImageView runIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/run.png")));
        runIcon.setFitHeight(16);
        runIcon.setFitWidth(16);
        Button runButton = new Button();
        runButton.setGraphic(runIcon);
        runButton.setOnAction(e -> {runMjj();});

        ImageView undoIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/undo.png")));
        runIcon.setFitHeight(16);
        runIcon.setFitWidth(16);
        Button undoButton = new Button();
        runButton.setGraphic(runIcon);
        runButton.setOnAction(e -> {undoMjj();});

        ImageView redoIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/redo.png")));
        runIcon.setFitHeight(16);
        runIcon.setFitWidth(16);
        Button redoButton = new Button();
        runButton.setGraphic(runIcon);
        runButton.setOnAction(e -> {redoMjj();});

        toolBar.getItems().addAll(runButton,undoButton,redoButton);
    }
    private void setTop() {
        createMenu();
        createToolBar();
        VBox topBar = new VBox(menuBar, toolBar);
        borderPane.setTop(topBar);
    }

    private void openFolderDialog() {
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
        // Set button action
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
            } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                if (editor.isRedoable()) {
                    redoMjj();
                }
                event.consume();
            } else if (event.isShiftDown() && event.getCode() == KeyCode.F10) {
                runMjj();
            }
        });
    }
    private void runMjj() {
        mjj = editor.getText();
        console.appendText(mjj);
//        MiniJaja mjjTree = new MiniJaja(new ByteArrayInputStream(mjj.getBytes()));
//        console.setText("");  // Vider la console avant d'exécuter
//        try
//        {
//            SimpleNode n = mjjTree.start();
//            console.appendText("Compilation successfull");
//            n.dump("");
//        }
//        catch (ParseException pe)
//        {
//            console.appendText("Syntax error in code");
//        }
//        catch (Exception exp)
//        {
//            console.appendText(exp.getMessage());
//        }
    }
    private void undoMjj() {
        editor.undo();
    }
    private void redoMjj() {
        editor.redo();
    }
}
