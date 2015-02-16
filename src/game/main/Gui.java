/*
 * Here we are
 */
package game.main;

import game.creature.Player;
import java.io.File;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Gui {
    
    Player player;
    int fps=0;
    Game game;
    static int xsize, ysize;
    boolean si;
    HashMap<String,Image> images = new HashMap<>();
    
    public Gui(Player pl) {
        player  = pl;
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in GUI: cannot load images");
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
    
    public void set(Game game) {
        this.game = game;
        xsize = game.sSizeX;
        ysize = game.sSizeY;
    }
    
    public void tick() {
        fps = game.app.getFPS();
        if(game.shop)si = true;
        else si = false;
    }
    
    public void render(Graphics g){
        g.setColor(Color.green);
        g.drawString("FPS:"+fps,0,25);
        if(!game.menu){
            g.setColor(Color.green);
            g.drawString("XP:"+player.xp, 0, 45);
            images.get("shop").draw(xsize-64, ysize - (ysize-50));
            if(si)images.get("frame").draw(xsize-65, ysize - (ysize-49));
            g.drawString("Room:"+player.roomloc,0,65);
            g.drawString("HP"+player.hp,0,85);
        }
    }
    
}
