package timetablecheckfx;

import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.*; 
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class TimetablePane {
    private static final int COL_NUM        = 5;
    private static final int ROW_NUM        = 12;
    private static final int VERT_MARGIN    = 30;
    private static final int HORI_MARGIN    = 30;
    private static final int MIN_TIME       = 8*60;
    private static final int MAX_TIME       = MIN_TIME + ROW_NUM*60;
    
    private static final String[] DAYS_OF_THE_WEEK = "Monday, Tuesday, Wednesday, Thurday, Friday".split(", ");
    
    private static Point            prevClick      = null;
    private static boolean          tableEditable  = false;
    public  static int              snapTo         = 60;
    public  static int              defaultSubject = 0;
    
    private static int paneHeight;
    private static int paneWidth;
    public static double colSpacing;
    private static double rowSpacing;
    
    private static Timetable timetable;
    public Pane pane;
    
    TimetablePane(Timetable t, double w, double h) {
        paneHeight = (int)h;
        paneWidth = (int)w;
        colSpacing = (paneWidth - HORI_MARGIN*2)/(double)(COL_NUM);
        rowSpacing = (paneHeight - VERT_MARGIN*2)/(double)(ROW_NUM +1);
        timetable = new Timetable("MitchSem2"
                + "/ENGN1217;COMP1110;MATH2307;COMP2610;"
                + "/540,600,0,EMPTY;720,780,1,EMPTY;"
                + "/540,600,2,EMPTY;600,660,0,EMPTY;780,840,1,EMPTY;840,900,3,EMPTY;"
                + "/780,840,0,EMPTY;840,900,3,EMPTY;960,1080,3,EMPTY;600,660,0,EMPTY;"
                + "/540,600,2,EMPTY;600,660,0,EMPTY;"
                + "/540,600,2,EMPTY;660,780,1,EMPTY;780,840,1,EMPTY;840,900,2,EMPTY;");
        System.out.println(timetable.toString());
        
        this.pane = new Pane();
        pane.setPrefSize(paneHeight, paneWidth);
        
        
        Line[] verticalLines   = new Line[COL_NUM +1];
        Line[] horizontalLines = new Line[ROW_NUM +2];
        Text[] dayLabels       = new Text[5];
        List<LessonBox> allLessons = new ArrayList<>();
        
        for (double i = 0; i < COL_NUM +1; i++) {
            int x = HORI_MARGIN + (int)(i*colSpacing);
            int y = VERT_MARGIN;
            verticalLines[(int)i] = new Line(x, y, x, paneHeight -y);
        }
        
        for (double i = 0; i < ROW_NUM +2; i++) {
            int x = HORI_MARGIN;
            int y = VERT_MARGIN + (int)(i*rowSpacing);
            horizontalLines[(int)i] = new Line(x, y, paneWidth - x, y);
        }
        
        for (double i = 0; i < 5; i++){
            int x = HORI_MARGIN + (int)(i*colSpacing) +2;
            int y = VERT_MARGIN + (int)rowSpacing -2;
            dayLabels[(int)i] = new Text(x, y, displayString(DAYS_OF_THE_WEEK[(int)i]));
        }
        
        int x1, y1;
        for(int i = 0; i < 5; i++)
            for(Lesson l : timetable.getDay(i).lessons){
                String subjectName;
                Color lessonColor;
                        
                if(l.subject == -1){
                    lessonColor = Color.RED;
                    subjectName = "BREAK";
                }else{
                    lessonColor = timetable.colors[l.subject];
                    subjectName = timetable.subjects.get(l.subject);}
     
                x1 = HORI_MARGIN + (int)(i*colSpacing) +1;
                y1 = VERT_MARGIN + (int)rowSpacing + (int)((((double)l.timeStart - MIN_TIME)*rowSpacing)/60 +1.9);
                Rectangle rect = new Rectangle();
                rect.setFill(lessonColor);
                rect.setWidth(colSpacing -2);
                rect.setHeight(l.durationHours()*rowSpacing -2);
                LessonBox temp = new LessonBox(l, rect,
                        new Text(3, 15, displayString(subjectName)),
                        (l.durationHours() >= 1.0) ? new Text(3, 30, displayString(l.durationString())) : null);
                temp.setLayoutX(x1);
                temp.setLayoutY(y1);
                //temp.b
                allLessons.add(temp);
            }
        //Name of Person and Version number displayed
        Text title = new Text(10, 20, timetable.name);
        Text version = new Text(paneWidth - displayWidth(TimetableCheckFX.versionName) -20,
                                paneHeight - 10,
                                TimetableCheckFX.versionName);
        
        Rectangle r = new Rectangle(100, 100, Color.AQUA);

        
        pane.getChildren().addAll(Arrays.asList(verticalLines));
        pane.getChildren().addAll(Arrays.asList(horizontalLines));
        pane.getChildren().addAll(Arrays.asList(dayLabels));
        pane.getChildren().addAll(allLessons);
    }
    
    private String displayString(String text){
        if(displayWidth(text) < colSpacing) return text;
        while(displayWidth(text) > colSpacing - 25)
            text = text.substring(0, text.length() -2);
        return text+"...";
    }
    
    private int displayWidth(String text){
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        FontMetrics fm = img.getGraphics().getFontMetrics(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));   
        return fm.stringWidth(text);
    } 
    
    
}

//Based on class at https://gist.github.com/miho/3821180.
class LessonBox extends Pane {
    //Lesson info
    private Lesson lesson;
    
    // node position
    private double x = 0;
    private double y = 0;
    // mouse position
    private double mousex = 0;
    private double mousey = 0;
    private Node view;
    private boolean dragging = false;
    private boolean moveToFront = true;
 
    public LessonBox() {
        init();
    }
 
    public LessonBox(Lesson l, Node view, Text s, Text d) {
        this.view = view;
        this.lesson = l;
        getChildren().addAll(view, s, d);
        init();
    }
 
    private void init() {
 
        onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
 
            // record the current mouse X and Y position on Node
            mousex = event.getSceneX();
            mousey = event.getSceneY();
 
            x = getLayoutX();
            y = getLayoutY();
 
            if (isMoveToFront())
                toFront();
        }
    });
 
    //Event Listener for MouseDragged
    onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent event) {
        // Get the exact moved X and Y
        double offsetX = event.getSceneX() - mousex;
        double offsetY = event.getSceneY() - mousey;
        
        x += 0;
        y += offsetY;
 
        double scaledX = x;
        double scaledY = y;
 
        setLayoutX(scaledX);
        setLayoutY(scaledY);
 
        dragging = true;
 
        // again set current Mouse x AND y position
        mousex = event.getSceneX();
        mousey = event.getSceneY();
 
        event.consume();
        }
    });
 
    onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            dragging = false;
        }
    });
 
    }
 
    protected boolean isDragging() {
        return dragging;
    }
 
    public Node getView() {
        return view;
    }
 
    public void setMoveToFront(boolean moveToFront) {
        this.moveToFront = moveToFront;
    }
 
    public boolean isMoveToFront() {
        return moveToFront;
    }
    
    public void removeNode(Node n) {
        getChildren().remove(n);
    }
}