package fr.m1comp5.UI;

import fr.m1comp5.mjj.generated.MiniJaja;
import fr.m1comp5.mjj.generated.ParseException;
import fr.m1comp5.mjj.generated.SimpleNode;
import fr.m1comp5.mjj.InterpreterMjj;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

public class MiniJajaWindow extends Application {
    private Stage primaryStage;
    private CodeArea codeArea;
    private TextArea consoleArea;
    private File currentFile;
    private boolean isModified;

    private TabPane tabPane;  // New: TabPane to hold multiple editors
    private Tab currentTab;   // New: Currently selected tab
    private Map<Tab, File> tabFiles;  // New: Map to track files associated with tabs


    private static final Pattern KEYWORDS = Pattern.compile("\\b(class|extends|void|int|boolean|if|else|while|return|true|false|main|writeln)\\b");
    private static final Pattern PARENTHESES = Pattern.compile("[()]");
    private static final Pattern BRACES = Pattern.compile("[{}]");
    private static final Pattern BRACKETS = Pattern.compile("[\\[\\]]");
    private static final Pattern SEMICOLON = Pattern.compile(";");
    private static final Pattern STRING_PATTERN = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("//[^\n]*|/\\*(.|\\R)*?\\*/");
    private static final Pattern NUMBERS = Pattern.compile("\\b\\d+\\b");
    private static final Pattern OPERATORS = Pattern.compile("[+\\-*/<>=!&|^~]");

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
                new Pair<>("semicolon", SEMICOLON)
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

    private class EditorTab extends Tab {
        private final CodeArea codeArea;
        private boolean isModified;

        public EditorTab(String title) {
            super(title);
            this.codeArea = new CodeArea();
            setupCodeArea(this.codeArea);
            VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
            setContent(scrollPane);

            // Add close button handler
            setOnCloseRequest(event -> {
                if (!checkSaveBeforeClosing(this)) {
                    event.consume();
                }
            });

            // Add on closed handler to clean up resources
            setOnClosed(event -> {
                tabFiles.remove(this);  // Remove the tab from the map when it's closed
            });

            // Track modifications
            codeArea.textProperty().addListener((obs, oldText, newText) -> {
                isModified = true;
                updateTitle();
            });
        }

        public CodeArea getCodeArea() {
            return codeArea;
        }
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.tabFiles = new HashMap<>();  // Initialize the map here
        initializeWindow();
    }

    private void initializeWindow() {
        primaryStage.setTitle("MiniJaja IDE");

        VBox root = new VBox();
        root.getStyleClass().add("root");

        root.getChildren().addAll(
                createMenuBar(),
                createToolBar(),
                createMainContent()
        );

        Scene scene = new Scene(root, 1024, 768);

        String cssUrl = getClass().getResource("/styles/light-theme.css").toExternalForm();
        scene.getStylesheets().add(cssUrl);

        primaryStage.setScene(scene);
        setupKeyboardShortcuts(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menu File
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(e -> newFile());

        MenuItem openItem = new MenuItem("Open");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(e -> openFile());

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(e -> saveFile());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(e -> closeWindow());

        fileMenu.getItems().addAll(newItem, openItem, saveItem, new SeparatorMenuItem(), exitItem);

        // Menu Edit
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoItem.setOnAction(e -> {if (codeArea.isUndoAvailable()) codeArea.undo();});

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redoItem.setOnAction(e -> {if (codeArea.isRedoAvailable()) codeArea.redo();});

        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        cutItem.setOnAction(e -> codeArea.cut());

        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        copyItem.setOnAction(e -> codeArea.copy());

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        pasteItem.setOnAction(e -> codeArea.paste());

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
        viewMenu.getItems().add(themesMenu);

        // Menu Run
        Menu runMenu = new Menu("Run");
        MenuItem executeItem = new MenuItem("Execute");
        executeItem.setAccelerator(new KeyCodeCombination(KeyCode.F5));
        executeItem.setOnAction(e -> executeCode());
        runMenu.getItems().add(executeItem);


        menuBar.getMenus().addAll(fileMenu, editMenu, viewMenu, runMenu);
        return menuBar;
    }

    private void applyTheme(String themeName) {
        Scene scene = primaryStage.getScene();

        // Supprime les anciens styles
        scene.getStylesheets().clear();

        // Applique le nouveau thème
        String cssPath = switch (themeName) {
            case "dark" -> "/styles/dark-theme.css";
            case "light" -> "/styles/light-theme.css";
            default -> "/styles/light-theme.css";
        };

        String cssUrl = getClass().getResource(cssPath).toExternalForm();
        scene.getStylesheets().add(cssUrl);

        // Applique spécifiquement pour CodeArea et Console
        if (themeName.equals("dark")) {
            codeArea.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: #a9b7c6;");
            consoleArea.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: #a9b7c6; " +
                    "-fx-control-inner-background: #2b2b2b;");
        } else {
            codeArea.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            consoleArea.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                    "-fx-control-inner-background: white;");
        }

        // Force le rafraîchissement
        Platform.runLater(() -> {
            scene.getWindow().sizeToScene();
            VBox root = (VBox) scene.getRoot();
            MenuBar menuBar = (MenuBar) root.getChildren().get(0);
            for (Menu menu : menuBar.getMenus()) {
                for (MenuItem item : menu.getItems()) {
                    if (themeName.equals("dark")) {
                        item.setStyle("-fx-text-fill: white;");
                    } else {
                        item.setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }



    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        toolBar.getItems().addAll(
                createToolBarButton("New", "/icon/new-file.png", this::newFile),
                createToolBarButton("Open", "/icon/open.png", this::openFile),
                createToolBarButton("Save", "/icon/save.png", this::saveFile),
                new Separator(),
                createToolBarButton("Build", "/icon/build.png", this::executeCode),
                createToolBarButton("Run", "/icon/run.png", this::executeCode)
        );

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
        // Create TabPane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        // Create initial tab
        createNewTab();

        // Handle tab selection changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                currentTab = newTab;
                updateTitle();
            }
        });

        consoleArea = new TextArea();
        consoleArea.setEditable(false);
        consoleArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        VBox consoleWrapper = new VBox();
        consoleWrapper.getChildren().add(consoleArea);
        VBox.setVgrow(consoleArea, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(tabPane, consoleWrapper);
        splitPane.setDividerPositions(0.7);

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        return splitPane;
    }

    private void createNewTab() {
        EditorTab tab = new EditorTab("Untitled");
        tabFiles.put(tab, null);  // Add tab to the map with no associated file yet
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        currentTab = tab;
    }

    private CodeArea getCurrentCodeArea() {
        if (currentTab instanceof EditorTab) {
            return ((EditorTab) currentTab).getCodeArea();
        }
        return null;
    }

    private void executeCode() {
        if (getCurrentCodeArea() == null) return;

        String code = getCurrentCodeArea().getText();
        consoleArea.clear();

        try {
            MiniJaja mjj = new MiniJaja(new ByteArrayInputStream(code.getBytes()));
            SimpleNode node = mjj.start();
            InterpreterMjj interpreter = new InterpreterMjj(node);

            Object result = interpreter.interpret();
            appendToConsole("Compilation successful\n");
            appendToConsole("Result: " + result + "\n");


            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                    pw.write(b);
                }
            }));

            node.dump("");
            System.setOut(originalOut);
            appendToConsole(sw.toString());

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
    private void setupCodeArea(CodeArea codeArea) {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setStyle("-fx-font-family: 'JetBrains Mono', Consolas, monospace; -fx-font-size: 14px;");

        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(100))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
    }

    private void appendToConsole(String text) {
        consoleArea.appendText(text);
    }

    private void newFile() {
        createNewTab();
        updateTitle();
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MiniJaja File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            // Check if file is already open
            Tab existingTab = null;
            for (Map.Entry<Tab, File> entry : tabFiles.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals(file)) {
                    existingTab = entry.getKey();
                    break;
                }
            }

            if (existingTab != null) {
                tabPane.getSelectionModel().select(existingTab);
                currentTab = existingTab;
                updateTitle();
                return;
            }

            try {
                String content = Files.readString(file.toPath());
                EditorTab tab = new EditorTab(file.getName());
                tab.getCodeArea().replaceText(content);
                tabFiles.put(tab, file);
                tabPane.getTabs().add(tab);

                tabPane.getSelectionModel().select(tab);
                currentTab = tab;
                ((EditorTab) currentTab).isModified = false;

                updateTitle();
            } catch (IOException e) {
                showError("Error opening file", e.getMessage());
            }
        }
    }

    private void saveFile() {
        if (!(currentTab instanceof EditorTab)) return;

        File file = tabFiles.get(currentTab);
        if (file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save MiniJaja File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("MiniJaja Files", "*.mjj")
            );

            file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                tabFiles.put(currentTab, file);
                currentTab.setText(file.getName());
            } else {
                return;
            }
        }

        try {
            Files.writeString(file.toPath(), getCurrentCodeArea().getText());
            ((EditorTab) currentTab).isModified = false;
            updateTitle();
        } catch (IOException e) {
            showError("Error saving file", e.getMessage());
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
        if (currentTab != null) {
            File currentFile = tabFiles.get(currentTab);
            if (currentFile != null) {
                title += " - " + currentFile.getName();
            } else {
                title += " - " + currentTab.getText();
            }
            if (currentTab instanceof EditorTab && ((EditorTab) currentTab).isModified) {
                title += " *";
            }
        }
        primaryStage.setTitle(title);
    }

    private boolean checkSaveBeforeClosing(Tab tab) {
        if (!(tab instanceof EditorTab) || !((EditorTab) tab).isModified) {
            return true;
        }

        // Get the file name or tab title to display
        String fileName = tab.getText();
        File file = tabFiles.get(tab);
        if (file != null) {
            fileName = file.getName();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes");
        alert.setHeaderText("Do you want to save changes to " + fileName + "?");
        alert.setContentText("Your changes will be lost if you don't save them.");

        ButtonType saveButton = new ButtonType("Save");
        ButtonType dontSaveButton = new ButtonType("Don't Save");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == saveButton) {
            currentTab = tab;
            saveFile();
            return !((EditorTab) tab).isModified;
        }
        return result == dontSaveButton;
    }


    private void exit() {
        boolean allSaved = true;
        for (Tab tab : new ArrayList<>(tabPane.getTabs())) {
            if (!checkSaveBeforeClosing(tab)) {
                allSaved=false;
            }
            if(allSaved)
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
                executeCode();
            }
        });
    }

    private void highlightError(ParseException pe) {
        int line = pe.currentToken.beginLine - 1;
        int column = pe.currentToken.beginColumn - 1;
        codeArea.moveTo(line, column);
        codeArea.requestFocus();


        int lineStart = codeArea.position(line, 0).toOffset();
        int lineEnd = lineStart + codeArea.getParagraph(line).length();
        codeArea.selectRange(lineStart, lineEnd);
    }


    private void closeWindow() {
        for (Tab tab : new ArrayList<>(tabPane.getTabs())) {
            if (!checkSaveBeforeClosing(tab)) {
                return;
            }
        }
        Platform.exit();
    }
    /*
    public static void main(String[] args) {
        launch(args);
    }  */
}