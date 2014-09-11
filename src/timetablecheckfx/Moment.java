package timetablecheckfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Mitch
 */
public class Moment {
    final String[] daysOfWeek = "Monday, Tuesday, Wednesday, Thursday, Friday".split(", ");
    int day;
    int time;
    
    public Moment(){
        this.day = -1;
        this.time = -1;
    }

    public Moment(int day, int time) {
        this.day = day;
        this.time = time;
    }

    @Override
    public String toString() {
        if(this == null) return "empty moment";
        else return (day >= 0 && day < 5 ? daysOfWeek[day] : day)+", "+Lesson.intToTime(time);
    }
    
    
}
