/*
 * Here we are
 */
package game.object;

import java.io.File;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Equipment {
    
    HashMap<String,Image> images = new HashMap<>();
    
    
    public Equipment() {
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in Equipment: cannot load images");
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
    
    public void tick() {
        
    }
    
    
    public void render(Graphics g, int x, int y) {
        g.drawImage(images.get("empty"),x,y);
    }
}
