package fr.m1comp5.UI;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class JavaFxIDE {
    private BorderPane borderPane;
    private MenuBar menuBar;
    private MenuItem newFile;
    private MenuItem openFile;
    private MenuItem saveFile;
    private MenuItem openFolder;
    private MenuItem undo;
    private MenuItem redo;
    private MenuItem run;
    private TextArea editor;
    private TextArea terminal;
    private String text;
    private boolean folderTreeOpened;
    private boolean jjcWindowOpened;

    public JavaFxIDE() {
        borderPane = new BorderPane();
        menuBar = new MenuBar();
        editor = new TextArea();
        terminal = new TextArea();
        folderTreeOpened = false;
        jjcWindowOpened = true;
    }

    public void mainScreen(Stage primaryStage) {
        addMenus(menuBar);

        editor.setStyle("-fx-background-color: #3e3e42; -fx-text-fill: black;");

//        terminal.setStyle("-fx-background-color: black; -fx-text-fill: black;");
        terminal.setEditable(false);
        terminal.setWrapText(true);

        TextListener(undo, redo, run, editor);

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(editor, terminal);
        splitPane.setDividerPositions(0.75);

        //Adding elements to borderpane
        borderPane.setTop(menuBar);
        borderPane.setCenter(splitPane);

        //Styling borderpane
        borderPane.setStyle("-fx-background-color: #3e3e42;");
        BorderPane.setMargin(editor, new javafx.geometry.Insets(10));
        BorderPane.setMargin(terminal, new javafx.geometry.Insets(10));

        Scene scene = new Scene(borderPane, 1200, 800);

        primaryStage.setTitle("AVM IDE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addMenus(MenuBar menuBar) {
        Menu fileMenu = new Menu("File");
//        ImageView newFileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/new-file.png")));
//        newFileIcon.setFitHeight(16);
//        newFileIcon.setFitWidth(16);
        newFile = new MenuItem("New"/*, newFileIcon*/);
        openFile = new MenuItem("Open File");
        saveFile = new MenuItem("Save File");
        openFolder = new MenuItem("Open Folder");
        openFolder.setOnAction(event -> openFolderDialog());
        fileMenu.getItems().addAll(newFile, openFile, saveFile, openFolder);

        Menu editMenu = new Menu("Edit");
        undo = new MenuItem("Undo");
        redo = new MenuItem("Redo");
        run = new MenuItem("Run");
        editMenu.getItems().addAll(undo, redo, run);

        menuBar.getMenus().addAll(fileMenu,editMenu);
    }

    private void openFolderDialog() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            TreeView<String> folderTreeView = createFolderTreeView(selectedDirectory);

            SplitPane innerSplitPane = new SplitPane();
            innerSplitPane.setOrientation(javafx.geometry.Orientation.HORIZONTAL);

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
            splitPane.getItems().addAll(innerSplitPane, terminal);
            splitPane.setDividerPositions(0.75);

            borderPane.setCenter(splitPane);
        }
    }

    private TreeView<String> createFolderTreeView(File directory) {
        TreeItem<String> rootItem = createDirectoryTree(directory);
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setPrefWidth(250);
        return treeView;
    }

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

    private void TextListener(MenuItem undo, MenuItem redo, MenuItem runCode, TextArea editor) {
        undo.setOnAction(event -> {
            if (editor.isUndoable()) {
                editor.undo();
            }
        });

        redo.setOnAction(event -> {
            if (editor.isRedoable()) {
                editor.redo();
            }
        });

        run.setOnAction(event -> {
            text = editor.getText();
            terminal.appendText(text);
        });

        editor.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                if (editor.isUndoable()) {
                    editor.undo();
                }
                event.consume(); // Consume the event to prevent it from propagating
            } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                if (editor.isRedoable()) {
                    editor.redo();
                }
                event.consume(); // Consume the event to prevent it from propagating
            } else if (event.isControlDown() && event.getCode() == KeyCode.ENTER) {
                text = editor.getText();
                terminal.appendText(text);
            }
        });
    }
}
