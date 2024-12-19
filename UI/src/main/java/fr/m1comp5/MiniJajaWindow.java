package fr.m1comp5;


import fr.m1comp5.Logger.AppLogger;
import fr.m1comp5.Logger.TestLoggerListener;
import fr.m1comp5.Typechecker.TypeChecker;
import fr.m1comp5.jjc.InterpreterJjc;
import fr.m1comp5.jjc.generated.Node;
import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.ParseException;
import fr.m1comp5.mjj.generated.SimpleNode;
import fr.m1comp5.mjj.InterpreterMjj;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.flowless.VirtualizedScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import javafx.util.Pair;
;



public class MiniJajaWindow extends Application {
    private Stage primaryStage;
    private CodeArea codeArea;
    private TextArea consoleArea;
    private File currentFile;
    private boolean isModified;
    private CodeArea jajaCodeArea;
    private SplitPane mainSplitPane;
    private VBox jajaCodeWrapper;
    private SplitPane mainContentSplitPane;
    private TreeView<File> fileExplorer;
    private HBox mainContainer;
    private TabPane editorTabPane;
    private Map<Tab, File> tabFileMap = new HashMap<>();
    private Scene scene;
    private List<Node> instrs;
    private boolean isCompiled = false;
    private boolean folderTreeOpened = false;
    private TestLoggerListener loggerListener = new TestLoggerListener();




    private static final Pattern KEYWORDS = Pattern.compile("\\b(class|extends|void|int|boolean|if|else|while|return|true|false|main|writeln)\\b");
    private static final Pattern PARENTHESES = Pattern.compile("[()]");
    private static final Pattern BRACES = Pattern.compile("[{}]");
    private static final Pattern BRACKETS = Pattern.compile("[\\[\\]]");
    private static final Pattern SEMICOLON = Pattern.compile(";");
    private static final Pattern STRING_PATTERN = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("//[^\n]*|/\\*(.|\\R)*?\\*/");
    private static final Pattern NUMBERS = Pattern.compile("\\b\\d+\\b");
    private static final Pattern OPERATORS = Pattern.compile("[+\\-*/<>=!&|^~]");
    private static final Pattern JAJACODE_INSTRUCTIONS = Pattern.compile("\\b(push|pop|load|store|add|sub|mul|div|not|and|or|neg|cmp|sup|jmp|jcmp)\\b");

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        int lastKwEnd = 0;

        List<Pair<String, Pattern>> patterns = Arrays.asList(
                new Pair<>("keyword", KEYWORDS),
                new Pair<>("string", STRING_PATTERN),
                new Pair<>("comment", COMMENT_PATTERN),
                new Pair<>("number", NUMBERS),
                new Pair<>("operator", OPERATORS),
                new Pair<>("parenthesis", PARENTHESES),
                new Pair<>("brace", BRACES),
                new Pair<>("bracket", BRACKETS),
                new Pair<>("semicolon", SEMICOLON),
                new Pair<>("jajacode-instruction", JAJACODE_INSTRUCTIONS)
        );


        List<IndexRange> ranges = new ArrayList<>();
        for (Pair<String, Pattern> patternPair : patterns) {
            Matcher matcher = patternPair.getValue().matcher(text);
            while (matcher.find()) {
                ranges.add(new IndexRange(matcher.start(), matcher.end(), patternPair.getKey()));
            }
        }


        ranges.sort(Comparator.comparing(r -> r.start));

        // Construire les spans
        int lastEnd = 0;
        for (IndexRange range : ranges) {
            spansBuilder.add(Collections.emptyList(), range.start - lastEnd);
            spansBuilder.add(Collections.singleton(range.style), range.end - range.start);
            lastEnd = range.end;
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastEnd);

        return spansBuilder.create();
    }

    private static class IndexRange {
        final int start;
        final int end;
        final String style;

        IndexRange(int start, int end, String style) {
            this.start = start;
            this.end = end;
            this.style = style;
        }
    }

    @Override
    public void start(Stage stage) {
        AppLogger.getInstance().addLoggerListener(loggerListener);
        this.primaryStage = stage;
        tabFileMap = new HashMap<>();
        initializeWindow();
    }

    private void initializeWindow() {
        primaryStage.setTitle("MiniJaja IDE");

        VBox root = new VBox();
        root.setStyle("-fx-background-color: white;");
        VBox.setVgrow(root, Priority.ALWAYS);

        root.getChildren().addAll(
                createMenuBar(),
                createToolBar(),
                createMainContent()
        );


        scene = new Scene(root, 1024, 768);
        String cssUrl = getClass().getResource("/styles/light-theme.css").toExternalForm();
        scene.getStylesheets().add(cssUrl);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();


        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(e -> newFile());

        MenuItem openItem = new MenuItem("Open File");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(e -> openFile());

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(e -> saveFile());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(e -> closeWindow());

        fileMenu.getItems().addAll(newItem, openItem, saveItem, new SeparatorMenuItem(), exitItem);


        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoItem.setOnAction(e -> {
            CodeArea currentCodeArea = getCurrentCodeArea();
            if (currentCodeArea != null && currentCodeArea.isUndoAvailable()) {
                currentCodeArea.undo();
            }
        });

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redoItem.setOnAction(e -> {
            CodeArea currentCodeArea = getCurrentCodeArea();
            if (currentCodeArea != null && currentCodeArea.isRedoAvailable()) {
                currentCodeArea.redo();
            }
        });

        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        cutItem.setOnAction(e -> {
            CodeArea currentCodeArea = getCurrentCodeArea();
            if (currentCodeArea != null) currentCodeArea.cut();
        });

        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        copyItem.setOnAction(e -> {
            CodeArea currentCodeArea = getCurrentCodeArea();
            if (currentCodeArea != null) currentCodeArea.copy();
        });

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        pasteItem.setOnAction(e -> {
            CodeArea currentCodeArea = getCurrentCodeArea();
            if (currentCodeArea != null) currentCodeArea.paste();
        });

        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(),
                cutItem, copyItem, pasteItem);


        Menu viewMenu = new Menu("View");


        Menu themesMenu = new Menu("Themes");
        ToggleGroup themeGroup = new ToggleGroup();

        RadioMenuItem lightTheme = new RadioMenuItem("Light Theme");
        lightTheme.setToggleGroup(themeGroup);
        lightTheme.setSelected(true);
        lightTheme.setOnAction(e -> applyTheme("light"));

        RadioMenuItem darkTheme = new RadioMenuItem("Dark Theme");
        darkTheme.setToggleGroup(themeGroup);
        darkTheme.setOnAction(e -> applyTheme("dark"));

        themesMenu.getItems().addAll(lightTheme, darkTheme);


        CheckMenuItem showExplorer = new CheckMenuItem("Show Explorer");
        showExplorer.setSelected(true);
        showExplorer.setOnAction(e -> toggleExplorer(showExplorer.isSelected()));

        viewMenu.getItems().addAll(themesMenu, new SeparatorMenuItem(), showExplorer);

        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);

        return menuBar;
    }
    private void toggleExplorer(boolean show) {
        if (show) {
            if (!mainSplitPane.getItems().contains(fileExplorer)) {
                mainSplitPane.getItems().add(0, fileExplorer);
                mainSplitPane.setDividerPositions(0.2);
            }
        } else {
            mainSplitPane.getItems().remove(fileExplorer);
        }
    }

    private void applyTheme(String themeName) {
        if (scene == null) return;

        scene.getStylesheets().clear();

        if (themeName.equals("dark")) {
            String darkThemeCSS =
                    ".root { -fx-background-color: #2B2B2B; } " +
                            ".tab-pane { -fx-background-color: #2B2B2B; } " +
                            ".tab-header-area { -fx-background-color: #2B2B2B; } " +
                            ".tab { -fx-background-color: #3C3F41; } " +
                            ".tab-label { -fx-text-fill: #BBBBBB; } " +
                            ".split-pane { -fx-background-color: #2B2B2B; } " +
                            ".split-pane-divider { -fx-background-color: #3C3F41; } " +
                            ".tree-view { -fx-background-color: #2B2B2B; } " +
                            ".tree-cell { -fx-background-color: #2B2B2B; -fx-text-fill: #BBBBBB; } " +
                            ".code-area { -fx-background-color: #2B2B2B; } " +
                            ".code-area .text { -fx-fill: #A9B7C6 !important; } " +
                            ".code-area .caret { -fx-stroke: #BBBBBB; } " +
                            ".code-area .lineno { -fx-background-color: #313335; -fx-text-fill: #606366; } " +


                            ".code-area .keyword { -fx-fill: #CC7832 !important; } " +
                            ".code-area .string { -fx-fill: #6A8759 !important; } " +
                            ".code-area .comment { -fx-fill: #808080 !important; -fx-font-style: italic; } " +
                            ".code-area .number { -fx-fill: #6897BB !important; } " +
                            ".code-area .operator { -fx-fill: #A9B7C6 !important; } " +
                            ".code-area .parenthesis { -fx-fill: #A9B7C6 !important; } " +
                            ".code-area .brace { -fx-fill: #A9B7C6 !important; } " +
                            ".code-area .bracket { -fx-fill: #A9B7C6 !important; } " +
                            ".code-area .semicolon { -fx-fill: #CC7832 !important; }";

            scene.getStylesheets().clear();
            scene.getStylesheets().add("data:text/css," + darkThemeCSS.replace(" ", "%20"));


            for (Tab tab : editorTabPane.getTabs()) {
                if (tab.getContent() instanceof VirtualizedScrollPane<?>) {
                    VirtualizedScrollPane<?> scrollPane = (VirtualizedScrollPane<?>) tab.getContent();
                    CodeArea codeArea = (CodeArea) scrollPane.getContent();
                    codeArea.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #A9B7C6; " +
                            "-fx-font-size: 16px; -fx-font-family: 'JetBrains Mono', Consolas, monospace;");


                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
                }
            }



            consoleArea.setStyle("-fx-background-color: #323232; -fx-text-fill: #f8f8f2; " +
                    "-fx-control-inner-background: #323232; -fx-font-size: 14px;");


            fileExplorer.setStyle("-fx-background-color: #1e1e1e;");
            fileExplorer.setCellFactory(tv -> new TreeCell<>() {
                @Override
                protected void updateItem(File file, boolean empty) {
                    super.updateItem(file, empty);
                    setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #d4d4d4;");

                    if (empty || file == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        String icon = file.isDirectory() ? "📁 " : "📄 ";
                        setText(icon + file.getName());
                    }


                    setOnMouseEntered(e -> {
                        if (!isSelected()) {
                            setStyle("-fx-background-color: #2d2d2d; -fx-text-fill: #d4d4d4;");
                        }
                    });
                    setOnMouseExited(e -> {
                        if (!isSelected()) {
                            setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #d4d4d4;");
                        }
                    });
                }

                @Override
                public void updateSelected(boolean selected) {
                    super.updateSelected(selected);
                    if (!isEmpty()) {
                        if (selected) {
                            setStyle("-fx-background-color: #404040; -fx-text-fill: #ffffff;");
                        } else {
                            setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #d4d4d4;");
                        }
                    }
                }
            });
        } else {
            String lightThemeCSS =
                    ".root { -fx-background-color: white; } " +
                            ".tab-pane { -fx-background-color: white; } " +
                            ".tab-header-area { -fx-background-color: white; } " +
                            ".split-pane { -fx-background-color: white; } " +
                            ".tree-view { -fx-background-color: white; } " +
                            ".code-area { -fx-background-color: white; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-family: 'JetBrains Mono', Consolas, monospace; }" + // Ajout de la taille et famille de police
                            // Styles pour la coloration syntaxique
                            ".keyword { -fx-fill: #000080; -fx-font-weight: bold; }" +
                            ".string { -fx-fill: #008000; }" +
                            ".comment { -fx-fill: #808080; -fx-font-style: italic; }" +
                            ".number { -fx-fill: #0000FF; }" +
                            ".operator { -fx-fill: #000000; -fx-font-weight: bold; }" +
                            ".parenthesis { -fx-fill: #000000; }" +
                            ".brace { -fx-fill: #000000; }" +
                            ".bracket { -fx-fill: #000000; }" +
                            ".semicolon { -fx-fill: #000000; }";

            scene.getStylesheets().add("data:text/css," + lightThemeCSS.replace(" ", "%20"));


            for (Tab tab : editorTabPane.getTabs()) {
                if (tab.getContent() instanceof VirtualizedScrollPane<?>) {
                    VirtualizedScrollPane<?> scrollPane = (VirtualizedScrollPane<?>) tab.getContent();
                    CodeArea codeArea = (CodeArea) scrollPane.getContent();
                    codeArea.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                            "-fx-font-size: 16px; -fx-font-family: 'JetBrains Mono', Consolas, monospace;");
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
                }
            }



            consoleArea.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                    "-fx-control-inner-background: white;");


            fileExplorer.setStyle("-fx-background-color: white;");
            fileExplorer.setCellFactory(tv -> new TreeCell<>() {
                @Override
                protected void updateItem(File file, boolean empty) {
                    super.updateItem(file, empty);
                    setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

                    if (empty || file == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        String icon = file.isDirectory() ? "📁 " : "📄 ";
                        setText(icon + file.getName());
                    }
                }
            });


            if (jajaCodeArea != null) {
                jajaCodeArea.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            }
        }
    }
    private CodeArea getCurrentCodeArea() {
        Tab currentTab = editorTabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null && currentTab.getContent() instanceof VirtualizedScrollPane) {
            VirtualizedScrollPane<?> scrollPane = (VirtualizedScrollPane<?>) currentTab.getContent();
            return (CodeArea) scrollPane.getContent();
        }
        return null;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        Button folderButton = createToolBarButton("Open Folder", "/icon/open.png", () -> {
            openFolderDialog();
        });

        toolBar.getItems().addAll(
                createToolBarButton("New", "/icon/new-file.png", this::newFile),
                createToolBarButton("Open File", "/icon/open-file.png", this::openFile),
                folderButton,
                createToolBarButton("Save", "/icon/save.png", this::saveFile),
                new Separator(),
                createToolBarButton("Run MiniJaja", "/icon/run.png", this::executeMiniJaja),
                createToolBarButton("Compile", "/icon/build.png", this::buildCode),
                createToolBarButton("Run JajaCode", "/icon/run.png", this::executeJajaCode)
        );

        toolBar.getItems().add(new Separator());

        return toolBar;
    }


    private Button createToolBarButton(String tooltip, String iconPath, Runnable action) {
        Button button = new Button();
        button.setTooltip(new Tooltip(tooltip));

        try (InputStream is = getClass().getResourceAsStream(iconPath)) {
            if (is != null) {
                Image icon = new Image(is);
                ImageView imageView = new ImageView(icon);
                imageView.setFitWidth(24);
                imageView.setFitHeight(24);
                button.setGraphic(imageView);
            }
        } catch (IOException e) {
            button.setText(tooltip);
        }

        button.setOnAction(event -> action.run());
        return button;
    }

    private SplitPane createMainContent() {

        mainSplitPane = new SplitPane();


        if (fileExplorer == null) setupFileExplorer();
        if (editorTabPane == null) setupTabPane();
        if (consoleArea == null) {
            consoleArea = new TextArea();
            consoleArea.setEditable(false);
            consoleArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;");
        }

        // Panel de gauche (explorateur)
        VBox fileExplorerWrapper = new VBox();
        fileExplorerWrapper.getChildren().add(fileExplorer);
        VBox.setVgrow(fileExplorer, Priority.ALWAYS);


        SplitPane centerSplitPane = new SplitPane();
        centerSplitPane.setOrientation(Orientation.VERTICAL);
        centerSplitPane.getItems().addAll(editorTabPane, consoleArea);
        centerSplitPane.setDividerPositions(0.7);

        // Ajouter uniquement la partie centrale au début
        mainSplitPane.getItems().addAll(centerSplitPane);

        VBox.setVgrow(mainSplitPane, Priority.ALWAYS);
        return mainSplitPane;
    }

    private void setupTabPane() {
        editorTabPane = new TabPane();
        newFileTab();
    }

    private void newFileTab() {
        CodeArea newCodeArea = createNewCodeArea();
        Tab tab = new Tab("Untitled", new VirtualizedScrollPane<>(newCodeArea));
        tab.setOnCloseRequest(event -> {
            if (!checkSaveBeforeClosing(tab)) {
                event.consume();
            }
        });
        editorTabPane.getTabs().add(tab);
        editorTabPane.getSelectionModel().select(tab);
        tabFileMap.put(tab, null);
    }

    private void openFolderDialog() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {

            if (!mainSplitPane.getItems().contains(fileExplorer)) {
                mainSplitPane.getItems().add(0, fileExplorer);
                mainSplitPane.setDividerPositions(0.2);
            }


            TreeItem<File> root = buildFileTreeItem(selectedDirectory);
            fileExplorer.setRoot(root);
            root.setExpanded(true);
            folderTreeOpened = true;
        }
    }

    private CodeArea createNewCodeArea() {
        CodeArea newCodeArea = new CodeArea();
        newCodeArea.setParagraphGraphicFactory(LineNumberFactory.get(newCodeArea));
        newCodeArea.setStyle("-fx-font-family: 'JetBrains Mono', Consolas, monospace; -fx-font-size: 16px;");


        newCodeArea.textProperty().addListener((obs, oldText, newText) -> {
            Platform.runLater(() -> {
                newCodeArea.setStyleSpans(0, computeHighlighting(newText));
            });
        });


        Platform.runLater(() -> {
            newCodeArea.setStyleSpans(0, computeHighlighting(newCodeArea.getText()));
        });

        return newCodeArea;
    }

    private void setupFileExplorer() {
        fileExplorer = new TreeView<>();
        fileExplorer.setShowRoot(true);
        fileExplorer.setPrefWidth(200);


        fileExplorer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<File> selectedItem = fileExplorer.getSelectionModel().getSelectedItem();
                if (selectedItem != null && !selectedItem.getValue().isDirectory()) {
                    openFileInNewTab(selectedItem.getValue());
                }
            }
        });

        fileExplorer.setStyle("-fx-background-color: white;");

        fileExplorer.setCellFactory(tv -> new TreeCell<File>() {
            @Override
            public void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);


                setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

                if (empty || file == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String icon = file.isDirectory() ? "📁 " : "📄 ";
                    setText(icon + file.getName());


                    setOnMouseEntered(e -> {
                        if (!isSelected()) {
                            setStyle("-fx-background-color: #e8e8e8; -fx-text-fill: black;");
                        }
                    });

                    setOnMouseExited(e -> {
                        if (!isSelected()) {
                            setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
                        }
                    });


                    if (isSelected()) {
                        setStyle("-fx-background-color: #0096C9; -fx-text-fill: white;");
                    }
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (!isEmpty()) {
                    if (selected) {
                        setStyle("-fx-background-color: #0096C9; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void openFileFromExplorer(File file) {
        try {
            String content = Files.readString(file.toPath());
            codeArea.replaceText(content);
            currentFile = file;
            isModified = false;
            updateTitle();
        } catch (IOException e) {
            showError("Error opening file", e.getMessage());
        }
    }

    private TreeItem<File> buildFileTreeItem(File file) {
        TreeItem<File> item = new TreeItem<>(file);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                Arrays.sort(files, (f1, f2) -> {
                    if (f1.isDirectory() && f2.isDirectory()) {
                        return f1.getName().compareToIgnoreCase(f2.getName());
                    } else if (f1.isDirectory()) {
                        return -1;
                    } else if (f2.isDirectory()) {
                        return 1;
                    } else {
                        return f1.getName().compareToIgnoreCase(f2.getName());
                    }
                });

                for (File childFile : files) {
                    item.getChildren().add(buildFileTreeItem(childFile));
                }
            }
        }

        fileExplorer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<File> selectedItem = fileExplorer.getSelectionModel().getSelectedItem();
                if (selectedItem != null && !selectedItem.getValue().isDirectory()) {
                    openFileInNewTab(selectedItem.getValue());
                }
            }
        });
        return item;
    }
    private void openDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Project Folder");
        File directory = directoryChooser.showDialog(primaryStage);

        if (directory != null) {
            TreeItem<File> root = buildFileTreeItem(directory);
            fileExplorer.setRoot(root);
            root.setExpanded(true);
        }
    }

    private VBox createJajaCodePanel() {

        if (jajaCodeArea == null) {
            setupJajaCodeArea();
        }

        VBox wrapper = new VBox();


        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setPadding(new Insets(5));
        header.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("JajaCode");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setPadding(new Insets(0, 0, 0, 5));

        Button closeButton = new Button("×");
        closeButton.setStyle("-fx-font-size: 14px; -fx-padding: 0 5 0 5; -fx-min-width: 20px; -fx-min-height: 20px;");
        closeButton.setOnAction(e -> mainSplitPane.getItems().remove(wrapper));

        header.getChildren().addAll(titleLabel, closeButton);


        VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(jajaCodeArea);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        wrapper.getChildren().addAll(header, scrollPane);

        return wrapper;
    }

    private void setupCodeAreaListeners(CodeArea codeArea) {
        codeArea.textProperty().addListener((obs, oldText, newText) -> {

            if (mainSplitPane.getItems().contains(jajaCodeWrapper)) {
                buildCode();
            }


            Tab currentTab = editorTabPane.getSelectionModel().getSelectedItem();
            if (currentTab != null && !currentTab.getText().endsWith("*")) {
                currentTab.setText(currentTab.getText() + "*");
            }
        });
    }

    private void setupJajaCodeArea() {
        jajaCodeArea = new CodeArea();
        jajaCodeArea.setParagraphGraphicFactory(LineNumberFactory.get(jajaCodeArea));
        jajaCodeArea.setEditable(false);
        jajaCodeArea.setStyle("-fx-font-family: 'JetBrains Mono', Consolas, monospace; -fx-font-size: 14px;");
    }

    private void buildCode() {
        CodeArea currentCodeArea = getCurrentCodeArea();
        if (currentCodeArea == null) return;

        String sourceCode = currentCodeArea.getText();

        try {

            MiniJaja mjj = new MiniJaja(new ByteArrayInputStream(sourceCode.getBytes()));
            SimpleNode root = mjj.start();


            Compiler compiler = new Compiler(root);
            this.instrs = compiler.compile();
            String javaCode = compiler.jjcToString();

            // Afficher le JajaCode dans le panneau
            if (!mainSplitPane.getItems().contains(jajaCodeWrapper)) {
                if (jajaCodeWrapper == null) {
                    jajaCodeWrapper = createJajaCodePanel();
                }
                mainSplitPane.getItems().add(jajaCodeWrapper);
                mainSplitPane.setDividerPositions(0.2, 0.6);
            }

            if (jajaCodeArea != null) {
                jajaCodeArea.clear();
                if (javaCode != null && !javaCode.isEmpty()) {
                    jajaCodeArea.appendText(javaCode);
                } else {
                    jajaCodeArea.appendText("// No JajaCode generated");
                }
            }


            consoleArea.clear();
            consoleArea.appendText("Compilation successful\n");
            isCompiled = true;

        } catch (ParseException pe) {
            consoleArea.clear();
            consoleArea.appendText("Syntax error at line " + pe.currentToken.beginLine +
                    ", column " + pe.currentToken.beginColumn + "\n" +
                    pe.getMessage() + "\n");
            try {
                highlightError(pe);
            } catch (Exception e) {
                System.err.println("Error highlighting: " + e.getMessage());
            }
        } catch (Exception e) {
            consoleArea.clear();
            consoleArea.appendText("Compilation error: " + e.getMessage() + "\n");
        }
    }

    private void executeMiniJaja() {
        CodeArea currentCodeArea = getCurrentCodeArea();
        if (currentCodeArea == null) return;

        String code = currentCodeArea.getText();
        consoleArea.clear();

        try {
            MiniJaja mjj = new MiniJaja(new ByteArrayInputStream(code.getBytes()));
            SimpleNode node = mjj.start();
            TypeChecker typeChecker = new TypeChecker();
            typeChecker.visit(node, null);

            for (String message : loggerListener.getMessages()) {
                consoleArea.appendText(message + "\n");
            }
            boolean hasErrors = loggerListener.getLevels().stream().anyMatch(level -> level == AppLogger.TypeMessage.ERROR);

            if (!hasErrors) {
                appendToConsole("Type Check successful\n");
                InterpreterMjj interpreter = new InterpreterMjj(node);
                String result = interpreter.interpret();
                appendToConsole("MiniJaja Interpretation successful\n");
                appendToConsole("Result: " + result + "\n");
            } else {
                appendToConsole("Type checking failed. Interpretation aborted.\n");
            }

        } catch (ParseException pe) {
            highlightError(pe);
            appendToConsole("Syntax error at line " + pe.currentToken.beginLine +
                    ", column " + pe.currentToken.beginColumn + "\n" +
                    pe.getMessage() + "\n");
        } catch (Exception e) {
            appendToConsole("Error: " + e.getMessage() + "\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            appendToConsole(sw.toString());
        }
    }

    private void executeJajaCode() {
        CodeArea currentCodeArea = getCurrentCodeArea();
        if (currentCodeArea == null) return;

        String code = currentCodeArea.getText();
        consoleArea.clear();

        try {
            if (!isCompiled) {
                appendToConsole("MiniJaja is not compiled. Can't execute JajaCode");
                return;
            }
//            JajaCode jjc = new JajaCode(instrs.get(0));
//            fr.m1comp5.jjc.generated.SimpleNode node = jjc.start();
            InterpreterJjc interpreter = new InterpreterJjc(null, new Memory(), this.instrs);

            Object result = interpreter.interpret();
            appendToConsole("JajaCode Interpretation successful\n");
            appendToConsole("Result: " + result + "\n");

        } catch (Exception e) {
            appendToConsole("Error: " + e.getMessage() + "\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            appendToConsole(sw.toString());
        }
    }

    private void appendToConsole(String text) {
        consoleArea.appendText(text);
    }

    private void newFile() {
        Tab currentTab = editorTabPane.getSelectionModel().getSelectedItem();

        if (currentTab != null && checkSaveBeforeClosing(currentTab)) {
            CodeArea newCodeArea = createNewCodeArea();
            Tab newTab = new Tab("Untitled", new VirtualizedScrollPane<>(newCodeArea));
            editorTabPane.getTabs().add(newTab);
            editorTabPane.getSelectionModel().select(newTab);
            tabFileMap.put(newTab, null);
            isModified = false;
            updateTitle();
        }
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj"));

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            openFileInNewTab(file);
        }
    }
    private void openFileInNewTab(File file) {
        try {
            String content = Files.readString(file.toPath());


            for (Tab tab : editorTabPane.getTabs()) {
                if (tab.getUserData() != null && tab.getUserData().equals(file)) {
                    editorTabPane.getSelectionModel().select(tab);
                    return;
                }
            }


            CodeArea newCodeArea = createNewCodeArea();


            Tab tab = new Tab(file.getName());
            tab.setContent(new VirtualizedScrollPane<>(newCodeArea));
            tab.setUserData(file);

            tab.setOnCloseRequest(event -> {
                if (!checkSaveBeforeClosing(tab)) {
                    event.consume();
                }
            });

            editorTabPane.getTabs().add(tab);
            editorTabPane.getSelectionModel().select(tab);
            tabFileMap.put(tab, file);


            Platform.runLater(() -> {
                newCodeArea.replaceText(content);
                // Attendre que le texte soit chargé avant d'appliquer la coloration
                Platform.runLater(() -> {
                    newCodeArea.setStyleSpans(0, computeHighlighting(newCodeArea.getText()));
                });
            });

        } catch (IOException e) {
            showError("Error opening file", e.getMessage());
        }
    }

    private void saveFile() {
        Tab currentTab = editorTabPane.getSelectionModel().getSelectedItem();
        if (currentTab == null) return;

        File file = tabFileMap.get(currentTab);
        VirtualizedScrollPane<?> scrollPane = (VirtualizedScrollPane<?>) currentTab.getContent();
        CodeArea codeArea = (CodeArea) scrollPane.getContent();

        if (file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj"));
            file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                tabFileMap.put(currentTab, file);
                currentTab.setText(file.getName());
            }
        }

        if (file != null) {
            try {
                Files.writeString(file.toPath(), codeArea.getText());
                currentTab.setText(file.getName());
            } catch (IOException e) {
                showError("Error saving file", e.getMessage());
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateTitle() {
        String title = "MiniJaja IDE";
        if (currentFile != null) {
            title += " - " + currentFile.getName();
        }
        if (isModified) {
            title += " *";
        }
        primaryStage.setTitle(title);
    }
    private boolean checkSaveBeforeClosing(Tab tab) {
        if (!tab.getText().endsWith("*")) {
            return true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes");
        alert.setHeaderText("Do you want to save changes to " + tab.getText().replace("*", "") + "?");
        alert.setContentText("Your changes will be lost if you don't save them.");

        ButtonType saveButton = new ButtonType("Save");
        ButtonType dontSaveButton = new ButtonType("Don't Save");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == saveButton) {
                saveFile();
                return !tab.getText().endsWith("*");
            }
            return result.get() == dontSaveButton;
        }
        return false;
    }

    private boolean saveAllTabsBeforeClosing() {
        for (Tab tab : editorTabPane.getTabs()) {
            if (tab.getText().endsWith("*")) {
                editorTabPane.getSelectionModel().select(tab);
                if (!checkSaveBeforeClosing(tab)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void exit() {
        if (saveAllTabsBeforeClosing()) {
            primaryStage.close();
        }
    }


    private void setupKeyboardShortcuts(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {
                    case S -> saveFile();
                    case O -> openFile();
                    case N -> newFile();
                    case Q -> exit();
                }
            } else if (event.getCode() == KeyCode.F5) {
                executeMiniJaja();
            }
        });
    }

    private void highlightError(ParseException pe) {
        CodeArea currentCodeArea = getCurrentCodeArea();
        if (currentCodeArea == null) return;

        try {
            int line = Math.max(0, pe.currentToken.beginLine - 1);
            int column = Math.max(0, pe.currentToken.beginColumn - 1);


            if (line < currentCodeArea.getParagraphs().size()) {

                int lineLength = currentCodeArea.getParagraph(line).length();
                column = Math.min(column, lineLength);

                currentCodeArea.moveTo(line, column);
                currentCodeArea.requestFocus();


                int lineStart = currentCodeArea.position(line, 0).toOffset();
                int lineEnd = lineStart + currentCodeArea.getParagraph(line).length();
                currentCodeArea.selectRange(lineStart, lineEnd);
            }
        } catch (Exception e) {
            // En cas d'erreur, on évite de planter l'application
            System.err.println("Error while highlighting: " + e.getMessage());
        }
    }

    private void closeWindow() {
        if (saveAllTabsBeforeClosing()) {
            Platform.exit();
        }
    }

}