
/*
 * Here we are
 */

package game.creature;

import game.main.Game;
import game.main.Gui;
import game.main.Inventory;
import game.main.Timer;
import game.object.Bullet;
import game.object.Equipment;
import game.object.Wearing;
import game.world.Block;
import game.world.Room;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends Creature {
    
    boolean prev;
    public Gui gui;
    public  boolean invent, take, full, onBase, pause;
    public Inventory inventory;
    public String anim = "stand";
    public Equipment[] items = new Equipment[14];
    public Point [] loc_items = new Point[14];
    public HashMap<String, Animation> animations = new HashMap<>();
    public Equipment hand;
    public int duck=0;

    public Player(){
        super(200,200);  
        set();
        assign();
        for(int i = 0; i < 10; i++){
            loc_items[i] = new Point(302+(i>4?i-4:i+1)*50, 120+((int)(i/5)+1)*50);
        }
        for(int i = 10; i<14; i++){
            loc_items[i] = new Point(502+(i-9)*50, 120);
        }
            
    }
    
    public void set() {
        roomloc = 1;
        ranged = true;
        dir = 1;
        dmg = 1;
        hp = 10;
        maxhp=10;
        xp = 100;
    }
    
    public void assign() {
        animations = new HashMap<>();
        try {
            initAnim("left");
            initAnim("right");
        } catch (Exception ex) {
            System.out.println("Error in Player: cannot load images");
        }
        dir = 1;
        gui = new Gui(this);
        inventory = new Inventory();
        initTimers();
    }
    
    public void initTimers() {
        timer = new HashMap<>();
        timer.put("take", new Timer("take", 60));
        timer.put("shoot", new Timer("shoot", 60));
        timer.put("wait", new Timer("wait", 60));
        timer.put("button", new Timer("button",20));
    }
    
    @Override
    public void tick(Game game) {   
        if(!pause){
            super.tick(game);
            interaction(game.room);
            cross(game);
            if(hp<=0 || vy>=60){
                try {
                    game.playerDie();
                } catch (SlickException ex) {
                    System.out.println("Cant Die");
                }
            }
        }
    }

    
    public boolean addItem(Wearing item){
        for(int i = 0; i < items.length-4;i++){
            if(items[i]==null){
                items[i] = new Equipment(item.type, this); 
                full =  false;
            return true;
        }
    }
        full = true;
        return false;
    } 
    
    
    public void initAnim(String dir) throws SlickException {
        File file = new File("res/player/"+dir);
        File[] f = file.listFiles();
        for(int i = 0; i < f.length;i++){
            String name = f[i].getName();
            File[] fil = new File("res/player/"+dir +"/" + f[i].getName()).listFiles();
            Image[] images = new Image[fil.length];     
            for(int j = 0; j <fil.length;j++){  
                String path = fil[j].getPath();
                images[j] = new Image(path);
                animations.put(dir+"_"+name, new Animation(images,125));
            }
        }
    }
    
    public Animation getDir(String name) {
        if(dir==-1)return animations.get("left_"+name);
        else return animations.get("right_"+name);
    }
    
    
    public void cross(Game game) {
        if (Block.block[game.room.get(Math.round((float)(x-16) / (float) game.room.size), (int) y / game.room.size, 1)].doorr && dir == 1){
            roomloc ++;
            try {
                if(!prev){
                    game.prevRoom = game.room;  
                    game.room = new Room(roomloc, (int)y/game.room.size);
                    game.base = true;
                }
                else {
                    Room r = game.room;
                    game.room = game.prevRoom;
                    game.prevRoom = r;
                    game.base = true;
                }
                if(prev)prev = false;
            } catch (SlickException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            x = 64;
            game.cam_x = (int) x;
        } 
        if (Block.block[game.room.get(Math.round((float)(x-16) / (float) game.room.size), (int) y / game.room.size, 1)].doorl && roomloc >1 && dir == -1){
            if(!prev && game.prevRoom!=null){
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
    
    
    public void interaction(Room room) { 
        int bx = (int)((x)/room.size), dx = (int) Math.round(x/room.size);
        int by = (int)(y/room.size), dy = (int) Math.round(y/room.size);
        
        if(Block.block[room.get(dx,by + 2-duck,1)].solid){ //Collision from bellow
            vy = 0;
            y = by*room.size;
            onGround = true;
        }else onGround = false;
        
        if(Block.block[room.get(dx,by,1)].solid){ //Collision from above
            vy = 0;
            y = (by+1)*room.size;
        }
        
        if(Block.block[room.get(bx,dy,1)].solid || Block.block[room.get(bx,dy+1-duck,1)].solid){
            vx = 0;
            x = (bx+1)*room.size;    
        }
        
        if(Block.block[room.get(bx+1,dy,1)].solid || Block.block[room.get(bx+1,dy+1-duck,1)].solid){
            vx = 0;
            x = bx*room.size;    
        }
        
        if (Block.block[room.get((int) (x+32) / room.size, Math.round((float) (y+96-duck*64) / (float) room.size),1)].stairs){
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
            game.room.bullets.add(new Bullet(x+dir*32,y+32, dir, this,1));
        }
    }
    
    public void crash(Game game) {
        if(Block.block[game.room.get((int)(x)/game.room.size + vx, Math.round((float)(y)/(float)game.room.size)+1, 1)].destroyable)
            game.room.destroy((int)x/game.room.size + vx, Math.round((float)(y)/(float)game.room.size)+1);
    }
    
    @Override
    public void render(Graphics g) {
        if(!pause){
        getDir(anim).draw((float)x-32, (float)y);
        for(int i = 10; i < 14;i++){
            if(items[i]!=null){
                items[i].render(g, (int)x+16, (int)y);
            }
        }}
    }
    
    public void staticRender(Graphics g, int x ,int y) {
        if(!pause){
        getDir("stand").draw((float)x-32, (float)y);
        for(int i = 10; i < 14;i++){
            if(items[i]!=null){
                items[i].render(g, (int)x+16, (int)y);
            }
        }
    }
    }
}
