/*
 * Here we are
 */

package game.creature;

import game.main.Game;
import game.main.Timer;
import game.object.Bullet;
import game.world.Room;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Creature {
    
    public double x, y, c, vy, ay = 0.4f;
    public int vx, hp, maxhp, dir, roomloc, xp, dmg;
    public boolean onStairs, onGround;
    HashMap <String, Timer> timer = new HashMap<>();
    HashMap<String,Image> images = new HashMap<>();
    
    
    public boolean ranged, sprint;
    
    public Creature(int x, int y) {
        this.x = x;
        this.y = y;
        timer.put("shoot", new Timer("shoot", 60));
        timer.put("attention", new Timer("attention", 200));
    }
    
    protected void checkCounters(){
    for(Map.Entry<String, Timer> entry : timer.entrySet()) {
        Timer cnt = entry.getValue();
        cnt.tick();
        }
    }   
    
    public void start(String att){
        timer.get(att).start();
    }
    
    public boolean ready(String att){
        return timer.get(att).is();
    }
    
    public int get(String att){
        return timer.get(att).get();
    }
    
    public void tick(Game game) {
        if (sprint)x+=vx*6;
        else x+=vx*4;
        vy+=ay;
        y+=vy;
        checkCounters();
        if(vy>8)vy=8;        
    }
    
    public void jump(Room world) {
        if(onGround)
            vy=-12;
    }
    
    public void smash(Room world) {
        if(!onGround)
            vy+=1;
    }
    
    public void shoot(Game game) {
        if (ranged){
            game.room.bullets.add(new Bullet(x+dir*16, y+32, dir, this));
        }
    }
    
    public void render(Graphics g) {
    }
}
