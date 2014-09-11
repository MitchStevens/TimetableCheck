/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package timetablecheckfx;

import org.controlsfx.dialog.Dialog;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.collections.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author Mitch
 */
public class TimetableCheckFX extends Application {
    public  static final File           save           = new File("Timetables/savedTimetables.tbl");
    private static final File           backup         = new File("Timetables/backupTimetables.tbl");
    
    private static             TimetableList  allTimetables  = TimetableList.fromFile(save);
    public  static             Timetable      myTimetable        = null;
    private static             List<String>   allNames       = allTimetables.getNames();
    public  static final String         versionName    = "0.3BETA";
    
    private static TimetablePane    tPane;
    private static BorderPane       border = new BorderPane();
    private static StackPane        sidebar;
    private static MenuBar          menu;
    
    private static double panelHeight = 700;
    private static double panelWidth = 900;
    
    private void initialise(){
        //allTimetables = TimetableList.fromFile(save);
        getMenu();
        getSideBar();
        
    }
    
    @Override
    public void start(Stage primaryStage) {        
        final StackPane root = new StackPane();
        Scene scene = new Scene(root, panelWidth, panelHeight);
        scene.getStylesheets().add("timetablecheckfx/StyleSheet.css");
        
        initialise();
        border = new BorderPane();
        border.setTop(menu);
        border.setLeft(sidebar);
        tPane = new TimetablePane(null, panelWidth - 250, panelHeight - 30);
        border.setCenter(tPane.pane);
        
        ChangeListener<Number> changedSize = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                panelHeight = root.getHeight();
                panelWidth = root.getWidth();
                tPane.changeSize(panelWidth -250, panelHeight -30);
                border.setCenter(tPane.pane);
            }
        };
        
        primaryStage.heightProperty().addListener(changedSize);
        primaryStage.widthProperty().addListener(changedSize);
        
        root.getChildren().add(border);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //CREATING PANES
    public void getMenu(){
        menu = new MenuBar();
        menu.getStyleClass().add("menu");
        
        Menu menuHome = new Menu("Home");
        MenuItem getBreaksItem = new MenuItem("Get Breaks");
        getBreaksItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                for(Node n : sidebar.getChildren())
                    if(n.getId().equals("homePane")){
                        n.toFront();
                        break;
                    }
            }
        });
        MenuItem editTimetableItem = new MenuItem("Edit");
        editTimetableItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                for(Node n : sidebar.getChildren())
                    if(n.getId().equals("editPane")){
                        n.toFront();
                        break;
                    }
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
        MenuItem tutorialItem = new MenuItem("Tutorial");
        tutorialItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                for(Node n : sidebar.getChildren())
                    if(n.getId().equals("tutePane")){
                        n.toFront();
                        break;
                    }
            }
        });
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Dialog about = new Dialog(border, "About");
                about.setMasthead("TimetableCheck - Version "+versionName);
                about.setContent("Made by Mitch Stevens\n" +
                                 "Contact at somecleverstatement@gmail.com");
                about.show();
            }
        });
        menuHelp.getItems().addAll(tutorialItem, aboutItem);
        
        menu.getMenus().addAll(menuHome, menuTimetable, menuHelp);
        menu.setPrefHeight(20);
    }
    
    public void getSideBar(){
        sidebar = new StackPane();
        sidebar.getStyleClass().add("side_bar");
        //for editPane
        VBox editPane = new VBox();
            editPane.getStyleClass().add("vbox");
            editPane.setId("editPane");
            Label editTitle = new Label("Edit Timetable");
            editTitle.getStyleClass().add("title");
            Label editl1 = new Label("Add/Remove Lessons");
            ListView<String> editList = new ListView(allTimetables.getNames());
            editList.setPrefHeight(148);
            editList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            Button editb1 = new Button("Add Timetable");
            editPane.getChildren().addAll(editTitle, editl1, editList, editb1);
            sidebar.getChildren().addAll(editPane);
        VBox homePane = new VBox();
            homePane.getStyleClass().add("vbox");
            homePane.setId("homePane");
            Label homeTitle = new Label("Get Breaks");
            homeTitle.getStyleClass().add("title");
            ListView<String> homelist = new ListView<>(allTimetables.getNames());
            homelist.setPrefHeight(148);
            homelist.getStyleClass().add("list-view");
            homelist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            Label homel1 =  new Label("Max time to wait (h:m):");
            TextField homet1 = new TextField("1:00");
            Button homeb1 = new Button("Show Breaks");
            Button homeb2 = new Button("Delete Timetable");
            homePane.getChildren().addAll(homeTitle, homelist, homel1, homet1, homeb1, homeb2);
            sidebar.getChildren().add(homePane);
        VBox tutePane = new VBox();
            tutePane.setId("tutePane");
            tutePane.getStyleClass().add("acc-vbox");
            Label tuteTitle = new Label("Tutorial");
            tuteTitle.getStyleClass().add("acc-title");
            Accordion accordion = new Accordion();
            accordion.getStyleClass().add("accordion");
            TextArea tuteText = new TextArea("Label homel1 =  new Label(\"Max time to wait (h:m):\");\n" +
                "TextField homet1 = new TextField(\"1:00\");\n" +
                "Button homeb1 = new Button(\"Show Breaks\");\n" +
                "Button homeb2 = new Button(\"Delete Timetable\");");
            tuteText.getStyleClass().add("text-area");
            tuteText.setPrefWidth(250);
            TitledPane t1 = new TitledPane("Edit Timetable", tuteText);
            TitledPane t2 = new TitledPane("T2", new Button("B2"));
            TitledPane t3 = new TitledPane("T3", new Button("B3"));
            accordion.getPanes().addAll(t1, t2, t3);
            tutePane.getChildren().addAll(tuteTitle, accordion);
            sidebar.getChildren().addAll(tutePane);
            sidebar.getChildren().get(1).toFront();
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
