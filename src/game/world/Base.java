/*
 * Here we are
 */
package game.world;

import game.creature.Player;
import game.main.Game;
import game.main.Timer;
import game.object.Equipment;
import game.object.Wearing;
import java.io.File;
import java.util.HashMap;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Base {
    public Timer butt = new Timer("butt", 20);
    public int x=1,y=1, repCost;
    public boolean invent;
    Player player;
    Equipment rand;
    Random random = new Random();
    static HashMap<String,Image> images = new HashMap<>();
    
    public Base(Game game) {
        rand = new Equipment(random.nextInt(4)+1, game.player);
        try {initSprites();} catch (Exception ex) {
            System.out.println("Error in Base: cannot load images");
        }
    }
    
    public int number(int x, int y) {
        return((x-1)*4 + y);
    }
    
    public void initSprites() throws SlickException{
        File[] fList;
        File F = new File("res/gui/");
        String path = "res/gui/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length()-4);
            images.put(wut, new Image(path + fList[i].getName()));
        }
    }
    
    public void button(Game game) {
        if((Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_UP)) && butt.is()){
            if(y>1)y--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)) && butt.is()){
            if(x==1?y<3:y<2)y++;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)) && butt.is()){
            if(x>1)x--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) && butt.is()){
            if(x<2)x++;
            butt.start();
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && butt.is()){
            game.player.onBase = false;
            game.men.butt.start();
            butt.start();
        }
        
        if((Keyboard.isKeyDown(Keyboard.KEY_I)) && player.inventory.butt.is()){
            player.invent = true;
            game.shop = true;
            player.inventory.butt.start();
            butt.start();
        }
        
        if((Keyboard.isKeyDown(Input.KEY_ENTER) && butt.is())){
            switch(y){
                case(1):
                    if(x==1 && game.player.xp>=repCost){
                        game.player.xp-=repCost;
                        game.player.hp = game.player.maxhp;
                    }
                    if(x==2){
                        game.save();
                    }
                    break;
                case(2):
                    if(x==1 && game.player.xp>=rand.cost){
                        game.player.addItem(new Wearing(game.player.x,game.player.y,rand.type));
                        game.player.xp-=rand.cost;
                        rand = new Equipment(random.nextInt(4)+1, game.player);
                    }
                    if(x==2){
                        game.load();
                    }
                break;
                    case(3):game.player.onBase = false;
                        game.men.butt.start();
                        butt.start();
                        break;
                        
                    
            }
            butt.start();
        }
        repCost = (game.player.maxhp-game.player.hp) * 10;
        player = game.player;
    }
            
    public void render(Graphics g){
        int xsize = Display.getWidth();
        int ysize = Display.getHeight();
        
        g.setColor(Color.green);
        for(int i = 0; i < 3; i++){
            g.drawRect(xsize/2-200, 120 + i*80, 400, 50);
        }    
        
        g.setColor(Color.gray);
        if(x==1)g.fillRect(xsize/2-200, 40 + y*80, 400, 50);
        else g.fillRect(xsize-202, ysize/2-128 + y*64, 50, 50);
        
        g.setColor(repCost==0?Color.green:Color.yellow);
        g.drawString("Repair yourself: " + repCost, xsize/2-100,120);
        g.setColor(Color.green);
        g.drawString("Buy random item: " + rand.cost, xsize/2-100,210);
        g.drawString("Leave the base", xsize/2-100,300);
        
            images.get("save").draw(xsize-200, ysize/2 - 64);
            g.drawString("Save",xsize-200, ysize/2 - 84);
            images.get("load").draw(xsize-200, ysize/2);
            g.drawString("Load",xsize-200, ysize/2 + 58);
        
    }
    
}
