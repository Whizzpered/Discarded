/*
 * Here we are
 */
package game.object;

import game.main.Game;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Modifier extends Objects {
        
    int type;
    
    public Modifier(double x, double y, int type) {
        super(x,y,1);
        this.type = type;
    }
    
    @Override
    public void tick(Game game) {
        super.tick(game);
        
        if(Math.abs(game.player.x-x)<=32 && Math.abs(game.player.y-y)<=128){
            if(type ==1)game.player.xp += 50;
            else game.player.hp = game.player.maxhp;
            game.room.objects.remove(this);
        }
    }
    
    @Override
    public void render(Graphics g, Game game) {
        if(Math.abs(x - game.player.x)<=(Display.getWidth()/2)+200 && Math.abs(y - game.player.y)<=(Display.getHeight()/2)+200){
                if(type==1)g.setColor(Color.white);
                else g.setColor(Color.green);
                g.fillRect((float)x,(float)y,8,8);
        }   
    }
            
}
