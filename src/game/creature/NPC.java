/*
 * Here we are
 */
package game.creature;

import game.main.Game;
import game.main.Timer;
import game.object.Bullet;
import game.object.Modifier;
import game.object.Wearing;
import game.world.Block;
import game.world.Room;
import java.io.File;
import java.util.HashMap;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class NPC extends Creature{
    
    public double ex,ey, sx, sy;
    double dist;
    public boolean lost;
    int lostim, type;
    Random r = new Random();
    byte condition = 0; /*
                              *0 is passive
                              *1 is patrool
                              *2 is in pursuit
                              *3 is "I saw corpse T_T"
                        */
    
    public NPC(int x, int y,int type, int loc){
        super(x, y);
        this.type = type;
        set(loc);
        assign();  
    }
    
    public void set(int loc){
        hp = 10 + (loc-1)*20;
        if(type==1)ranged = true;
        else ranged = false;
        dir = 1;
        dmg = 1 + (loc-1)*2 + (ranged?0:(int)((loc-1)*4/3));
        roomloc = loc;
        sx = x; sy = y;
    }
    
    public void assign() {
        timer = new HashMap<>();
        timer.put("shoot", new Timer("shoot", 60));
        timer.put("attention", new Timer("attention", 300));
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in NPC: cannot load images");
        }
    }
    
    public void initSprites() throws SlickException{
        images = new HashMap<>();
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
        int ra = r.nextInt(4);
        game.room.objects.add(new Modifier(x,y,1));
        if(r.nextInt(10)>7)game.room.objects.add(new Modifier(x,y,2));
        if(r.nextInt(10)>6)game.room.objects.add(new Wearing(x+32,y,ra+1));
        game.room.npcies.remove(this);
    }
    
    @Override
    public void tick(Game game) {
        super.tick(game);
        if(hp<=0)die(game);
        interaction(game.room);
        ai(game);
    }
    
    public void ai(Game game) {
        double distx = Math.abs(x-game.player.x), disty = Math.abs(y-game.player.y);
        
        if(distx < 600 && disty<256){    // If Hero will be nearly, AI will go to catch him
            ex = x>game.player.x?game.player.x + (ranged?100:46):game.player.x - (ranged?100:46); //This is a fiend
            ey = game.player.y;
            condition = 2;
        }    
        
        if((condition == 2 && disty<512 && disty>210) || game.player.onStairs){
            if(y-game.player.y<0)
            for(int i = 0;i < 8; i++){
                int type = game.room.get((int)(x/64) + i-4, (int)(x/64)+ 2, 1);
                if(Block.block[type].stairs){
                    ex = (int)(x/64)*64 + (i-4)*64;
                }
            }
            else {
                for(int i = 0;i < 8; i++){
                int type = game.room.get((int)(x/64) + i-4, (int)(x/64), 1);
                if(Block.block[type].stairs){
                    ex = (int)(x/64)*64 + (i-4)*64;
                    if(Block.block[game.room.get((int)(x/64), (int)(x/64), 1)].stairs)vy = -4;
                }
            }
            }
        }
        if(onStairs)vx =0;
        if(onStairs && condition ==2){
            if(y-game.player.y<0){
                y++;
                vx=0;
            }
            else {
                y--;
                vx=0;
            }
        }
        
        if((distx>600 || disty>460) && condition==2){  //If Hero escaped, stop and wait some time
            condition = 0;
            start("attention");
        } 
        if(ready("attention") && condition == 0) ex = sx;
        
        if(ex>0)moving(game.room, game);
        
        if(condition==2){
            if(ranged){
                if(distx<=300 && disty<=128 && ready("shoot")){
                    dir = -(int)((x-game.player.x)/distx);
                    shoot(game);
                    start("shoot");
                }
            }
            else {
                if(distx<=70 && disty<=128 && ready("shoot")){
                    dir = -(int)((x-game.player.x)/distx);
                    beat(game);
                    start("shoot");
                }
            }
        }
    }
    
    public void interaction(Room room) {
        int bx = (int)((x)/room.size), dx = (int) Math.round(x/room.size);
        int by = (int)(y/room.size), dy = (int) Math.round(y/room.size);
        
        if(Block.block[room.get(dx,by + 2,1)].solid){ //Collision from bellow
            vy = 0;
            y = by*room.size;
            onGround = true;
        }else onGround = false;
        
        if(Block.block[room.get(dx,by,1)].solid){ //Collision from above
            vy = 0;
            y = (by+1)*room.size;
        }
        
        if(Block.block[room.get(bx,dy,1)].solid || Block.block[room.get(bx,dy+1,1)].solid){
            vx = 0;
            x = (bx+1)*room.size;    
        }
        
        if(Block.block[room.get(bx+1,dy,1)].solid || Block.block[room.get(bx+1,dy+1,1)].solid){
            vx = 0;
            x = bx*room.size;    
        }
        
        if (Block.block[room.get((int) (x+32) / room.size, Math.round((float) (y+96) / (float) room.size),1)].stairs){
            ay = 0;
            vy = 0;
            onStairs = true;
        } else {
            ay = 0.4f;
            onStairs = false;
        }
                
    }
    

    public void moving(Room world, Game game) {
            if (x-ex>=1)moveLeft(world);
            else if(x-ex<=-1)moveRight(world);
            else {
                vx = 0;
            }
        
    }
    
    public void beat(Game game) {
        if (!ranged){
            game.room.bullets.add(new Bullet(x+dir*32, y+32, dir, this,2));
            vx = (int)((x-game.player.x)/Math.abs(x-game.player.x)) * 32;
        }
    }
    
    @Override
    public void shoot(Game game) {
        if (ranged){
            game.room.bullets.add(new Bullet(x+dir*32, y+32, dir, this,1));
        }
    }
    
    public void moveLeft(Room world) {
        vx = -1;
        dir = -1;
        if(Block.block[world.get((int)((x)/world.size)-1,(int)(y/world.size) + 1,1)].solid)jump(world);
        if(onGround && !Block.block[world.get((int)(x/world.size)-1,(int)(y/world.size) + 2,1)].solid)jump(world);
        if(vy!=0)vx = -1;
    }
     
    public void moveRight(Room world) {
        vx = 1;
        dir = 1;
        if(Block.block[world.get((int)((x)/world.size)+1,(int)(y/world.size) + 1,1)].solid)jump(world);
        if(onGround && !Block.block[world.get((int)(x/world.size)+1,(int)(y/world.size) + 2,1)].solid)jump(world);
        if(vy!=0)vx = 1;
    }
 
    
    public void render(Graphics g, Game game){
        
        if(Math.abs(x - game.player.x)<=(Display.getWidth()/2)+360 && Math.abs(y - game.player.y)<=(Display.getHeight()/2)+360){
                if(dir==1)g.drawImage(images.get(type==1?"rob_right":"robot_right"), (float)x-29,(float)y);
                else g.drawImage(images.get(type==1?"rob_left":"robot_left"), (float)x-29,(float)y);
        }
    }
}
