/*
 * Here we are
 */
package game.main;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import game.creature.NPC;
import game.creature.Player;

import game.object.Bullet;
import game.object.Objects;
import game.world.Base;
import game.world.Block;
import game.world.Room;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

    public Game game;
    public Sound sound;
    public Player player;
    public Menu men;
    public Base bas;
    public Room room, prevRoom;
    
    public PrintWriter wrt;

    public float angle;
    public int cam_y = 0, cam_x = 0, real_cam_x, real_cam_y;
    public static int sSizeX, sSizeY;
    public boolean menu, shop, training, open, music=true, base;
    Graphics g = new Graphics();

    public static AppGameContainer app;

    public Game() {
        super("...");
        game= this;
    }
    
    public void playerDie() throws SlickException {
        room = new Room(1, 0);
        men.died = true;
        menu = true;
        open = false;
        player = new Player();
    }
    
    public void save() {
            app.pause();
            XStream x = new XStream(new DomDriver());
            new File("res/save.xml").delete();
            
            player.animations = null;
            player.timer = null;
            player.gui = null;
            player.pause = true;
            try {
            wrt = new PrintWriter(new File("res/save.xml"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            wrt.print(x.toXML(player));
            wrt.close();
            player.assign();
            player.pause = false;
            app.resume();

    }
    
    public void load() {
        if(new File("res/save.xml").exists()){
            app.pause();
            XStream x = new XStream(new DomDriver());
            Object h = x.fromXML(new File("res/save.xml"));
            player = (Player)h;
            player.assign();
            try {
                room = new Room(player.roomloc,(int)(player.y/64));
            } catch (SlickException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            player.pause = false;
            app.resume();
        }
    }
    
    public void setSettings(int index) throws SlickException {
        switch(index){
            case(1):
                    if(music){
                        music = false;
                        sound.sounds.get("KingOfTheDesert").stop();
                    }
                    else{
                        music = true;
                        sound.playSound("KingOfTheDesert", 1, 1, true); 
                    }
                break;
                
            case(2):
                men.settings = false;
            break;
        }
    }

    public void timer() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (shop) {
                    try {
                        player.inventory.buttons(game);
                    } catch (Exception ex) {
                    }
                } else if (menu) {
                    try {
                        men.buttons(game);
                    } catch (Exception ex) {
                    }
                    
                }else if(player.onBase){
                    try {
                        if(shop){
                            player.inventory.buttons(game);
                        }
                        bas.button(game);
                    } catch (Exception ex) {
                    }
                } else {
                    try {
                        buttons();
                    } catch (Exception ex) {
                    }
                }
                bas.butt.tick();
                player.gui.tick(game);
                player.inventory.butt.tick();
                men.butt.tick();
                cameras();
            }
        }, 0, 10);
    }

    public void creatureTimer() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (!menu && !shop && !training && !base && !player.onBase && !app.isPaused()) {
                    player.tick(game);
                    room.blockTim--;
                    for (NPC en : room.getNPCArr()) {
                        en.tick(game);
                    }
                }
            }
        }, 0, 10);

    }

    public void objectsTimer() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (!menu && !shop && !training && !base && !player.onBase) {
                    for (Bullet bul : room.getBullArr()) {
                        bul.tick(room, game);
                    }
                    for (Objects obj : room.getObjArr()) {
                        obj.tick(game);
                    }
                }

            }
        }, 0, 10);
    }

    public static void main(String[] arguments) throws SlickException {
        setUpNatives();
        app = new AppGameContainer(new Game());
        Display.setResizable(true);
        app.setDisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight()-50, false);
        sSizeX = app.getWidth();
        sSizeY = app.getHeight();
        app.setDefaultMouseCursor();
        app.setAlwaysRender(true);
        app.setTargetFrameRate(80);
        app.setShowFPS(false);
        app.start();
    }

    public void buttons() throws SlickException {
        
        if(base){
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && bas.butt.is()) {
                base = false;
                player.onBase = false;
                bas.butt.start();
                men.butt.start();
            }
            if (Keyboard.isKeyDown(Input.KEY_ENTER) && bas.butt.is()) {
                base = false;
                player.onBase = true;
                bas.butt.start();
            }
        }
        else {
            if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            player.vx = -1;
            player.dir = -1;
            if(player.ready("wait") && !player.anim.equals("going"))player.anim = "going";
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            player.vx = 1;
            player.dir = 1;
            if(player.ready("wait")&& !player.anim.equals("going"))player.anim = "going";
        } else {
            player.vx = 0;
            if(player.ready("wait")&& !player.anim.equals("stand"))player.anim = "stand";
        }

        if ((Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))) {
            if(player.onStairs)player.y -= 3;
            else {
                if(player.onGround && player.duck==1){
                    player.y-=64;
                    player.duck=0;
                } 
            }
        }
        if ((Keyboard.isKeyDown(Keyboard.KEY_SPACE)) && !player.onStairs) {
            player.jump(room);
            if(player.duck==1)player.duck=0;
        }

        if ((Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))) {
            if(player.onStairs)player.y += 3;
            else {
                if(player.onGround && player.duck==0){
                    player.duck=1;
                }
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q) && player.ready("shoot")) {
            player.start("shoot");
            player.shoot(game);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && player.ready("button")) {
            player.take = true;
            player.start("button");
        } else player.take = false;
        
        
        if (Keyboard.isKeyDown(Keyboard.KEY_I) && player.inventory.butt.is()) {
            player.invent = true;
            shop = true;
            player.inventory.butt.start();
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_Z) && player.ready("button")) {
            player.crash(game);
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (!player.sprint) {
                player.sprint = true;
            }
        } else {
            player.sprint = false;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && men.butt.is()) {
            menu = true;
            if(music)sound.playSound("KingOfTheDesert", 1, 1, true); 
            men.butt.start();
        }
        
        if (Keyboard.isKeyDown(Input.KEY_ENTER) && men.butt.is() && training) {
            training = false;
            base = true;
            bas.butt.start();
            men.butt.start();
        }
        }
    }

    public void setSources() throws SlickException {
        app.setDisplayMode(512, 256, false);
        Display.setTitle("Loading...");
            room = new Room(1, 0);
            player = new Player();
            player.inventory = new Inventory();
            men = new Menu();
            bas = new Base(game);
            sound = new Sound();
            sound.setSounds();
            if(music)sound.playSound("KingOfTheDesert", 1, 1, true); 
            Block.setBlocks();
        app.setDisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight()-50, false);
        Display.setTitle("Here we go");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException {
        setSources();
        menu = true;
        training = true;   
        
        timer();
        creatureTimer();
        objectsTimer();
    }
    

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        
    }
    
    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if(player.onBase){
            bas.render(g);
            player.staticRender(g,Display.getWidth()/2-55, Display.getHeight()/2+128);
            if (player.invent) {
                player.inventory.render(g,game.player);
            }
        }
        else if(base){
            g.setColor(Color.green);
            g.drawString("Press ENTER to enter base or ESC to continue",Display.getWidth()/2-200,240);
        } else if (menu) {
            men.render(g, game);
        } else if(training){
            g.setColor(Color.green);
                g.drawString("WASD or arrows  for moving", Display.getWidth()/2-200,160);
                g.drawString("SPACE for jumping", Display.getWidth()/2-200,200);
                g.drawString("Q for shooting", Display.getWidth()/2-200,240);
                g.drawString("I for inventory and upgrades", Display.getWidth()/2-200,280);
        }
        else {
            room.render(g, real_cam_x, real_cam_y, player, game); // There is a hero, npc'ies and objects render 
        }
        if(player.gui!=null)player.gui.render(g, game);
    }

    public void cameras() {
        cam_y = ((cam_y * 7) + (int) player.y) / 8;
        cam_x = ((cam_x * 50) + (int) player.x) / 51;
        real_cam_x = (int) (cam_x - (sSizeX / 2));
        real_cam_y = (int) (cam_y - (sSizeY / 2));
    }

    public static void setUpNatives() {
        if (!new File("natives").exists()) {
        JOptionPane.showMessageDialog(null, "Error!\nNative libraries not found!");
        System.exit(1);
    }
    try {
        System.setProperty("java.library.path", new File("natives").getAbsolutePath());

        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);

    try {
        fieldSysPath.set(null, null);
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
        System.exit(1);
    } catch (IllegalAccessException ex) {
        JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
        System.exit(1);
    }
    } catch (NoSuchFieldException ex) {
        JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
        System.exit(1);
    } catch (SecurityException ex) {
        JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
        System.exit(1);
}}
}
