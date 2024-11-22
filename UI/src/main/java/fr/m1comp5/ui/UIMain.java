package fr.m1comp5.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class UIMain extends Application {

    @Override
    public void start(Stage stage) {
        IDE testJavaFx = new IDE();
        testJavaFx.mainScreen(stage);
    }

    public static void main(String[] args) {
//      MiniJajaWindow mjjwindow = new MiniJajaWindow();
//      mjjwindow.showWindow();
        launch();
    }
}