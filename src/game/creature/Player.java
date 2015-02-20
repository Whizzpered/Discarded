
/*
 * Here we are
 */

package game.creature;

import game.main.Game;
import game.main.Gui;
import game.main.Inventory;
import game.object.Bullet;
import game.object.Equipment;
import game.world.Block;
import game.world.Room;
import java.awt.Point;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends Creature {
    
    boolean prev;
    public Gui gui = new Gui(this);
    public  boolean invent;
    public Inventory shop;
    public Equipment[] items = new Equipment[10];
    public Point [] loc_items = new Point[10];
    
    public Player(){
        super(200,200);  
        roomloc = 1;
        ranged = true;
        dir = 1;
        dmg = 1;
        hp = 10;
        xp = 500;
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in Player: cannot load images");
        }
        for(int i = 0; i < 10; i++){
            items[i] = new Equipment();
            loc_items[i] = new Point(102+(i>4?i-4:i+1)*50, 120+((int)(i/5)+1)*50);
        }
        
    }
    
    @Override
    public void tick(Game game) {
        super.tick(game);
        interaction(game.room);
        cross(game);
        if(hp<=0){
            
        }
    }
    

    public void initSprites() throws SlickException{
        File[] fList;
        File F = new File("res/player/");
        String path = "res/player/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length()-4);
            images.put(wut, new Image(path + fList[i].getName()));
        }
    }
    
    public void cross(Game game) {
        if (Block.block[game.room.get(Math.round((float)(x-16) / (float) game.room.size), (int) y / game.room.size, 1)].doorr && dir == 1){
            roomloc ++;
            try {
                if(!prev){
                    game.prevRoom = game.room;  
                    game.room = new Room(roomloc, (int)y/game.room.size);      
                }
                else {
                    Room r = game.room;
                    game.room = game.prevRoom;
                    game.prevRoom = r;
                }
                if(prev)prev = false;
            } catch (SlickException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            x = 64;
            game.cam_x = (int) x;
        } 
        if (Block.block[game.room.get(Math.round((float)(x-16) / (float) game.room.size), (int) y / game.room.size, 1)].doorl && roomloc >1 && dir == -1){
            if(!prev){
                roomloc --;
                Room r = game.room;
                game.room = game.prevRoom;
                game.prevRoom = r;
                prev = true;
                x = 40*64;
                game.cam_x = (int) x;
            }
        }
    }
    
    
    public void interaction(Room world) { 
        if (Block.block[world.get(Math.round((float)x / (float) world.size), (int) y / world.size + 1, 1)].solid) {
            onGround = true;
        }else onGround= false;
        
        if (onGround) { // World.size == 64
            y = ((int) y / world.size) * world.size;
            vy = 0;
        }
        if (Block.block[world.get(Math.round((float) x / (float) world.size), (int) y / world.size, 1)].solid) {
            y = ((int) y / world.size) * world.size + world.size;
            vy = 0;
        }
        if (Block.block[world.get((int) x / world.size + 1, Math.round((float) y / (float) world.size), 1)].solid) {
            x = ((int) x / world.size) * world.size;
        }

        if (Block.block[world.get((int) x / world.size, Math.round((float) y / (float) world.size), 1)].solid) {
            x = ((int) x / world.size) * world.size + world.size;
        }
        if (Block.block[world.get((int) (x+32) / world.size, Math.round((float) (y+32) / (float) world.size), 1)].stairs){
            ay = 0;
            vy = 0;
            onStairs = true;
        } else {
            ay = 0.4f;
            onStairs = false;
        }
    }
    
    @Override
    public void shoot(Game game) {
        if (ranged) {
            game.room.bullets.add(new Bullet(x+dir*32,y+32, dir, this));
        }
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.green);
        if(dir==1)g.drawImage(images.get("robo_right"), (float) x, (float) y);
        else g.drawImage(images.get("robo_left"), (float) x, (float) y);
    }
}
