/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package timetablecheckfx;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mitch
 */
class TimetableList {
    private List<Timetable> tList;
    
    public TimetableList(List<Timetable> tList){
        this.tList = tList;
    }
    
    public TimetableList(){
        tList = new ArrayList<>();
    }
    
    public TimetableList fromFile(File file){
        try{
            if(!file.exists()){
                new File("Timetables").mkdir();
                file.createNewFile();
                Timetable mitch = new Timetable("MitchSem2"
                        + "/ENGN1217;COMP1110;MATH2307;COMP2610;"
                        + "/540,600,0,EMPTY;720,780,1,EMPTY;"
                        + "/540,600,2,EMPTY;600,660,0,EMPTY;780,840,1,EMPTY;840,900,3,EMPTY;"
                        + "/780,840,0,EMPTY;840,900,3,EMPTY;960,1080,3,EMPTY;600,660,0,EMPTY;"
                        + "/540,600,2,EMPTY;600,660,0,EMPTY;"
                        + "/540,600,2,EMPTY;660,780,1,EMPTY;780,840,1,EMPTY;840,900,2,EMPTY;");
                List<Timetable> tList = new ArrayList<>();
                tList.add(mitch);
                return new TimetableList(tList);
            }
            
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            
            while ((strLine = br.readLine()) != null)   {              
                Timetable temp = new Timetable(strLine);
                tList.add(temp);
            }
            in.close();
        }
        catch (Exception e){
            System.err.println("Read file error: Program currently eating a bag of hot dicks." + e.getMessage());}
        return this;
    }
    
    public void addTimetable(Timetable table){
        for(Timetable t : this.tList)
            if(t.name.toUpperCase().equals(table.name.toUpperCase()))
                return;
        
        this.tList.add(table);
    }
    
    public List<Timetable> all(){
        return tList;
    }
    
    public Timetable getTimetable(int i){
        if(i < this.tList.size() && i >= 0)
            return tList.get(i);
        else return null;
    }
    
    public Timetable getTimetable(String s){
        for(Timetable t : this.tList)
            if(t.name.equals(s))
                return t;
 
        return null;
    } 
    
    public List<String> getNames(){
        List<String> tbr = new ArrayList<>();
        for(Timetable t : this.tList)
            tbr.add(t.name);
        return tbr;
    }
    
    public static void removeTimetable(TimetableList list, File f){
        List<Timetable> allTables = new TimetableList().all();
        allTables.removeAll(list.tList);
        
        try{
            //I just removed all the text in a file by deleting it and making a new one
            //Thats pretty fucky
            System.out.println(f.delete());
            f.createNewFile();
        }catch(Exception e){}
        for(Timetable t : allTables)
            t.saveTimetable(f);           
    }
    
    public static void removeTimetable(Timetable table, File f){
        List<Timetable> allTables = new TimetableList().all();
        allTables.remove(table);
        
        try{
            //I just removed all the text in a file by deleting it and making a new one
            //Thats pretty fucky
            System.out.println(f.delete());
            f.createNewFile();
        }catch(Exception e){}
        for(Timetable t : allTables)
            t.saveTimetable(f);             
    }
    
}
