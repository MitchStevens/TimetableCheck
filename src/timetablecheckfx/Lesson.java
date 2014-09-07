package timetablecheckfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JOptionPane;
/**
 *
 * @author Mitch
 */
public class Lesson implements Comparable<Lesson>{
    private static final int minDuration   = 30;
    private static final int maxDuration   = 6*60;
    private static final int minTime       = 8*60;    
    private static final int maxTime       = minTime + 12*60;
            
    public int    timeStart;
    public int    timeEnd;
    public int    subject;
    public String lessonData;
    
    public Lesson (int timeStart, int timeEnd, int subject, String lessonData){
        this.timeStart  = timeStart;
        this.timeEnd    = timeEnd;
        this.subject    = subject;
        this.lessonData = lessonData;
              
        if(!this.isLegal()) throw new Error("Illegal Lesson created!\n"+this.toString());        
    }
    
    public Lesson (String data){
        String[] lesson  = data.split(",");
        this.timeStart  = Integer.parseInt(lesson[0]);
        this.timeEnd    = Integer.parseInt(lesson[1]);
        this.subject    = Integer.parseInt(lesson[2]);
        this.lessonData = lesson[3];
        
        if(!this.isLegal()) throw new Error("Illegal Lesson created!\n"+this.toString()); 
    }
    
    public double durationHours(){
        return (double)(durationMinutes()) / 60.0;
    }
    
    public int durationMinutes(){
        return this.timeEnd - this.timeStart;
    }
    
    public double position(Moment m){
        double center = (double)(this.timeEnd + this.timeStart)/2;
        return (m.time - center)/durationMinutes()*2;
    }
    
    @Override
    public int compareTo(Lesson b) {
        return this.timeStart - b.timeStart;
    }
    
    @Override
    public String toString (){
        return Integer.toString(timeStart)+","+
               Integer.toString(timeEnd)+","+
               Integer.toString(subject)+","+
               lessonData;
    }
    
    public Lesson overlap (Lesson a){
        if(this.timeEnd - a.timeStart > 0 && a.timeEnd - this.timeStart > 0)
            return new Lesson (Math.max(a.timeStart, this.timeStart), Math.min(a.timeEnd, this.timeEnd), -1, "BREAK");
        else return null;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Lesson)) return false;
        Lesson l = (Lesson)o;
        if(this.timeStart != l.timeStart) return false;
        if(this.timeEnd   != l.timeEnd)   return false;
        if(this.subject   != l.subject)   return false;
        if(!this.lessonData.equals(l.lessonData)) return false;
        return true;
    }
    
    @Override
    public int hashCode(){
        int hash = 2;
        hash += timeStart;
        hash += timeEnd;
        hash += subject;
        hash += lessonData.hashCode();
        return hash;
    }
    
    public static String intToTime (int time){
         String hours   = String.valueOf(time/60);
         String minutes = String.valueOf(time%60);
         if(time/60 > 12)
             hours = String.valueOf(time/60 -12);
         if(minutes.length() < 2)
             minutes = "0"+minutes;
         return hours+":"+minutes;     
    }
    
    public static int timeToInt (String time){
        String[] temp = time.split(":");
        int m=0, h=0;
        try{
            if(temp.length == 1){
                m = Integer.parseInt(temp[0]);
            }
            else {
                h = Integer.parseInt(temp[0]);
                System.out.println("h="+h);
                m = Integer.parseInt(temp[1]);
            }
            if(temp.length > 2) throw new Exception();
            if(h < 0 || h > 21) throw new Exception();
            if(m < 0 || m > 61) throw new Exception();
        }catch(Exception e) {JOptionPane.showMessageDialog(null, "That is not a real time", "ERROR!", JOptionPane.PLAIN_MESSAGE);}
        return h*60 + m;
    }
    
    public String durationString(){
        return intToTime(this.timeStart)+" - "+intToTime(timeEnd);
    }
    
    public Double pointLesson(Moment m){
        double tbr = (double)(m.time - this.timeStart)/(double)this.durationMinutes();
        if(tbr >= 0.0 && tbr <= 1.0) return tbr;
        else return null;
    }
    
    public void shift(int time){
        this.timeStart += time;
        this.timeEnd += time;
        this.legaliseLesson();
    }
    
    public void stretch(Moment now, int time){
        //now is the time pos of mouse, time is the amount an direction to strectch by
        //time can be negative of positive depending on which direction you want to change by
        
        //if this.pos(now) is not inside the interval {-1.0, 1.0} then something is very wrong.   
        if(this.position(now) > 1.0 || this.position(now) < -1.0) return;
        
        if(this.position(now) < 0)
            //if pos less than zero, lesson is stretched up.
            this.timeStart += time;
        else if(this.position(now) > 0)
            this.timeEnd += time;
        this.legaliseLesson();
    }
    
    public boolean isAbove(Lesson l){
        return this.timeEnd > l.timeStart;
    }
    
    public boolean isBelow(Lesson l){
        return this.timeStart < l.timeEnd;
    }
    
    public boolean isLegal(){
        if(this.timeStart < minTime) return false;
        if(this.timeEnd > maxTime) return false;
        if(this.durationMinutes() < minDuration) return false;
        if(this.durationMinutes() > maxDuration) return false;
        return true;
    }
    
    private void legaliseLesson(){
        if(this.durationMinutes() < minDuration) this.timeEnd += (minDuration - this.durationMinutes());
        if(this.durationMinutes() > maxDuration) this.timeEnd += (maxDuration - this.durationMinutes());
        if(this.timeStart < minTime) this.shift(minTime - this.timeStart);
        if(this.timeEnd > maxTime) this.shift(maxTime - this.timeEnd);
        
    }
}
