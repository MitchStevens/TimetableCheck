package timetablecheckfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Mitch
 */
public class Day {
    List<Lesson> lessons;
    
    public Day(){
        lessons = new ArrayList<>();
    }
    
    public Day(List<Lesson> list){
        this.lessons = list;
    }
    
    public Day(String data){
        lessons = new ArrayList<>();
        if(data.equals(" ")) return;
        String[] st = data.split(";");
        for(String s : st)
            lessons.add(new Lesson(s));
    }
    
    public Day getBreaksDay(int maxWaiTime){
        Day tbr = new Day();
        Collections.sort(lessons);
        
        for(int i = 0; i < lessons.size() -1; i++){
            boolean no_overlap = lessons.get(i).overlap(lessons.get(i+1)) == null;
            boolean not_same = lessons.get(i).timeEnd != lessons.get(i+1).timeStart;
            Lesson temp;
            if(no_overlap && not_same)
                try{
                    temp = new Lesson(lessons.get(i).timeEnd, lessons.get(i+1).timeStart, -1, "BREAK");
                    tbr.lessons.add(temp);
                }catch(Error e) {System.err.println("caught error -//- "+e);}
            }
        
            try{
                tbr.lessons.add(new Lesson(lessons.get(lessons.size() -1).timeEnd, lessons.get(lessons.size() -1).timeEnd + maxWaiTime, -1, "BREAK"));
            } catch (Error e) {System.out.println("caught error -//- "+e);}
        return tbr;
    }

    public Day intersectDay(Day a){
        List<Lesson> tbr = new ArrayList<>();
        List<Lesson> tbd = new ArrayList<>();
        
        Lesson temp;
        
        for(Lesson la : this.lessons)
            for(Lesson lb : a.lessons){
                temp =  la.overlap(lb);
                if(temp != null){
                    tbr.add(temp);
                    System.out.println("new break "+ temp.toString());}
            }
        
        Collections.sort(tbr);
        for(int i = 0; i < tbr.size() -1; i++){
            if(tbr.get(i).timeEnd == tbr.get(i+1).timeStart){
                tbr.set(i, new Lesson(tbr.get(i).timeStart, tbr.get(i+1).timeEnd, -1, "BREAK"));
                tbd.add(tbr.get(i+1));
            }
        }
        
        System.out.println("INTERSECT COMPLETE");
        tbr.removeAll(tbd);
        return new Day(tbr);
    }
    
    public void stretch(Lesson l, Moment now, int time){
        if(l == null) return;
        
        for(Lesson all : lessons)
            if(l.equals(all))
                all.stretch(now, time);
        
        for(Lesson all : lessons)
            if(!l.equals(all) && all.overlap(l) != null){
                    shift(all, time);
                System.out.println(all.toString()+"overlaps"+l.toString());
            }
    }
    
    public void shift(Lesson l, int time){
        if(l == null) return;
        
        for(Lesson all: lessons)
            if(l.equals(all))
                all.shift(time);
        
        for(Lesson all : lessons)
            if(!l.equals(all) && all.overlap(l) != null){
                    shift(all, time);
                System.out.println(all.toString()+"overlaps"+l.toString());
            }
    }
    
    public void snapPos(Lesson l, int snap){
        int pos = l.timeStart % snap;
        if(pos <= snap/2)
            shift(l, -pos);
        else
            shift(l, -pos + snap);        
    }
    
    public void snapDur(Lesson l, int snap){
        if(l.durationMinutes() < snap) ;
        int dur = l.durationMinutes() % snap;
        if(dur <= snap/2)
            l.timeEnd += -dur;
        else
            l.timeEnd += -dur + snap;
    }
    
    @Override
    public String toString() {
        if(lessons.isEmpty()) return " ";
        String tbr = "";
        for(Lesson l : lessons)
            tbr += l.toString()+";";
        return tbr;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Day)) return false;
        Day d = (Day)o;
        if(!this.lessons.equals(d.lessons)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.lessons.hashCode();
    }
    
    
}
