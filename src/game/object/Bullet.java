/*
 * Here we are
 */
package game.object;

import game.creature.Creature;
import game.creature.NPC;
import game.main.Game;
import game.world.Block;
import game.world.Room;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Bullet {
    public int  vx,damage, size = 8,tim = 0, type;
    public double x, y;
    boolean hit = false;
    
    
    public Bullet(double x, double y, int dir, Creature cr, int type) {
        this.x = x;
        this.y = y;
        vx = dir;
        damage = cr.dmg;
        this.type = type;
    }
    
    public void tick(Room room, Game game) {
        if(hit){
            tim++;
        }
        else {
        x += vx*10;
        
        if(x<=-64 || x >= 43 *64 )game.room.bullets.remove(this);
        if(Block.block[room.get((int)x/room.size, Math.round((float)(y-32)/(float)room.size), 1)].solid){ 
            if(Block.block[room.get((int)x/room.size, Math.round((float)(y-32)/(float)room.size), 1)].destroyable)
                if(type==1)room.destroy((int)x/room.size, Math.round((float)(y-32)/(float)room.size));
            game.room.bullets.remove(this);
        }
            for(NPC npc : game.room.getNPCArr()){
            if(Math.abs(npc.x-x)<=16 && npc.y-y>=-128 && npc.y-y<0){
                if(!hit)npc.hp-=damage;
                hit = true;
            }
        }
            if(Math.abs(x-game.player.x)<16 &&game.player.y-y>=-128 && game.player.y-y<0){
                if(!hit)game.player.hp-=damage;
                hit = true;
            }
        }
        if(tim == 70){
                hit  = false;
                tim = 0;
                game.room.bullets.remove(this);
               
            }
    }
           
    
    public void render(Graphics g, Game game){
        if(Math.abs(x - game.player.x)<=(Display.getWidth()/2)+100 && Math.abs(y - game.player.y)<=(Display.getHeight()/2)){
                if(hit){
                    g.setColor(Color.red);
                    g.drawString("-"+damage, (float)(x),(float)(y-tim));
                } else if (type==1) {
                    g.setColor(Color.yellow);
                    g.drawRect((float)x,(float)y,size,size);
                }
        }
    }
    }