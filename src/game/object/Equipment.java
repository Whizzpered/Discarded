/*
 * Here we are
 */
package game.object;

import game.creature.Player;
import java.io.File;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Equipment {
    
    public int type, cost;
    public int modhp=0, moddmg=0;
    transient HashMap<String,Image> images = new HashMap<>();
    public boolean wearable;
    
    
    public Equipment(int type, Player player) {
        /*try {initSprites();} catch (Exception ex) {
            System.out.println("Error in Equipment: cannot load images");
        }*/
        this.type = type;
            wearable = true;
            switch(type){
                case(1): modhp = 4 + (int)(player.roomloc/2*3);
                        cost = modhp*40;
                    break;
                case(4):modhp = 2 + (int)(player.roomloc/3*2);
                        moddmg = 2 + (int)(player.roomloc/3*2);
                    cost = modhp*30 + moddmg*30;
                    break;
                case(3):modhp = 2 + (int)(player.roomloc/2*3);
                cost = modhp*40;
                    break;
                case(2):moddmg = 2 + (int)(player.roomloc/2*3);
                cost = moddmg*40;
                    break;
                
            }
    }
    
    public void initSprites() throws SlickException{
        File[] fList;
        File F = new File("res/equipment/");
        String path = "res/equipment/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length()-4);
            images.put(wut, new Image(path + fList[i].getName()));
        }
    }
    
    public void wear(Player player) {
        player.maxhp +=modhp;
        player.dmg +=moddmg;
        if(player.onBase)player.hp=player.maxhp;
    }
    
    public void unwear(Player player) {
        player.maxhp -=modhp;
        if(player.hp>player.maxhp)player.hp=player.maxhp;
        player.dmg -=moddmg;
    }
    
    public void render(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.drawString(""+type,x+12,y+type*12);
    }
    
    public void invRender(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.drawString(""+type,x+16,y+16);
    }
}
