/*
 * Here we are
 */
package game.main;

import game.creature.NPC;
import game.creature.Player;
import game.object.Bullet;
import game.object.Objects;
import game.world.Block;
import game.world.Room;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
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

    public Game game = this;
    public Player player;
    public Menu men;
    public Room room, prevRoom;

    public float angle;
    public int cam_y = 0, cam_x = 0, real_cam_x, real_cam_y;
    public static int sSizeX, sSizeY;
    public boolean menu, shop, training;

    public static AppGameContainer app;

    public Game() {
        super("...");
    }

    public void timer() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (shop) {
                    try {
                        player.shop.buttons(game);
                    } catch (Exception ex) {
                    }
                } else if (menu) {
                    try {
                        men.buttons(game);
                    } catch (Exception ex) {
                    }
                } else {
                    try {
                        buttons();
                    } catch (Exception ex) {
                    }
                }
                player.gui.tick();
                cameras();
            }
        }, 0, 10);
    }

    public void creatureTimer() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (!menu && !shop && !training) {
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
                if (!menu && !shop && !training) {
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
        setNatives();
        app = new AppGameContainer(new Game());
        Display.setResizable(true);
        app.setDisplayMode(1366, 768, false);
        sSizeX = app.getWidth();
        sSizeY = app.getHeight();
        app.setDefaultMouseCursor();
        app.setAlwaysRender(true);
        app.setShowFPS(false);
        app.start();
    }

    public void buttons() throws SlickException {
        if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            player.vx = -1;
            player.dir = -1;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            player.vx = 1;
            player.dir = 1;
        } else {
            player.vx = 0;
        }

        if ((Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))&& player.onStairs) {
            player.y -= 3;
        }
        if ((Keyboard.isKeyDown(Keyboard.KEY_SPACE)) && !player.onStairs) {
            player.jump(room);
        }

        if ((Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) && player.onStairs) {
            player.y += 3;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q) && player.ready("shoot")) {
            player.start("shoot");
            player.shoot(game);
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
            men.butt.start();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_I) && player.shop.butt.is()) {
            player.invent = true;
            shop = true;
            player.shop.butt.start();
        }
        if (Keyboard.isKeyDown(Input.KEY_ENTER) && men.butt.is() && training) {
            training = false;
            men.butt.start();
        }
        men.butt.tick();
        player.shop.butt.tick();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        menu = true;
        training = true;
        room = new Room(1, 0);
        player = new Player();
        player.shop = new Inventory();
        men = new Menu();
        player.gui.set(game);
        Block.setBlocks();
        timer();
        creatureTimer();
        objectsTimer();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        if (menu) {
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
        player.gui.render(g);
    }

    public void cameras() {
        cam_y = ((cam_y * 7) + (int) player.y) / 8;
        cam_x = ((cam_x * 70) + (int) player.x) / 71;
        real_cam_x = (int) (cam_x - (sSizeX / 2));
        real_cam_y = (int) (cam_y - (sSizeY / 2));
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public static void setNatives() {
        try {

            if (isUnix()) {
                System.setProperty("java.library.path", new File("natives/natives-linux/").getAbsolutePath());
            } else if (isMac()) {
                System.setProperty("java.library.path", new File("natives/natives-mac/").getAbsolutePath());
            } else if (isWindows()) {
                System.setProperty("java.library.path", new File("natives/natives-windows/").getAbsolutePath());
            }
            System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

            java.lang.reflect.Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            try {
                fieldSysPath.set(null, null);
            } catch (Exception ex) {
                System.exit(1);
            }

        } catch (Exception ex) {
            System.exit(1);
        }
    }
}
