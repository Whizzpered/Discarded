/*
 * Here we are
 */
package game.object;

import game.main.Game;
import java.io.File;
import java.util.HashMap;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Юрий Whizzpered
 */
public class Wearing extends Objects {
 
    public int type;
    Random r  = new Random();
    transient static HashMap<String,Image> images = new HashMap<>();
    
    public Wearing(double x, double y, int type) {
        super(x,y,type);
        this.type = type;
        try {initSprites();} catch (Exception ex) {
            System.out.print("Error in Wearing: cannot load images");
        }
    }
    
    public void initSprites() throws SlickException{
        File[] fList;
        File F = new File("res/wearing/");
        String path = "res/wearing/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length()-4);
            images.put(wut, new Image(path + fList[i].getName()));
        }
    }
    
    @Override
    public void tick(Game game) {
        super.tick(game);
        if(Math.abs(game.player.x-x)<=96 && Math.abs(game.player.y-y)<=128 && game.player.take){
            if(type<5)if(game.player.addItem(this))game.room.objects.remove(this);
            if(type==5){
                game.room.objects.remove(this);
                game.room.objects.add(new Wearing(x-76*(r.nextInt(1)-2),y-30,r.nextInt(4)+1));
                for(int i = 0; i < r.nextInt(5);i++){
                    game.room.objects.add(new Modifier(x-76*(r.nextInt(1)-2)-i*10,y-30,1));
                }
            }
        }
    }
    
    @Override
    public void render(Graphics g, Game game) {
        if(Math.abs(x - game.player.x)<=(Display.getWidth()/2)+200 && Math.abs(y - game.player.y)<=(Display.getHeight()/2)+200){
                if(type<5){
                    g.setColor(Color.cyan);
                    g.drawString(type+ "",(float)x,(float)y);
                }
                if(type==5){
                    g.drawImage(images.get("chest"),(float)x,(float)y);
                }
        }   
    }
      
}
