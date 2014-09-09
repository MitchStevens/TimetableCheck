package timetablecheckfx;

import java.util.*;
import  java.io.*;
import javafx.scene.paint.Color;
/**
 *
 * @author Mitch
 */

public class Timetable {
    private List<Day> days;
    public String    name;
    public List<String> subjects;
    //final static Color[] colors = {new Color(155, 194, 230, 0.7), new Color(255, 102, 255, 0.7), Color.RED};
    final public static Color[] colors = {new Color(0.9254, 0.4706, 0.0906, 0.9),
        new Color(0.5725, 0.8157, 0.3137, 0.9),
        new Color(1.0000, 0.7529, 0.0000, 0.9),
        new Color(0.3569, 0.6078, 0.8363, 0.9),
        Color.RED,
        Color.RED,
        Color.RED};
    
    public Timetable(){
        this.name = "UNTITLED";
        this.subjects = new ArrayList<>();
        this.days = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            days.add(new Day());
    }
    
    public Timetable(String name, List<String> subjects){
        this.name = name;
        this.subjects = subjects;
        this.days = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            days.add(new Day());
    }
    
    public Timetable(String data) {
        String[] st = data.split("/");
        this.name = st[0];
        if(st[1].equals(" "))
            this.subjects = new ArrayList<>();
        else
            this.subjects = Arrays.asList(st[1].split(";"));
        days = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            days.add(new Day(st[i+2]));
    }
    
    public void addSubject(String subjectName){
        subjects.add(subjectName);
    }
    
    public void addLesson(Lesson lesson, int day){
        for(Lesson l : this.days.get(day).lessons)
            if(lesson.overlap(l) != null)
                return;
        
        if(subjects.isEmpty()) subjects.add("NEW SUBJECT");
        
        this.days.get(day).lessons.add(lesson);
    }
    
    public void addLessonUnsafe(Lesson lesson, int day){
        this.days.get(day).lessons.add(lesson);
    }
    
    public boolean dayEmpty(int day){
        return this.getDay(day).lessons.isEmpty();
    }
    
    public Lesson hasLesson(Moment m){
        for(Lesson l : this.days.get(m.day).lessons)
            if(l.timeStart <= m.time && l.timeEnd >= m.time)
                return l;
        return null;
    }
    
    public Timetable intersect(Timetable a){
        Timetable tbr = new Timetable();
        for(int i = 0; i < 5; i++){
            tbr.days.set(i, this.getDay(i).intersectDay(a.getDay(i)));
            System.out.println("DAY "+i);}
        System.out.println(tbr.toString());
        return tbr;
    }
    
    public Day getDay(int i){
        return days.get(i);
    }
     
    public Timetable getBreaks(int maxWaitTime){
        Timetable tbr = new Timetable();
        for(int i = 0; i < 5; i++)
            if(!this.getDay(i).lessons.isEmpty())
                tbr.days.set(i, this.getDay(i).getBreaksDay(maxWaitTime));
    
        return tbr;
    }    
    
    public List<Lesson> getSubject(int index){
        List<Lesson> tbr = new ArrayList<>();
        if(index >= this.subjects.size() && index < 0)
            return tbr;
        for(Day d : days)
            for(Lesson l : d.lessons)
                if(l.subject == index)
                    tbr.add(l);
        return tbr;
    }
    
    public List<Lesson> getLessons(){
        List<Lesson> tbr = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            tbr.addAll(this.days.get(i).lessons);
        return tbr;
    }
    
    public void removeLesson(Lesson removed, int day){        
        for(Lesson l : this.days.get(day).lessons)
            if(l.equals(removed))
                this.days.get(day).lessons.remove(l);
    }
    
    public void saveTimetable(File file){
        try{
            if(!file.exists())
                file.createNewFile();
            
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.printf("%s" + "%n", this.toString());
            pw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String tbr = this.name+"/";
        
        if(subjects.isEmpty()) tbr += " ";
        else
            for(String s : this.subjects)
                tbr += s+";";
        
        for(int i = 0; i < 5; i++){
            tbr += "/";
            try{tbr += days.get(i).toString();}
            catch(Exception e){
                tbr += " ";
            }
        }
        return tbr;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Timetable)) return false;
        Timetable d = (Timetable)o;
        if(!this.days.equals(d.days)) return false;
        if(!this.name.equals(d.name)) return false;
        if(!this.subjects.equals(d.subjects)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += name.hashCode();
        hash += days.hashCode();
        hash += subjects.hashCode();
        return hash;
    }    
}
