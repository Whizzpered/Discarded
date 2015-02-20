/*
 * Here we are
 */
package game.main;

import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Menu {
    
    public boolean settings,credits;
    public int but = 1;
    Timer butt = new Timer("butt", 20);
    
    public void buttons(Game game) throws SlickException {
        butt.tick();
        
        if((Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_UP)) && butt.is()){
            if(settings?but>2:but>1)but--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)) && butt.is()){
            if(settings?but<2:but<4)but++;
            butt.start();
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && butt.is()){
            butt.start();
            game.menu = false;
        }
        
         if(Keyboard.isKeyDown(Input.KEY_ENTER)&& butt.is()){
            butt.start();
            if(settings){
                switch(but){
                    case(2):
                        settings = false;
                    break;
                }
            } else if(credits){
                credits = false;
            } else {
                switch(but){
                    case(1):
                        game.menu = false;
                    break;
                    case(2):
                        settings = true;
                    break;
                    case(3):
                        credits = true;
                    break;
                    case(4):
                        game.app.exit();
                    break;
                }
            }
        }
    }
   
    public void render(Graphics g, Game game) {
        
        int w = Display.getWidth();
        int h = Display.getHeight();
        if(settings){
            g.setColor(Color.green);
             g.drawRect(w/2-200, 280, 400, 50);
             
            g.setColor(Color.gray);
            g.fillRect(w/2-200, 40 + but*120, 400, 50);
            
            g.setColor(Color.green);
            g.drawString("menu", w/2-100,280);
            
        } else if(credits){
            g.setColor(Color.green);
            g.drawString("Programmer: Whizzpered", w/2-200,160);
            g.drawString("Designer & Consultant: Yew Mentzaki", w/2-200,200);
            g.drawString("Аssistance in the design : VanHunter", w/2-200,240);
            
        } else {
            g.setColor(Color.green);
            for(int i = 1; i < 5;i++){
                g.drawRect(w/2-200, 40 + i*80, 400, 50);
            }
        
            g.setColor(Color.gray);
            g.fillRect(w/2-200, 40 + but*80, 400, 50);
        
            g.setColor(Color.green);
            g.drawString("play", w/2-100,130);
            g.drawString("settings", w/2-100,210);
            g.drawString("about", w/2-100,290);
            g.drawString("exit", w/2-100,370);
        }
    }
}
