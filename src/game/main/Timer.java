/*
 * Here we are
 */
package game.main;

import java.io.Serializable;

/*
 * A counter to make some things for fixed time
 */
public class Timer implements Serializable{
    public int period, tick;
    String name;
    public Timer(String Name,int period){
        name=Name;
        this.period = period;
    }

    public void start() {
        tick = period;
    }
    
    public int get() {
        return tick;
    }   
    
    public boolean is(){
        if(tick == 0) return true;
    else return false;
    }
    
    public void tick(){
        if(tick > 0) tick--;
    }
}
