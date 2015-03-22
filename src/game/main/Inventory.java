/*
 * Here we are
 */
package game.main;

import game.creature.Player;
import game.object.Equipment;
import game.object.Wearing;
import java.io.File;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class Inventory {
    public Timer butt = new Timer("butt", 20);
    
    int x=2,y=2, armcost=100, dmgcost=100;
    
    transient static HashMap<String,Image> images = new HashMap<>();
    
    
    public Inventory() {
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
            if(y==2&&x>2){
                y=0;x=1;
            } else if(y==2?x<3&&y>1:y>1) 
                y--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)) && butt.is()){
            if(y<(game.player.items.length-4)/5+1){
                if(y==0){
                    x=1;
                    y=2;
                }else y++;
            }
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)) && butt.is()){
            if(y==0 && x==1){
                x=2;
                y=1;
            }
            if(x>1)x--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) && butt.is()){
            if(y==0?x<4:x<(y==1?2:(game.player.items.length-4)/2))x++;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_I)||Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) && butt.is()){
            game.shop = false;
            game.player.invent = false;
            butt.start();
        }
        
        if((Keyboard.isKeyDown(Keyboard.KEY_G)) && butt.is()){
            game.room.objects.add(new Wearing(game.player.x, game.player.y, game.player.items[itemNumber(x-1,y)].type));
            game.player.items[itemNumber(x-1,y)] = null;
            butt.start();
        }
        
        if((Keyboard.isKeyDown(Keyboard.KEY_E)&& butt.is())){
            if(game.player.hand == null){
                game.player.hand = game.player.items[itemNumber(x-1,y)];
                if(y==0)game.player.items[itemNumber(x-1,y)].unwear(game.player);
                game.player.items[itemNumber(x-1,y)] = null;
            } else if(game.player.items[itemNumber(x-1,y)]==null){
                
                if(y==0){
                    if(itemNumber(x-1,y)-9  ==game.player.hand.type){
                        game.player.items[itemNumber(x-1,y)]=game.player.hand;
                        game.player.items[itemNumber(x-1,y)].wear(game.player);
                        game.player.hand = null;
                    }
                } else {
                    game.player.items[itemNumber(x-1,y)]=game.player.hand;
                    game.player.hand = null;
                }
                
            } 
            butt.start();
        }
        
        if((Keyboard.isKeyDown(Input.KEY_ENTER) && butt.is())){
            if(y==1)
                switch(number(x,y)){
                case (1):
                    if(game.player.xp>=dmgcost){
                        game.player.dmg+=3;
                        game.player.xp-=dmgcost;
                        dmgcost+=50;
                    }
                    break;
                case (2):
                    
                        if(game.player.xp>=armcost){
                        game.player.maxhp+=5;
                        game.player.xp-=armcost;
                        armcost+=50;
                    }
                    break;
            }
            else {
                switch(itemNumber(x,y)){
                    
                }
            }
             butt.start();
        }
        
    }
    
    public int number(int x, int y) {
        return((y-1)*6+x);
    }
    
    public int itemNumber(int x, int y) {
        if(y==0)return 10+x;
        return((y-2)*5+x);
    }
    
    public void render(Graphics g, Player player) {
        g.drawImage(images.get("weapon"),352,120);
        g.drawImage(images.get("armour"),402,120);
        
        for(int i = 0; i < 14;i++){
            g.drawImage(images.get("empty"),player.loc_items[i].x,player.loc_items[i].y);
            if(i>9){
                g.setColor(Color.gray);
                g.drawString(""+(i-9), player.loc_items[i].x+12,player.loc_items[i].y+12);
            }
            if(player.items[i]!=null)
              player.items[i].invRender(g, player.loc_items[i].x,player.loc_items[i].y);
        }
        g.drawImage(images.get("empty"),900,600);
        if(player.hand!=null)player.hand.render(g, 900, 600);
        drawDescription(g);
        g.drawImage(images.get("frame"),y==0?502+x*50:299 + 50*x, y==0?119:69 + y*50);
        
    }
        
        public void drawDescription(Graphics g) {
            g.setColor(Color.green);
            g.drawString("Your inventory",900,30);
            switch(number(x,y)) {
                case (1):
                    g.drawString("Upgrade your weapon, increase damage",500,50);
                    g.drawString("Cost: " + dmgcost,500,70);
                    break;
                case (2):
                    g.drawString("Upgrade your armor, increase hit points",500,50);
                    g.drawString("Cost: " + armcost,500,70);
                    break;
            }
            g.setColor(Color.yellow);
            g.drawString("Upgrades",250,120);
            g.drawString("Equipment",560,100);
            g.drawString("Backpack",250,170);
            g.drawString("Your hand",900,580);
        }
}
