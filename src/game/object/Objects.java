/*
 * Here we are
 */
package game.object;

import game.main.Game;
import game.world.Block;
import game.world.Room;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Objects {
    public double x, y, vy, ay = 0.4f;
    public int type;
    
    
    public Objects(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public void tick(Game game) {
        vy=ay*10;
        y+=vy;
        
        collision(game.room);
    }
    
    public void collision(Room world) {
       if (Block.block[world.get(Math.round((float)x / (float) world.size), (int) (y+(type==5?30:8)) / world.size, 1)].solid) { // World.size == 64
            y = ((int) y / world.size) * world.size + 64 - (type==5?30:8);
            vy = 0;
        }
    }
    
    public void render(Graphics g, Game game){
        
    }
    
}
