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
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
    public  static final String         versionName    = "0.3BETA";
    
    private static TimetablePane    tPane;
    private static BorderPane       border = new BorderPane();
    private static StackPane        sidebar;
    private static MenuBar          menu;
    
    private static int panelHeight = 700;
    private static int panelWidth = 900;
    
    private void initialise(){
        //allTimetables = TimetableList.fromFile(save);
        getMenu();
        getSideBar();
        
    }
    
    @Override
    public void start(Stage primaryStage) {        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, panelWidth, panelHeight);
        scene.getStylesheets().add("timetablecheckfx/StyleSheet.css");
        
        initialise();
        border = new BorderPane();
        border.setTop(menu);
        border.setLeft(sidebar);
        tPane = new TimetablePane(null, panelWidth - 250, panelHeight - 30);
        border.setCenter(tPane.pane);
        
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
                    n.toBack();
                sidebar.getChildren().get(1).toFront();
                System.out.println("getbreaks");
            }
        });
        MenuItem editTimetableItem = new MenuItem("Edit");
        editTimetableItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
               for(Node n : sidebar.getChildren())
                   n.toBack();
               sidebar.getChildren().get(0).toFront();
                System.out.println("edit");
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
                //Do things
            }
        });
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                 String msg = "<html>TimetableCheck - Version "+versionName+
                     "  <br>Made by Mitch Stevens"+
                     "  <br>Contact at somecleverstatement@gmail.com"+
                     "</html>";

                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage(msg);
                optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog(null, "Width 100");
                dialog.setVisible(true);
            }
        });
        menuHelp.getItems().addAll(tutorialItem, aboutItem);
        
        menu.getMenus().addAll(menuHome, menuTimetable, menuHelp);
    }
    
    public void getSideBar(){
        sidebar = new StackPane();
        sidebar.getStyleClass().add("side_bar");
        //for editPane
        VBox editPane = new VBox();
            editPane.setPrefWidth(250);
            editPane.getStyleClass().add("vbox");
            Label editTitle = new Label("Edit Timetable");
            editTitle.getStyleClass().add("title");
            Label editl1 = new Label("Add/Remove Lessons");
            ObservableList<String> names = FXCollections.observableArrayList(
                "Iron, Carbon, Thallium, Nitrogen, Oxygen".split(", "));
            ListView<String> editList = new ListView(names);
            editList.setPrefHeight(142);
            editList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            Button editb1 = new Button("Add Timetable");
            editPane.getChildren().addAll(editTitle, editl1, editList, editb1);
            sidebar.getChildren().addAll(editPane);
        VBox homePane = new VBox();
            homePane.setPrefWidth(250);
            homePane.getStyleClass().add("vbox");
            Label homeTitle = new Label("Get Breaks");
            homeTitle.getStyleClass().add("title");
            ListView<String> homelist = new ListView<>(names);
            homelist.setPrefHeight(142);
            homelist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            Label homel1 =  new Label("Max time to wait (h:m):");
            TextField homet1 = new TextField("1:00");
            Button homeb1 = new Button("Show Breaks");
            Button homeb2 = new Button("Delete Timetable");
            homePane.getChildren().addAll(homeTitle, homelist, homel1, homet1, homeb1, homeb2);
            sidebar.getChildren().add(homePane);
        VBox tutePane = new VBox();
            
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
