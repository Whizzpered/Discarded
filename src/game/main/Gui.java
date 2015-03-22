/*
 * Here we are
 */
package game.main;

import game.creature.Player;
import java.io.File;
import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Gui {
    
    Player player;
    int fps=0;
    Game game;
    boolean si;
    static HashMap<String,Image> images = new HashMap<>();
    
    public Gui(Player pl) {
        player  = pl;
        try {initSprites();} catch (Exception ex) {
            System.out.println("Error in GUI: cannot load images");
        }
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
    
    
    
    public void tick(Game game) {
        fps = game.app.getFPS();
        if(game.shop)si = true;
        else si = false;
    }
    
    public void render(Graphics g, Game game){
        
        int xsize = Display.getWidth();
        int ysize = Display.getHeight();
        
        g.setColor(fps>30?Color.green:Color.yellow);
        g.drawString("FPS:"+fps,0,10);
        if(!game.menu){
            g.setColor(Color.green);
            g.drawString("XP:"+player.xp, 0, 30);
            images.get("shop").draw(xsize-64, ysize - (ysize-50));
            if(si)images.get("frame").draw(xsize-65, ysize - (ysize-49));
            if(game.player.full)g.drawString("Inventory full", xsize/2-65, 135);
            g.drawString("Room:"+player.roomloc,0,50);
            g.drawString("HP"+player.hp,0,70);
            g.drawRect(90, 70, player.maxhp*5, 20);
            for(int i = 0; i < player.hp;i++){
                g.fillRect(90+i*5, 70, 5, 20);
            }
            g.drawString("of "+player.maxhp,38,70);
            g.drawString("Damage"+player.dmg,0,90);
            if(!game.player.onBase){
                g.drawString("Energy"+player.energy,0,110);
                g.setColor(Color.yellow);
                g.drawRect(90, 110, 100, 20);
                for(int i = 0; i < player.energy;i++){
                    g.drawRect(90+i, 110, 1, 20);
                }
            }
        }
    }
}
