/*
 * Here we are
 */
package game.main;

import java.io.File;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class Shop {
    Timer butt = new Timer("butt", 20);
    
    int x=2,y=2;
    
    HashMap<String,Image> images = new HashMap<>();
    
    
    public Shop() {
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in Shop: cannot load images");
        }
    }
    
    public void initSprites() throws SlickException{
        File[] fList;
        File F = new File("res/shop/");
        String path = "res/shop/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length()-4);
            images.put(wut, new Image(path + fList[i].getName()));
        }
    }
    
    public void buttons(Game game){
        if((Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_UP)) && butt.is()){
            if(y>1)y--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)) && butt.is()){
            if(y<3)y++;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)) && butt.is()){
            if(x>1)x--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) && butt.is()){
            if(x<3)x++;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_P)||Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) && butt.is()){
            game.shop = false;
            butt.start();
        }
        
        if((Keyboard.isKeyDown(Input.KEY_ENTER) && butt.is())){
            switch(number(x,y)){
                case (1):
                    if(game.player.xp>=100){
                        game.player.dmg+=3;
                        game.player.xp-=100;
                    }
                    break;
                case (2):
                    
                        if(game.player.xp>=100){
                        game.player.hp+=5;
                        game.player.xp-=100;
                    }
                    break;
            }
             butt.start();
        }
        
        butt.tick();
        
    }
    
    public int number(int x, int y) {
        return((y-1)*6+x);
    }
    
    public void render(Graphics g) {
        g.drawString("Your inventory",700,30);
        g.drawImage(images.get("weapon"),150,120);
        g.drawImage(images.get("armour"),200,120);
        for(int x = 3; x < 6;x++){
            for(int y = 3; y < 5;y++){
                g.drawImage(images.get("empty"),x*50,y*50+20);
        }}
        drawDescription(g);
        g.setColor(Color.gray);
        g.drawImage(images.get("frame"),99 + 50*x, 69 + y*50);
    }
    
        public void drawDescription(Graphics g) {
            g.setColor(Color.green);
            switch(number(x,y)) {
                case (1):
                    g.drawString("Upgrade your weapon, increase damage",300,80);
                    break;
                case (2):
                    g.drawString("Upgrade your armor, increase hit points",300,80);
                    break;
            }
            g.setColor(Color.yellow);
            g.drawString("Upgrades",50,120);
            g.drawString("Backpack",50,170);
        }
}
