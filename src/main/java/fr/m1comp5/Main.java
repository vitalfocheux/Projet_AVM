package fr.m1comp5;

import fr.m1comp5.UI.JavaFxIDE;
import javafx.application.Application;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JavaFxIDE testJavaFx = new JavaFxIDE();
        testJavaFx.mainScreen(primaryStage);
    }

    public static void main(String[] args) {
//        MiniJajaWindow mjjwindow = new MiniJajaWindow();
//        mjjwindow.showWindow();
        launch(args);
    }
}
