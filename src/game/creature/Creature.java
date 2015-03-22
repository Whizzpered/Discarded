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
    public int vx, hp, maxhp, dir, roomloc, xp, dmg, energy = 100;
    public boolean onStairs, onGround;
    public HashMap <String, Timer> timer;
    public HashMap <String,Image> images;
    
    
    public boolean ranged, sprint;
    
    public Creature(int x, int y) {
        this.x = x;
        this.y = y;
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
    
    public void tick(Game game) {
        if (sprint &&  energy>=1){
            x+=vx*4;
            energy--;
        }
        else x+=vx*2;
        vy+=ay;
        y+=vy;
        if(energy<100 && !sprint)energy++;
        checkCounters();        
    }
    
    public void jump(Room world) {
        if(onGround)
            vy=-12;
    }
    
    
    public void shoot(Game game) {
        if (ranged){
            game.room.bullets.add(new Bullet(x+dir*16, y+32, dir, this,1));
        }
    }
    
    public void render(Graphics g) {
    }
}
