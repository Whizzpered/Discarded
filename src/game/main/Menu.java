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
    
    public boolean settings,credits, died;
    public int y = 1;
    public Timer butt = new Timer("butt", 20);
    
    public void buttons(Game game) throws SlickException {
        if((Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_UP)) && butt.is()){
            if(settings?y>1:y>1)y--;
            butt.start();
        }
        if((Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)) && butt.is()){
            if(settings?y<2:y<4)y++;
            butt.start();
        }    
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && butt.is()){
            butt.start();
            game.sound.sounds.get("KingOfTheDesert").stop();
            game.menu = false;
        }
        
         if(Keyboard.isKeyDown(Input.KEY_ENTER)&& butt.is()){
            butt.start();
            if(settings){
               game.setSettings(y);    
            } else if(credits || died){
                credits = false;
                died = false;
            } else {
                switch(y){
                    case(1):
                            game.menu = false;
                            game.sound.sounds.get("KingOfTheDesert").stop();
                            if(!game.open){
                                game.training=true;
                                game.open=true;
                            }
                    break;
                    case(2):
                            settings = true;
                            y = 2;
                    break;
                    case(3):
                        credits = true;
                    break;
                    case(4):
                        game.app.exit();
                    break;
                    case(5):
                }
            }
        }
    }
   
    public void render(Graphics g, Game game) {
        
        int w = Display.getWidth();
        
        if(died){
            
            g.setColor(Color.green);
            g.drawString("You died. Press ENTER to go to menu", w/2-200,160);
            
        } else if(settings){
            g.setColor(Color.green);
            
            for(int i = 0; i < 2;i++){
                g.drawRect(w/2-200, 120+i*80, 400, 50);
            }
             
            g.setColor(Color.gray);
            g.fillRect(w/2-200, 40 + y*80, 400, 50);
            
            g.setColor(Color.green);
            g.drawString("Sound: " + (game.music?"yes":"no"), w/2-100,130);
            g.drawString("menu", w/2-100,220);
            
        } else if(credits){
            g.setColor(Color.green);
            g.drawString("Programmer: Whizzpered", w/2-200,160);
            g.drawString("Designer & Consultant: Yew Mentzaki", w/2-200,200);
            g.drawString("Assistance in the design : VanHunter", w/2-200,240);
            
        } else {
            g.setColor(Color.green);
            for(int i = 1; i < 5;i++){
                g.drawRect(w/2-200, 40 + i*80, 400, 50);
            }
        
                g.setColor(Color.gray);
                g.fillRect(w/2-201, 40 + y*80, 400, 50);
            
            
            g.setColor(Color.green);
            g.drawString(game.open?"Continue":"New Game", w/2-100,130);
            g.drawString("settings", w/2-100,210);
            g.drawString("about", w/2-100,290);
            g.drawString("exit", w/2-100,370);
        }
    }
}
