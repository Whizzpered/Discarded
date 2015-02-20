/*
 * Here we are
 */
package game.creature;

import game.main.Game;
import game.object.Bullet;
import game.object.Objects;
import game.world.Block;
import game.world.Room;
import java.io.File;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class NPC extends Creature{
    
    public double ex,ey, sx, sy;
    double dist;
    public byte condition; /*   0 is a passive
                                1 is a patrool
                                2 is a agressive  */   
    
    public NPC(int x, int y, int loc){
        super(x, y);
        this.hp = 10;
        this.ranged = true;
        dir = 1;
        dmg = 1;
        roomloc = loc;
        sx = x; sy = y;
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in NPC: cannot load images");
        }
    }
    
    public void initSprites() throws SlickException{
        File[] fList;
        File F = new File("res/npc/");
        String path = "res/npc/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length()-4);
            images.put(wut, new Image(path + fList[i].getName()));
        }
    }
    
    
    public void die(Game game) {
        game.room.objects.add(new Objects(x,y,1));
        game.room.npcies.remove(this);
    }
    
    @Override
    public void tick(Game game) {
        super.tick(game);
        if(hp<=0)die(game);
        interaction(game.room, game);
        findEnemy(game);
        ai(game);
    }
    
    public void ai(Game game) {
        if((Math.abs(y-ey)>=256 && condition ==2) || ((Math.abs(y-ey)>dist) && condition ==2) ){
             condition = 0;
            start("attention");
        
        }
        if((Math.abs(y-ey)>=256 || Math.abs(x-ex)>=dist) && ready("attention")){
            ex = sx;
        }
        if(ex!=0) moving(game.room,game);
        double range = 464;
        if(Math.abs(x-ex)<=range &&  ready("shoot") && Math.abs(y-ey)<=64){
            shoot(game);
            start("shoot");
        }
        if(Math.abs(y-ey)<256 && Math.abs(y-ey)>=128){
                ex = ex;
        }
    }
    
    public void interaction(Room world, Game game) {
        
        if (Block.block[world.get(Math.round((float)x / (float) world.size), (int) y / world.size + 1, 1)].solid) {
            onGround = true;
        }else onGround= false;
        
        
       if (onGround) { // World.size == 64
            y = ((int) y / world.size) * world.size;
            vy = 0;
        }
        if (Block.block[world.get(Math.round((float) x / (float) world.size), (int) y / world.size,1)].solid) {
            y = ((int) y / world.size) * world.size + world.size;
            vy = 0;
        }
        if (Block.block[world.get((int) x / world.size + 1, Math.round((float) y / (float) world.size),1)].solid) {
            x = ((int) x / world.size) * world.size;
        }

        if (Block.block[world.get((int) x / world.size, Math.round((float) y / (float) world.size),1)].solid) {
            x = ((int) x / world.size) * world.size + world.size;
        }
        if (Block.block[world.get((int) (x+32) / world.size, Math.round((float) (y+32) / (float) world.size),1)].stairs){
            ay = 0;
            vy = 0;
            onStairs = true;
        } else {
            ay = 0.4f;
            onStairs = false;
        }
                
    }
    
    
    public void findEnemy(Game game) {
        dist = 600;
        if(Math.abs(x-game.player.x)<=dist && Math.abs(y-game.player.y)<128&& vy == 0 
                &&(condition<2) && ready("attention")){
            ex = game.player.x;
            ey = game.player.y;
            condition = 2; 
        }
        else if (condition == 2) { dist = 800;
            if(Math.abs(x-game.player.x)<=dist){
                ex = game.player.x;
                ey = game.player.y;
            }
                }
    }
    
           
    
    
    public void moving(Room world, Game game) {
            if (x-ex>=100)moveLeft(world);
            else if(x-ex<=-100)moveRight(world);
            else {
                vx = 0;
            }
        
    }
    
    @Override
    public void shoot(Game game) {
        if (ranged){
            game.room.bullets.add(new Bullet(x+dir*32, y+32, dir, this));
        }
    }
    
    public void moveLeft(Room world) {
        vx = -1;
        dir = -1;
        if(Block.block[world.get((int)(x-32) / world.size, Math.round((float)y/(float)world.size), 1)].solid){
            jump(world);
        }
        if(onGround && !Block.block[world.get(Math.round((float)(x-32)/(float)world.size), (int)(y / world.size)+1, 1)].solid){
            jump(world);
        }
        if(vy!=0)vx = -1;
    }
     
    public void moveRight(Room world) {
        vx = 1;
        dir = 1;
        if(Block.block[world.get((int)(x+96) / world.size + 1, Math.round((float)y/(float)world.size), 1)].solid){
            jump(world);
        }
        if(onGround && !Block.block[world.get(Math.round((float)(x+32)/(float)world.size), (int)(y / world.size)+1, 1)].solid){
            jump(world);
        }
        if(vy!=0)vx = 1;
    }
 
    
    public void render(Graphics g, Game game){
        if(Math.abs(x - game.player.x)<=(Display.getWidth()/2)+360 && Math.abs(y - game.player.y)<=(Display.getHeight()/2)+360){
                if(dir==1)g.drawImage(images.get("robot_right"), (float)x,(float)y);
                else g.drawImage(images.get("robot_left"), (float)x,(float)y);
        }
    }
}
