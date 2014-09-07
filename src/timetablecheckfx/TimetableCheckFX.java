/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package timetablecheckfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Mitch
 */
public class TimetableCheckFX extends Application {
    public Timetable timetable;
    final static int xPush = 10;
    final static int yPush = 10;
    final static int numRows = 5;
    final static int numCols = 12;
    private static double xDist = 0;
    private static double yDist = 0;
    
    @Override
    public void start(Stage primaryStage) {        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 250);
        
        Line[] rows = new Line[numRows];
        Line[] cols = new Line[numCols];
        
        for(int i = 0; i < numCols; i++){
            cols[i] = new Line(xPush + 7, 50*i, 100, i*20);
            root.getChildren().add(cols[i]);
        }
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
