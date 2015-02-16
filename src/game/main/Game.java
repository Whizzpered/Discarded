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
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

    public Game game = this;
    public Player player;
    public Shop shp;
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
                        shp.buttons(game);
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
                    for (NPC en : room.getNPCArr(room.npcies)) {
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
                    for (Bullet bul : room.getBullArr(room.bullets)) {
                        bul.tick(room, game);
                    }
                    for (Objects obj : room.getObjArr(room.objects)) {
                        obj.tick(game);
                    }
                }

            }
        }, 0, 10);
    }

    public static void main(String[] arguments) throws SlickException {
        setNatives();
        app = new AppGameContainer(new Game());
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

        if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            player.y -= 3;
        }
        if ((Keyboard.isKeyDown(Keyboard.KEY_SPACE)) && !player.onStairs) {
            player.jump(room);
        }

        if ((Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_UP)) && !player.onStairs); 
        else if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
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
        if (Keyboard.isKeyDown(Keyboard.KEY_P) && shp.butt.is()) {
            shop = true;
            shp.butt.start();
        }
        men.butt.tick();
        shp.butt.tick();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        menu = true;
        room = new Room(1, 0);
        player = new Player();
        shp = new Shop();
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
            men.render(g, game, sSizeX);
        } else {
            room.render(g, real_cam_x, real_cam_y, sSizeX, sSizeY, player, game); // There is a hero, npc'ies and objects render
            if (shop) {
                shp.render(g);
            }
            if(training){
                
            }
        }
        player.gui.render(g);
    }

    public void cameras() {
        cam_y = ((cam_y * 7) + (int) player.y) / 8;
        cam_x = ((cam_x * 70) + (int) player.x) / 71;
        real_cam_x = (int) (player.x * 2 - cam_x - (sSizeX / 2));
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
