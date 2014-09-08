/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package timetablecheckfx;

import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JPanel;

/**
 *
 * @author Mitch
 */
public class TimetableCheckFX extends Application {
    public  static final File           save           = new File("Timetables/savedTimetables.tbl");
    private static final File           backup         = new File("Timetables/backupTimetables.tbl");
    
    private static             TimetableList  allTimetables  = new TimetableList().fromFile(save);
    public  static             Timetable      myTimetable        = null;
    private static             List<String>   allNames       = allTimetables.getNames();
    private static final String         versionName    = "0.3BETA";
    private static             Pane         canvas         = new Pane();
    private static final List<String>   daysOfWeek     = Arrays.asList("Monday, Tuesday, Wednesday, Thursday, Friday".split(", "));
    
    private static Point            prevClick      = null;
    private static Moment           then           = null;
    private static boolean          tableEditable  = false;
    public  static int              snapTo         = 60;
    public  static int              defaultSubject = 0;
    
    private static final int COL_NUM        = 5;
    private static final int ROW_NUM        = 12;
    private static final int VERT_MARGIN    = 30;
    private static final int HORI_MARGIN    = 30;
    private static final int minTime         = 8*60;
    private static final int maxTime         = minTime + ROW_NUM*60;
    
    private static int panelHeight = 1000;
    private static int panelWidth = 700;
    private static double colSpacing = (panelWidth - HORI_MARGIN*2)/(double)(COL_NUM);
    private static double rowSpacing = (panelHeight - VERT_MARGIN*2)/(double)(ROW_NUM +1);
    
    private static void initialise(){
        //allTimetables = TimetableList.fromFile(save);
    }
    
    @Override
    public void start(Stage primaryStage) {        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add("timetablecheckfx/StyleSheet.css");
        
        BorderPane border = new BorderPane();
        
        border.setTop(getMenu());
        border.setLeft(getSideBar("home"));
        border.getLeft().setStyle("side_bar");
        root.getChildren().add(border);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public MenuBar getMenu(){
        MenuBar tbr = new MenuBar();
        tbr.getStyleClass().add("menu");
        
        Menu menuHome = new Menu("Home");
        MenuItem getBreaksItem = new MenuItem("Get Breaks");
        getBreaksItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //Do things
            }
        });
        MenuItem editTimetableItem = new MenuItem("Edit");
        editTimetableItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //Do things
            }
        });
        menuHome.getItems().addAll(getBreaksItem);
        menuHome.getItems().addAll(editTimetableItem);
        
        
        Menu menuTimetable = new Menu("Timetable");
        MenuItem importItem = new MenuItem("Import");
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //Do things
            }
        });
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //Do things
            }
        });
        menuTimetable.getItems().addAll(importItem);
        menuTimetable.getItems().addAll(deleteItem);
        
        Menu menuHelp = new Menu("Help");
        tbr.getMenus().addAll(menuHome, menuTimetable, menuHelp);
        return tbr;
    }
    
    public AnchorPane getSideBar(String st){
        AnchorPane tbr = new AnchorPane();
        tbr.setStyle("side_bar");
        //for create
        VBox editPane = new VBox(10);
            editPane.setStyle("v_box");
            Label editTitle = new Label("Edit Timetable");
            editTitle.setStyle("title");
            Label editl1 = new Label("Add/Remove Lessons");
            ListView editList = new ListView();
            editList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            Button editb1 = new Button("Remove Timetable");
            Button editb2 = new Button("Add Timetable");
            editPane.getChildren().addAll(editTitle, editl1, editList, editb1, editb2);
            tbr.getChildren().addAll(editPane);
        return tbr;

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
