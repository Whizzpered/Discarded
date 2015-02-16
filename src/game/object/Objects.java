/*
 * Here we are
 */
package game.object;

import game.main.Game;
import game.world.Block;
import game.world.Room;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Objects {
    public double x, y, vy, ay = 0.4f;
    
    public Objects(double x, double y, int type) {
        this.x = x;
        this.y = y;
    }
    
    public void tick(Game game) {
        vy+=ay;
        y+=vy;
        
        collision(game.room);
        
        if(Math.abs(game.player.x-x)<=32){
            game.player.xp += 200;
            game.room.objects.remove(this);
        }
    }
    
    public void collision(Room world) {
       if (Block.block[world.get(Math.round((float)x / (float) world.size), (int) (y+8) / world.size, 1)].solid) { // World.size == 64
            y = ((int) y / world.size) * world.size + 64 - 8;
            vy = 0;
        }
    }
    
    public void render(Graphics g, int camx, int camy, int ssizex, int ssizey, Game game){
        if(Math.abs(x - game.player.x)<=(ssizex/2)+200 && Math.abs(y - game.player.y)<=(ssizey/2)+200){
                g.setColor(Color.white);
                g.fillRect((float)x,(float)y,8,8);
        }
    }
    
}
