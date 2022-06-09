/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package snake;

import javafx.application.Application;
import javafx.stage.Stage;
import snake.userinterface.UserInterfaceImpl;

/**
 *
 * @author William-UEM
 */
public class SnakeApplication extends Application {
    private UserInterfaceImpl uiImpl;
    
    @Override
    public void start(Stage primaryStage) {
         uiImpl = new UserInterfaceImpl(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
